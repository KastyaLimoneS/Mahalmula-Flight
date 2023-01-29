package net.kastya_limoness.mahalmula_flight2.network;

import net.kastya_limoness.mahalmula_flight2.entities.MahalmulaShipEntity;
import net.kastya_limoness.mahalmula_flight2.items.TeleportationModule;
import net.kastya_limoness.mahalmula_flight2.teleportation.MF2DimensionHelper;
import net.kastya_limoness.mahalmula_flight2.teleportation.MF2Teleporter;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TeleportKeyPressedMessage {
    public int i;
    public TeleportKeyPressedMessage(int i)
    {
        this.i = i;
    }
    public static void encode(TeleportKeyPressedMessage message, PacketBuffer buffer)
    {
        buffer.writeInt(message.i);
    }
    public static TeleportKeyPressedMessage decode(PacketBuffer buffer)
    {
        return new TeleportKeyPressedMessage(buffer.readInt());
    }
    public static void handle(TeleportKeyPressedMessage message, Supplier<NetworkEvent.Context> contextFactory)
    {
        NetworkEvent.Context context = contextFactory.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            Entity veh = player.getVehicle();
            if (veh instanceof MahalmulaShipEntity) {
                ServerWorld destWorld = TeleportationModule.getWorldByStack(player.getOffhandItem(), player.server);
                if (destWorld == null || destWorld.equals((ServerWorld) player.level)) return;
                Vector3d posit = MF2DimensionHelper.scaleByWorld(veh.position(), player.level.dimensionType(), destWorld.dimensionType());
                posit = posit.multiply(1, 0, 1).add(0, MF2DimensionHelper.safeHigh(posit, destWorld), 0);
                MF2Teleporter teleporter = new MF2Teleporter(posit);
                EntityDataManager entityData = veh.getEntityData();
                entityData.set(MahalmulaShipEntity.EFFECTS_PARAMETER, (entityData.get(MahalmulaShipEntity.EFFECTS_PARAMETER) == 7)? 8 : 7);
                player.changeDimension(destWorld, teleporter).startRiding(veh.changeDimension(destWorld, teleporter));
                //veh.getEntityData().set(MahalmulaShipEntity.FALLING_PARAMETER, true);
            }
        });
        context.setPacketHandled(true);
    }
}
