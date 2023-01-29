package net.kastya_limoness.mahalmula_flight2.items;

import net.kastya_limoness.mahalmula_flight2.MahalmulaFlightII;
import net.minecraft.item.ItemModelsProperties;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class MF2CustomProperties {
    public static void register(final FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
            ItemModelsProperties.register(MF2Items.SHIP_MODEL.get(), MahalmulaFlightII.getLocation("skin"), (stack, world, entity) -> stack.getOrCreateTag().getInt("skin"));
        });
    }
}
