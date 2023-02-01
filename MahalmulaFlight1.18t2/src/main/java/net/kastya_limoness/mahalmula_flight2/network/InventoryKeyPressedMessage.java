package net.kastya_limoness.mahalmula_flight2.network;

import net.kastya_limoness.mahalmula_flight2.entities.MahalmulaShipEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class InventoryKeyPressedMessage {
    public int i;
    public InventoryKeyPressedMessage(int i)
    {
        this.i = i;
    }
    public static void encode(InventoryKeyPressedMessage message, FriendlyByteBuf buffer)
    {
        buffer.writeInt(message.i);
    }
    public static InventoryKeyPressedMessage decode(FriendlyByteBuf buffer)
    {
        return new InventoryKeyPressedMessage(buffer.readInt());
    }
    public static void handle(InventoryKeyPressedMessage message, Supplier<NetworkEvent.Context> contextFactory)
    {
        NetworkEvent.Context context = contextFactory.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (!(player.getVehicle() instanceof MahalmulaShipEntity)) return;
            PlayerEnderChestContainer enderchestinventory = player.getEnderChestInventory();
            player.openMenu(new SimpleMenuProvider((p_226928_1_, p_226928_2_, p_226928_3_) -> {
                return ChestMenu.threeRows(p_226928_1_, p_226928_2_, enderchestinventory);}, new TranslatableComponent("container.enderchest")));
        });
        context.setPacketHandled(true);
    }
}
