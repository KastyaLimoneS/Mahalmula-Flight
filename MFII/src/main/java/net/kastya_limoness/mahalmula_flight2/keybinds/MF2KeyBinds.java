package net.kastya_limoness.mahalmula_flight2.keybinds;

import net.kastya_limoness.mahalmula_flight2.MahalmulaFlightII;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.swing.text.JTextComponent;
import java.awt.event.KeyEvent;
import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class MF2KeyBinds {
    public static KeyBinding flyUpKey = create("fly_up_key", KeyEvent.VK_SPACE);
    public static KeyBinding flyDownKey = create("fly_down_key", 	342);

    public static KeyBinding teleportKey = create("teleport_key", KeyEvent.VK_Z);
    public static KeyBinding inventoryKey = create("inventory_key", KeyEvent.VK_R);

    public static KeyBinding create(String name, int key)
    {
        return new KeyBinding("key." + MahalmulaFlightII.MODID + "." + name, key, "key.category." + MahalmulaFlightII.MODID);
    }

    public static boolean isSomePressedRight(int key, int action, KeyBinding[] acceptableBindings, int acceptableAction)
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
