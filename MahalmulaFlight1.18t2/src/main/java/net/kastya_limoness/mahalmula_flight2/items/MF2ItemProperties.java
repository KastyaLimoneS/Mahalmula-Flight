package net.kastya_limoness.mahalmula_flight2.items;

import net.kastya_limoness.mahalmula_flight2.MahalmulaFlightII;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class MF2ItemProperties {
    public static void register(final FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
            ItemProperties.register(MF2Items.SHIP_MODEL.get(), MahalmulaFlightII.getLocation("skin"), (stack, world, entity, a) -> stack.getOrCreateTag().getInt("skin"));
        });
    }
}
