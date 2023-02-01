package net.kastya_limoness.mahalmula_flight2.events;

import net.kastya_limoness.mahalmula_flight2.MahalmulaFlightII;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.awt.event.KeyEvent;
import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class MF2KeyBinds {
    public static KeyMapping flyUpKey = create("fly_up_key", KeyEvent.VK_SPACE);
    public static KeyMapping flyDownKey = create("fly_down_key", 	342);

    public static KeyMapping teleportKey = create("teleport_key", KeyEvent.VK_Z);
    public static KeyMapping inventoryKey = create("inventory_key", KeyEvent.VK_R);

    public static KeyMapping create(String name, int key)
    {
        return new KeyMapping("key." + MahalmulaFlightII.MODID + "." + name, key, "key.category." + MahalmulaFlightII.MODID);
    }

    public static boolean isSomePressedRight(int key, int action, KeyMapping[] acceptableBindings, int acceptableAction)
    {
        if (action != acceptableAction) return false;
        return Arrays.stream(acceptableBindings).anyMatch(b -> b.getKey().getValue() == key);
    }
    public static void register(final FMLClientSetupEvent event)
    {
        ClientRegistry.registerKeyBinding(flyUpKey);
        ClientRegistry.registerKeyBinding(flyDownKey);
        ClientRegistry.registerKeyBinding(teleportKey);
        ClientRegistry.registerKeyBinding(inventoryKey);
    }
}
