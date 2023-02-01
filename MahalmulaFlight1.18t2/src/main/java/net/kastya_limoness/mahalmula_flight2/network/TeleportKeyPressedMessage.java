package net.kastya_limoness.mahalmula_flight2.network;

import com.mojang.math.Vector3d;
import net.kastya_limoness.mahalmula_flight2.entities.MahalmulaShipEntity;
import net.kastya_limoness.mahalmula_flight2.items.TeleportationModule;
import net.kastya_limoness.mahalmula_flight2.utils.MF2DimensionHelper;
import net.kastya_limoness.mahalmula_flight2.utils.MF2Teleporter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TeleportKeyPressedMessage {
    public int i;
    public TeleportKeyPressedMessage(int i)
    {
        this.i = i;
    }
    public static void encode(TeleportKeyPressedMessage message, FriendlyByteBuf buffer)
    {
        buffer.writeInt(message.i);
    }
    public static TeleportKeyPressedMessage decode(FriendlyByteBuf buffer)
    {
        return new TeleportKeyPressedMessage(buffer.readInt());
    }
    public static void handle(TeleportKeyPressedMessage message, Supplier<NetworkEvent.Context> contextFactory)
    {
        NetworkEvent.Context context = contextFactory.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            Entity veh = player.getVehicle();
            if (veh instanceof MahalmulaShipEntity) {
                ServerLevel destWorld = TeleportationModule.getWorldByStack(player.getOffhandItem(), player.server);
                if (destWorld == null || destWorld.equals((ServerLevel) player.level)) return;
                Vec3 posit = MF2DimensionHelper.scaleByWorld(veh.position(), player.level.dimensionType(), destWorld.dimensionType());
                posit = posit.multiply(1, 0, 1).add(0, MF2DimensionHelper.safeHigh(posit, destWorld), 0);
                MF2Teleporter teleporter = new MF2Teleporter(posit);
                SynchedEntityData entityData = veh.getEntityData();
                entityData.set(MahalmulaShipEntity.EFFECTS_PARAMETER, (entityData.get(MahalmulaShipEntity.EFFECTS_PARAMETER) == 7)? 8 : 7);
                player.changeDimension(destWorld, teleporter).startRiding(veh.changeDimension(destWorld, teleporter));
                //veh.getEntityData().set(MahalmulaShipEntity.FALLING_PARAMETER, true);
            }
        });
        context.setPacketHandled(true);
    }
}
