package net.kastya_limoness.mahalmula_flight2.items;

import net.kastya_limoness.mahalmula_flight2.MahalmulaFlightII;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class MF2Items {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MahalmulaFlightII.MODID);

    public static final RegistryObject<Item> SHIP_MODEL = ITEMS.register("ship_model", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COLORFUL_SHELL = ITEMS.register("colorful_shell", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> TELEPORTATION_MODULE = ITEMS.register("teleportation_module", TeleportationModule::new);

    public static void register(IEventBus bus)
    {
        ITEMS.register(bus);
    }
}
