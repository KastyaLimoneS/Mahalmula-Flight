package net.kastya_limoness.mahalmula_flight2.events;

import net.kastya_limoness.mahalmula_flight2.MahalmulaFlightII;
import net.kastya_limoness.mahalmula_flight2.network.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MahalmulaFlightII.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class MF2Input {
    private static final KeyMapping[] upArray = {MF2KeyBinds.flyUpKey};
    private static final KeyMapping[] downArray = {MF2KeyBinds.flyDownKey};
    private static final KeyMapping[] teleportArray = {MF2KeyBinds.teleportKey};
    private static final KeyMapping[] inventoryArray = {MF2KeyBinds.inventoryKey};
    private static final KeyMapping[] bothArray = {MF2KeyBinds.flyUpKey, MF2KeyBinds.flyDownKey};
    @SubscribeEvent
    public static void onInputEvent(InputEvent.KeyInputEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        if (mc.screen != null) return;
        onInput(mc, event.getKey(), event.getAction());
    }

    public static void onInput(Minecraft mc, int key, int action)
    {
        if (MF2KeyBinds.isSomePressedRight(key, action, upArray, 1))
        {
            MF2Network.CHANNEL.sendToServer(new UpKeyPressedMessage(0));
            return;
        }
        if (MF2KeyBinds.isSomePressedRight(key, action, downArray, 1))
        {
            MF2Network.CHANNEL.sendToServer(new DownKeyPressedMessage(1));
            return;
        }
        if (MF2KeyBinds.isSomePressedRight(key, action, bothArray, 0))
        {
            MF2Network.CHANNEL.sendToServer(new FlyKeyReleaseMessage(2));
            return;
        }
        if (MF2KeyBinds.isSomePressedRight(key, action, teleportArray, 1))
        {
            MF2Network.CHANNEL.sendToServer(new TeleportKeyPressedMessage(3));
            return;
        }
        if (MF2KeyBinds.isSomePressedRight(key, action, inventoryArray, 1))
        {
            MF2Network.CHANNEL.sendToServer(new InventoryKeyPressedMessage(4));
            return;
        }
    }
}
