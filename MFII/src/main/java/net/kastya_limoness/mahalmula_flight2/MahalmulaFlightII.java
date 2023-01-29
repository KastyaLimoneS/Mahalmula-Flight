package net.kastya_limoness.mahalmula_flight2;

import net.kastya_limoness.mahalmula_flight2.blocks.MF2Blocks;
import net.kastya_limoness.mahalmula_flight2.entities.MF2EntityTypes;
import net.kastya_limoness.mahalmula_flight2.entities.MahalmulaShipRenderer;
import net.kastya_limoness.mahalmula_flight2.items.MF2CustomProperties;
import net.kastya_limoness.mahalmula_flight2.items.MF2Items;
import net.kastya_limoness.mahalmula_flight2.keybinds.MF2KeyBinds;
import net.kastya_limoness.mahalmula_flight2.network.MF2Network;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeRenderTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MahalmulaFlightII.MODID)
public class MahalmulaFlightII
{
    // Directly reference a log4j logger.
    public static final String MODID = "mahalmula_flight2";
    public static final Logger LOGGER = LogManager.getLogger();

    public MahalmulaFlightII() {
        // Register the setup method for modloading
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        bus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        bus.addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        bus.addListener(this::doClientStuff);

        MF2EntityTypes.register(bus);
        MF2Items.register(bus);
        MF2Blocks.register(bus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation getLocation(String name) {return new ResourceLocation(MODID, name);}

    private void setup(final FMLCommonSetupEvent event)
    {
        MF2Network.init();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
        MF2CustomProperties.register(event);
        MF2KeyBinds.register(event);
        RenderingRegistry.registerEntityRenderingHandler(MF2EntityTypes.MAHALMULA_SHIP_TYPE.get(), MahalmulaShipRenderer::new);
        RenderTypeLookup.setRenderLayer(MF2Blocks.ABADONED_SHIP.get(), RenderType.cutout());
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
