package net.kastya_limoness.mahalmula_flight2.entities;

import net.kastya_limoness.mahalmula_flight2.MahalmulaFlightII;
import net.kastya_limoness.mahalmula_flight2.blocks.MF2Blocks;
import net.kastya_limoness.mahalmula_flight2.flight.FlightEvent;
import net.kastya_limoness.mahalmula_flight2.items.MF2Items;
import net.kastya_limoness.mahalmula_flight2.utils.MF2GameModeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

public class MahalmulaShipEntity extends Entity {
    public static final EntityDataAccessor<Integer> YDIR_PARAMETER = SynchedEntityData.defineId(MahalmulaShipEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> SKIN_PARAMETER = SynchedEntityData.defineId(MahalmulaShipEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> EFFECTS_PARAMETER = SynchedEntityData.defineId(MahalmulaShipEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> FALLING_PARAMETER = SynchedEntityData.defineId(MahalmulaShipEntity.class, EntityDataSerializers.BOOLEAN);

    private Vec3 savedDir = Vec3.ZERO;
    private int ticker = 0;
    private int lerpSteps;

    private int eventTime = 0;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private FlightEvent event;

    private double speed = 0.6;

    public MahalmulaShipEntity(EntityType<?> p_i48580_1_, Level p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    @Override
    public boolean isPickable() {return true;}

    @Override
    public void tick() {
        commonTick();
        if (level.isClientSide) clientTick();
        else serverTick();
    }

    private void clientTick()
    {
        ticker = ++ticker % 360;
        showSmokeParticles(30);
    }

    private void serverTick()
    {
        eventStuff();
        if (entityData.get(FALLING_PARAMETER))
            fallLogic();
    }

    private void commonTick()
    {
        tickLerp();
        horisControl();
        move(MoverType.SELF.SELF, entityData.get(FALLING_PARAMETER)? savedDir : getDeltaMovement().scale(speed));
        move(MoverType.SELF, Vec3.ZERO.add(0, byFall(-1, entityData.get(YDIR_PARAMETER)*0.5), 0));
    }

    public int getTicker() {return ticker;}

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (level.isClientSide) return InteractionResult.PASS;
        player.startRiding(this);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(YDIR_PARAMETER, 0);
        entityData.define(SKIN_PARAMETER, 0);
        entityData.define(EFFECTS_PARAMETER, 0);
        entityData.define(FALLING_PARAMETER, false);
    }

    @Override
    protected void checkFallDamage(double p_184231_1_, boolean p_184231_3_, BlockState state, BlockPos pos) {
        if (level.isClientSide) return;
        if (level.getBlockState(pos.below()).is(Blocks.AIR)) return;
        if (entityData.get(FALLING_PARAMETER)) {
            level.setBlockAndUpdate(pos, MF2Blocks.ABADONED_SHIP.get().defaultBlockState());
            level.explode(this, null, null, pos.getX(), pos.getY(), pos.getZ(), 1, false, Explosion.BlockInteraction.NONE);
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    protected boolean canAddPassenger(Entity p_184219_1_) {
        return getPassengers().size() == 0;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> parameter) {
        if (parameter.getId() == EFFECTS_PARAMETER.getId())
            if (level.isClientSide)
                effectStuff(entityData.get(EFFECTS_PARAMETER));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        entityData.set(SKIN_PARAMETER, nbt.getInt("skin"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt("skin", entityData.get(SKIN_PARAMETER));
    }



    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity entity) {
        return position().add(0, 1, 0);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void riderUsedItem(ItemStack stack)
    {
        if (stack.getItem().equals(MF2Items.COLORFUL_SHELL.get())) {
            entityData.set(SKIN_PARAMETER, (entityData.get(SKIN_PARAMETER) + 1) % 7);
            entityData.set(EFFECTS_PARAMETER, (entityData.get(EFFECTS_PARAMETER) == 5)? 6 : 5);
            if (MF2GameModeHelper.getGameMode((Player) getRider(), level.getServer()) != GameType.CREATIVE) stack.shrink(1);
        }
        if (event != null && event.acceptItem(stack))
        {
            event.onDisapearing(this);
            event = null;
        }
    }

    private void effectStuff(int effect)
    {
        switch (effect) {
            case 1:
                level.playSound((Player) getRider(), blockPosition(), SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 1f, random.nextFloat());
                break;
            case 2:
                for (int i = 0; i < 30; i++) level.addParticle(ParticleTypes.REVERSE_PORTAL, position().x + (0.5 - level.random.nextFloat()) * 3, position().y + 1.5, position().z + (0.5 - level.random.nextFloat()) * 3, 0, 0, 0);
                level.playSound((Player) getRider(), blockPosition(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.BLOCKS, 1f, random.nextFloat());
                break;
            case 3:
                for (int i = 0; i < 30; i++) level.addParticle(ParticleTypes.HAPPY_VILLAGER, position().x + (0.5 - level.random.nextFloat()) * 3, position().y + 1.5, position().z + (0.5 - level.random.nextFloat()) * 3, 0, 0, 0);
                level.playSound((Player) getRider(), blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1f, random.nextFloat());
                break;
            case 4:
                for (int i = 0; i < 30; i++) level.addParticle(ParticleTypes.FLAME, position().x + (0.5 - level.random.nextFloat()) * 3, position().y + 1.5, position().z + (0.5 - level.random.nextFloat()) * 3, 0, 0, 0);
                level.playSound((Player) getRider(), blockPosition(), SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1f, random.nextFloat());
                break;
            case 5:
                for (int i = 0; i < 30; i++) level.addParticle(ParticleTypes.SOUL, position().x + (0.5 - level.random.nextFloat()) * 3, position().y + 1.5, position().z + (0.5 - level.random.nextFloat()) * 3, 0, 0, 0);
                level.playSound((Player) getRider(), blockPosition(), SoundEvents.ENDER_CHEST_OPEN, SoundSource.BLOCKS, 1f, random.nextFloat());
                break;
            case 6:
                for (int i = 0; i < 30; i++) level.addParticle(ParticleTypes.SOUL, position().x + (0.5 - level.random.nextFloat()) * 3, position().y + 1.5, position().z + (0.5 - level.random.nextFloat()) * 3, 0, 0, 0);
                level.playSound((Player) getRider(), blockPosition(), SoundEvents.ENDER_CHEST_OPEN, SoundSource.BLOCKS, 1f, random.nextFloat());
                break;
            case 7:
                level.addParticle(ParticleTypes.FLASH, position().x + (0.5 - level.random.nextFloat()) * 3, position().y + 1.5, position().z + (0.5 - level.random.nextFloat()) * 3, 0, 0, 0);
                for (Player pl : level.getServer().getPlayerList().getPlayers()) level.playSound(pl, blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1f, random.nextFloat());
                break;
            case 8:
                level.addParticle(ParticleTypes.FLASH, position().x + (0.5 - level.random.nextFloat()) * 3, position().y + 1.5, position().z + (0.5 - level.random.nextFloat()) * 3, 0, 0, 0);
                for (Player pl : level.getServer().getPlayerList().getPlayers()) level.playSound(pl, blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1f, random.nextFloat());
                break;
            default:
                break;
        }
    }

    private void eventStuff()
    {
        if (entityData.get(FALLING_PARAMETER) || !(getRider() instanceof Player) || MF2GameModeHelper.getGameMode((Player) getRider(), level.getServer()) == GameType.CREATIVE) return;
        if (event == null)
        {
            if (level.random.nextFloat() < 0.01)
            {
                event = FlightEvent.randomEvent(level.random);
                eventTime = 50;
                event.onCreating(this);
                entityData.set(EFFECTS_PARAMETER, 1);
                ((Player) getRider()).displayClientMessage(new TranslatableComponent(MahalmulaFlightII.MODID + ".warnings." + event.getEventName()), true);
            }
        }
        else {
            if (eventTime > 0) eventTime--;
            else {entityData.set(FALLING_PARAMETER, true); event = null;}
        }
    }



    private void tickLerp() {
        if (this.lerpSteps > 0) {
            double d0 = this.getX() + (this.lerpX - this.getX()) / (double)this.lerpSteps;
            double d1 = this.getY() + (this.lerpY - this.getY()) / (double)this.lerpSteps;
            double d2 = this.getZ() + (this.lerpZ - this.getZ()) / (double)this.lerpSteps;
            --this.lerpSteps;
            this.setPos(d0, d1, d2);
        }
    }
    @OnlyIn(Dist.CLIENT)
    public void lerpTo(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_) {
        this.lerpX = p_180426_1_;
        this.lerpY = p_180426_3_;
        this.lerpZ = p_180426_5_;
        this.lerpSteps = 10;
    }

    private void fallLogic()
    {
        fallDistance += 1;
    }

    private double byFall(double a, double b)
    {return entityData.get(FALLING_PARAMETER)? a : b;}

    private Entity getRider()
    {
        if (getPassengers().size() == 0) return null;
        return getPassengers().get(0);
    }
    private void horisControl()
    {
        if (getRider() == null) {setDeltaMovement(Vec3.ZERO); return;}
        if (entityData.get(FALLING_PARAMETER)) return;
        savedDir = getRider().getDeltaMovement().multiply(1, 0, 1).normalize();
        setDeltaMovement(savedDir);
    }

    private void showSmokeParticles(int count)
    {
        if (entityData.get(FALLING_PARAMETER)) {
            for (int i = 0; i < count; i++) {
                level.addParticle(level.random.nextFloat() > 0.1 ? ParticleTypes.SMOKE : ParticleTypes.FLAME, position().x + (0.5 - level.random.nextFloat()) * 3, position().y + 0.5, position().z + (0.5 - level.random.nextFloat()) * 3, 0, 0, 0);
            }
        }
        else {
            for (int i = 0; i < 8; i++) {
                double angle = Math.toRadians(ticker + 45 * i);
                level.addParticle(ParticleTypes.END_ROD, position().x + Math.cos(angle) * 1.5, position().y, position().z + + Math.sin(angle) * 1.5, 0, 0, 0);
            }
        }
    }
}
