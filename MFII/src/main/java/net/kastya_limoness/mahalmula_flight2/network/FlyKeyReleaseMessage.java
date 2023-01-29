package net.kastya_limoness.mahalmula_flight2.network;

import net.kastya_limoness.mahalmula_flight2.entities.MahalmulaShipEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class FlyKeyReleaseMessage {
    public int i;
    public FlyKeyReleaseMessage(int i)
    {
        this.i = i;
    }
    public static void encode(FlyKeyReleaseMessage message, PacketBuffer buffer)
    {
        buffer.writeInt(message.i);
    }
    public static FlyKeyReleaseMessage decode(PacketBuffer buffer)
    {
        return new FlyKeyReleaseMessage(buffer.readInt());
    }
    public static void handle(FlyKeyReleaseMessage message, Supplier<NetworkEvent.Context> contextFactory)
    {
        NetworkEvent.Context context = contextFactory.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            Entity veh = player.getVehicle();
            if (veh instanceof MahalmulaShipEntity)
                veh.getEntityData().set(MahalmulaShipEntity.YDIR_PARAMETER, 0);
        });
        context.setPacketHandled(true);
    }
}
