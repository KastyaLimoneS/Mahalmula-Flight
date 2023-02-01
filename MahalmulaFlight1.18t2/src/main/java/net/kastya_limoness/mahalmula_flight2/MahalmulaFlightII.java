package net.kastya_limoness.mahalmula_flight2;

import com.mojang.logging.LogUtils;
import net.kastya_limoness.mahalmula_flight2.blocks.MF2Blocks;
import net.kastya_limoness.mahalmula_flight2.entities.MF2EntityTypes;
import net.kastya_limoness.mahalmula_flight2.entities.MahalmulaShipRenderer;
import net.kastya_limoness.mahalmula_flight2.events.MF2KeyBinds;
import net.kastya_limoness.mahalmula_flight2.items.MF2ItemProperties;
import net.kastya_limoness.mahalmula_flight2.items.MF2Items;
import net.kastya_limoness.mahalmula_flight2.network.MF2Network;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MahalmulaFlightII.MODID)
public class MahalmulaFlightII
{
    public static final String MODID = "mahalmula_flight2";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MahalmulaFlightII()
    {
        // Register the setup method for modloading
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::registerRenderers);
        // Register the enqueueIMC method for modloading
        bus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        bus.addListener(this::processIMC);

        MF2Blocks.register(bus);
        MF2Items.register(bus);
        MF2EntityTypes.register(bus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation getLocation(String name)
    {
        return new ResourceLocation(MODID, name);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        MF2Network.init();
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
        MF2ItemProperties.register(event);
        MF2KeyBinds.register(event);
        ItemBlockRenderTypes.setRenderLayer(MF2Blocks.ABADONED_SHIP.get(), RenderType.cutout());

    }

    public void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MF2EntityTypes.MAHALMULA_SHIP_TYPE.get(), MahalmulaShipRenderer::new);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // Some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // Some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.messageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {
            // Register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
