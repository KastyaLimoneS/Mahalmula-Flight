package net.kastya_limoness.mahalmula_flight2.blocks;

import net.kastya_limoness.mahalmula_flight2.MahalmulaFlightII;
import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MF2Blocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MahalmulaFlightII.MODID);
    public static final RegistryObject<Block> ABADONED_SHIP = BLOCKS.register("abadoned_ship", AbadonedBlock::new);
    public static void register(IEventBus bus)
    {
        BLOCKS.register(bus);
    }
}
