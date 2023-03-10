package net.kastya_limoness.mahalmula_flight2.entities;

import net.kastya_limoness.mahalmula_flight2.MahalmulaFlightII;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MF2EntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, MahalmulaFlightII.MODID);

    public static final RegistryObject<EntityType<MahalmulaShipEntity>> MAHALMULA_SHIP_TYPE
            = ENTITY_TYPES.register("mahalmula_ship",
            () -> EntityType.Builder.of(MahalmulaShipEntity::new, EntityClassification.MISC)
                    .sized(3f, 1f).build(MahalmulaFlightII.getLocation("mahalmula_ship").toString())
    );

    public static void register(IEventBus bus)
    {
        ENTITY_TYPES.register(bus);
    }
}
