package net.kastya_limoness.mahalmula_flight2.entities;

import net.kastya_limoness.mahalmula_flight2.MahalmulaFlightII;
import net.kastya_limoness.mahalmula_flight2.blocks.MF2Blocks;
import net.kastya_limoness.mahalmula_flight2.flight.FlightEvent;
import net.kastya_limoness.mahalmula_flight2.items.MF2Items;
import net.kastya_limoness.mahalmula_flight2.teleportation.MF2GameModeHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.ExplosionContext;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class MahalmulaShipEntity extends Entity {
    public static final DataParameter<Integer> YDIR_PARAMETER = EntityDataManager.defineId(MahalmulaShipEntity.class, DataSerializers.INT);
    public static final DataParameter<Integer> SKIN_PARAMETER = EntityDataManager.defineId(MahalmulaShipEntity.class, DataSerializers.INT);
    public static final DataParameter<Integer> EFFECTS_PARAMETER = EntityDataManager.defineId(MahalmulaShipEntity.class, DataSerializers.INT);
    public static final DataParameter<Boolean> FALLING_PARAMETER = EntityDataManager.defineId(MahalmulaShipEntity.class, DataSerializers.BOOLEAN);

    private Vector3d savedDir = Vector3d.ZERO;
    private int ticker = 0;
    private int lerpSteps;

    private int eventTime = 0;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private FlightEvent event;

    private double speed = 0.6;

    public MahalmulaShipEntity(EntityType<?> p_i48580_1_, World p_i48580_2_) {
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
        move(MoverType.SELF, entityData.get(FALLING_PARAMETER)? savedDir : getDeltaMovement().scale(speed));
        move(MoverType.SELF, Vector3d.ZERO.add(0, byFall(-1, entityData.get(YDIR_PARAMETER)*0.5), 0));
    }

    public int getTicker() {return ticker;}

    @Override
    public ActionResultType interact(PlayerEntity player, Hand hand) {
        if (level.isClientSide) return ActionResultType.PASS;
        player.startRiding(this);
        return ActionResultType.SUCCESS;
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
            level.explode(this, null, null, pos.getX(), pos.getY(), pos.getZ(), 1, false, Explosion.Mode.NONE);
            this.remove();
        }
    }

    @Override
    protected boolean canAddPassenger(Entity p_184219_1_) {
        return getPassengers().size() == 0;
    }

    @Override
    public void onSyncedDataUpdated(DataParameter<?> parameter) {
        if (parameter.getId() == EFFECTS_PARAMETER.getId())
            if (level.isClientSide)
                effectStuff(entityData.get(EFFECTS_PARAMETER));
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        entityData.set(SKIN_PARAMETER, nbt.getInt("skin"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        nbt.putInt("skin", entityData.get(SKIN_PARAMETER));
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public Vector3d getDismountLocationForPassenger(LivingEntity entity) {
        return position().add(0, 1, 0);
    }

    public void riderUsedItem(ItemStack stack)
    {
        if (stack.getItem().equals(MF2Items.COLORFUL_SHELL.get())) {
            entityData.set(SKIN_PARAMETER, (entityData.get(SKIN_PARAMETER) + 1) % 7);
            entityData.set(EFFECTS_PARAMETER, (entityData.get(EFFECTS_PARAMETER) == 5)? 6 : 5);
            if (MF2GameModeHelper.getGameMode((PlayerEntity) getRider(), level.getServer()) != GameType.CREATIVE) stack.shrink(1);
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
                level.playSound((PlayerEntity) getRider(), blockPosition(), SoundEvents.BELL_BLOCK, SoundCategory.BLOCKS, 1f, random.nextFloat());
                break;
            case 2:
                for (int i = 0; i < 30; i++) level.addParticle(ParticleTypes.REVERSE_PORTAL, position().x + (0.5 - level.random.nextFloat()) * 3, position().y + 1.5, position().z + (0.5 - level.random.nextFloat()) * 3, 0, 0, 0);
                level.playSound((PlayerEntity) getRider(), blockPosition(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundCategory.BLOCKS, 1f, random.nextFloat());
                break;
            case 3:
                for (int i = 0; i < 30; i++) level.addParticle(ParticleTypes.HAPPY_VILLAGER, position().x + (0.5 - level.random.nextFloat()) * 3, position().y + 1.5, position().z + (0.5 - level.random.nextFloat()) * 3, 0, 0, 0);
                level.playSound((PlayerEntity) getRider(), blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1f, random.nextFloat());
                break;
            case 4:
                for (int i = 0; i < 30; i++) level.addParticle(ParticleTypes.FLAME, position().x + (0.5 - level.random.nextFloat()) * 3, position().y + 1.5, position().z + (0.5 - level.random.nextFloat()) * 3, 0, 0, 0);
                level.playSound((PlayerEntity) getRider(), blockPosition(), SoundEvents.FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1f, random.nextFloat());
                break;
            case 5:
                for (int i = 0; i < 30; i++) level.addParticle(ParticleTypes.SOUL, position().x + (0.5 - level.random.nextFloat()) * 3, position().y + 1.5, position().z + (0.5 - level.random.nextFloat()) * 3, 0, 0, 0);
                level.playSound((PlayerEntity) getRider(), blockPosition(), SoundEvents.ENDER_CHEST_OPEN, SoundCategory.BLOCKS, 1f, random.nextFloat());
                break;
            case 6:
                for (int i = 0; i < 30; i++) level.addParticle(ParticleTypes.SOUL, position().x + (0.5 - level.random.nextFloat()) * 3, position().y + 1.5, position().z + (0.5 - level.random.nextFloat()) * 3, 0, 0, 0);
                level.playSound((PlayerEntity) getRider(), blockPosition(), SoundEvents.ENDER_CHEST_OPEN, SoundCategory.BLOCKS, 1f, random.nextFloat());
                break;
            case 7:
                level.addParticle(ParticleTypes.FLASH, position().x + (0.5 - level.random.nextFloat()) * 3, position().y + 1.5, position().z + (0.5 - level.random.nextFloat()) * 3, 0, 0, 0);
                for (PlayerEntity pl : level.getServer().getPlayerList().getPlayers()) level.playSound(pl, blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1f, random.nextFloat());
                break;
            case 8:
                level.addParticle(ParticleTypes.FLASH, position().x + (0.5 - level.random.nextFloat()) * 3, position().y + 1.5, position().z + (0.5 - level.random.nextFloat()) * 3, 0, 0, 0);
                for (PlayerEntity pl : level.getServer().getPlayerList().getPlayers()) level.playSound(pl, blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1f, random.nextFloat());
                break;
            default:
                break;
        }
    }

    private void eventStuff()
    {
        if (entityData.get(FALLING_PARAMETER) || !(getRider() instanceof PlayerEntity) || MF2GameModeHelper.getGameMode((PlayerEntity) getRider(), level.getServer()) == GameType.CREATIVE) return;
        if (event == null)
        {
            if (level.random.nextFloat() < 0.01)
            {
                event = FlightEvent.randomEvent(level.random);
                eventTime = 50;
                event.onCreating(this);
                entityData.set(EFFECTS_PARAMETER, 1);
                ((PlayerEntity) getRider()).displayClientMessage(new TranslationTextComponent(MahalmulaFlightII.MODID + ".warnings." + event.getEventName()), true);
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
        if (getRider() == null) {setDeltaMovement(Vector3d.ZERO); return;}
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
