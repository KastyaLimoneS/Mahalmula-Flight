package net.kastya_limoness.mahalmula_flight2.network;

import net.kastya_limoness.mahalmula_flight2.entities.MahalmulaShipEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpKeyPressedMessage {
    public int i;
    public UpKeyPressedMessage(int i)
    {
        this.i = i;
    }
    public static void encode(UpKeyPressedMessage message, FriendlyByteBuf buffer)
    {
        buffer.writeInt(message.i);
    }
    public static UpKeyPressedMessage decode(FriendlyByteBuf buffer)
    {
        return new UpKeyPressedMessage(buffer.readInt());
    }
    public static void handle(UpKeyPressedMessage message, Supplier<NetworkEvent.Context> contextFactory)
    {
        NetworkEvent.Context context = contextFactory.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            Entity veh = player.getVehicle();
            if (veh instanceof MahalmulaShipEntity)
                veh.getEntityData().set(MahalmulaShipEntity.YDIR_PARAMETER, 1);
        });
        context.setPacketHandled(true);
    }
}
