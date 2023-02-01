package net.kastya_limoness.mahalmula_flight2.network;

import net.kastya_limoness.mahalmula_flight2.entities.MahalmulaShipEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FlyKeyReleaseMessage {
    public int i;
    public FlyKeyReleaseMessage(int i)
    {
        this.i = i;
    }
    public static void encode(FlyKeyReleaseMessage message, FriendlyByteBuf buffer)
    {
        buffer.writeInt(message.i);
    }
    public static FlyKeyReleaseMessage decode(FriendlyByteBuf buffer)
    {
        return new FlyKeyReleaseMessage(buffer.readInt());
    }
    public static void handle(FlyKeyReleaseMessage message, Supplier<NetworkEvent.Context> contextFactory)
    {
        NetworkEvent.Context context = contextFactory.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            Entity veh = player.getVehicle();
            if (veh instanceof MahalmulaShipEntity)
                veh.getEntityData().set(MahalmulaShipEntity.YDIR_PARAMETER, 0);
        });
        context.setPacketHandled(true);
    }
}
