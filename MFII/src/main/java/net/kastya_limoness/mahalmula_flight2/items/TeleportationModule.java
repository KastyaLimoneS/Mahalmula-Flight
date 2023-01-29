package net.kastya_limoness.mahalmula_flight2.items;

import net.kastya_limoness.mahalmula_flight2.MahalmulaFlightII;
import net.kastya_limoness.mahalmula_flight2.teleportation.MF2DimensionHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public class TeleportationModule extends Item {
    public TeleportationModule() {
        super(new Properties().stacksTo(1).tab(ItemGroup.TAB_TOOLS));
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        ItemStack stack = context.getItemInHand();
        if (context.getLevel().isClientSide) return ActionResultType.PASS;
        if (stack.getOrCreateTag().getBoolean("stored")) return ActionResultType.PASS;
        stack.getOrCreateTag().putString("dimension", context.getLevel().dimension().location().toString());
        stack.getOrCreateTag().putBoolean("stored", true);
        return ActionResultType.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> builder, ITooltipFlag flag) {
        if (stack.getOrCreateTag().getBoolean("stored"))
        {
            builder.add(new TranslationTextComponent(MahalmulaFlightII.MODID + ".tooltip.dimension"));
            builder.add(new TranslationTextComponent("dim_" + stack.getOrCreateTag().getString("dimension")));
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("stored");
    }

    public static ServerWorld getWorldByStack(ItemStack stack, MinecraftServer server)
    {
        if (!stack.getOrCreateTag().getBoolean("stored")) return null;
        return MF2DimensionHelper.getWorldByName(stack.getOrCreateTag().getString("dimension"), server);
    }
}
