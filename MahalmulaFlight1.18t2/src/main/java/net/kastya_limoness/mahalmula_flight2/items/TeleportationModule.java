package net.kastya_limoness.mahalmula_flight2.items;

import net.kastya_limoness.mahalmula_flight2.MahalmulaFlightII;
import net.kastya_limoness.mahalmula_flight2.utils.MF2DimensionHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeleportationModule extends SimpleFoiledItem {
    public TeleportationModule() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        if (context.getLevel().isClientSide) return InteractionResult.PASS;
        if (stack.getOrCreateTag().getBoolean("stored")) return InteractionResult.PASS;
        stack.getOrCreateTag().putString("dimension", context.getLevel().dimension().location().toString());
        stack.getOrCreateTag().putBoolean("stored", true);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> builder, TooltipFlag flag) {
        if (stack.getOrCreateTag().getBoolean("stored"))
        {
            builder.add(new TranslatableComponent(MahalmulaFlightII.MODID + ".tooltip.dimension"));
            builder.add(new TranslatableComponent("dim_" + stack.getOrCreateTag().getString("dimension")));
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("stored");
    }

    @Nullable
    public static ServerLevel getWorldByStack(ItemStack stack, MinecraftServer server)
    {
        if (!stack.getOrCreateTag().getBoolean("stored")) return null;
        return MF2DimensionHelper.getWorldByName(stack.getOrCreateTag().getString("dimension"), server);
    }
}
