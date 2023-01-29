package net.kastya_limoness.mahalmula_flight2.network;

import net.kastya_limoness.mahalmula_flight2.entities.MahalmulaShipEntity;
import net.kastya_limoness.mahalmula_flight2.items.TeleportationModule;
import net.kastya_limoness.mahalmula_flight2.teleportation.MF2DimensionHelper;
import net.kastya_limoness.mahalmula_flight2.teleportation.MF2Teleporter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class InventoryKeyPressedMessage {
    public int i;
    public InventoryKeyPressedMessage(int i)
    {
        this.i = i;
    }
    public static void encode(InventoryKeyPressedMessage message, PacketBuffer buffer)
    {
        buffer.writeInt(message.i);
    }
    public static InventoryKeyPressedMessage decode(PacketBuffer buffer)
    {
        return new InventoryKeyPressedMessage(buffer.readInt());
    }
    public static void handle(InventoryKeyPressedMessage message, Supplier<NetworkEvent.Context> contextFactory)
    {
        NetworkEvent.Context context = contextFactory.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (!(player.getVehicle() instanceof MahalmulaShipEntity)) return;
            EnderChestInventory enderchestinventory = player.getEnderChestInventory();
            player.openMenu(new SimpleNamedContainerProvider((p_226928_1_, p_226928_2_, p_226928_3_) -> {
                return ChestContainer.threeRows(p_226928_1_, p_226928_2_, enderchestinventory);}, new TranslationTextComponent("container.enderchest")));
        });
        context.setPacketHandled(true);
    }
}
