package net.kastya_limoness.mahalmula_flight2.items;

import net.kastya_limoness.mahalmula_flight2.MahalmulaFlightII;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class MF2Items {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MahalmulaFlightII.MODID);

    public static final RegistryObject<Item> SHIP_MODEL = create("ship_model", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TELEPORTATION_MODULE = create("teleportation_module", TeleportationModule::new);

    public static final RegistryObject<Item> COLORFUL_SHELL = create("colorful_shell", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));

    public static RegistryObject<Item> create(String name, Supplier<Item> factory)
    {
        return ITEMS.register(name, factory);
    }

    public static void register(IEventBus bus)
    {
        ITEMS.register(bus);
    }

}
