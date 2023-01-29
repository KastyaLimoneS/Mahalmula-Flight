package net.kastya_limoness.mahalmula_flight2.events;

import net.kastya_limoness.mahalmula_flight2.MahalmulaFlightII;
import net.kastya_limoness.mahalmula_flight2.entities.MF2EntityTypes;
import net.kastya_limoness.mahalmula_flight2.entities.MahalmulaShipEntity;
import net.kastya_limoness.mahalmula_flight2.rites.MF2StructMatcher;
import net.kastya_limoness.mahalmula_flight2.teleportation.MF2GameModeHelper;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MahalmulaFlightII.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MF2EntityEvents {
    @SubscribeEvent
    public static void onPlayerUSe(final PlayerInteractEvent event)
    {
        if (event.getWorld().isClientSide) return;
        PlayerEntity player = event.getPlayer();
        if (player.getMainHandItem().getItem().equals(Items.SADDLE) && player.getOffhandItem().getItem().equals(Items.ELYTRA))
        {
            if (MF2StructMatcher.match(event.getWorld(), event.getPos(), MF2StructMatcher.SHIP_STRUCT)) {
                MahalmulaFlightII.LOGGER.info("Hello! How you doing, creator?? UwU");
                MF2StructMatcher.doWithPattern(event.getWorld(), event.getPos(), MF2StructMatcher.SHIP_STRUCT, (world, pos, block) -> world.removeBlock(pos, false));
                MF2EntityTypes.MAHALMULA_SHIP_TYPE.get().spawn((ServerWorld) event.getWorld(), null, null, event.getPos(), SpawnReason.TRIGGERED, false, false);
                if (MF2GameModeHelper.getGameMode(player, event.getWorld().getServer()) != GameType.CREATIVE)
                {
                    player.getMainHandItem().shrink(1);
                    player.getOffhandItem().shrink(1);
                }
            }
            return;
        }
        if (player.getVehicle() instanceof MahalmulaShipEntity)
            ((MahalmulaShipEntity) player.getVehicle()).riderUsedItem(player.getMainHandItem());
    }


}
