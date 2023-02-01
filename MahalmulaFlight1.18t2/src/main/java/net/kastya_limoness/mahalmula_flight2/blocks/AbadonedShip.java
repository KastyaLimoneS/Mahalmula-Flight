package net.kastya_limoness.mahalmula_flight2.blocks;

import net.kastya_limoness.mahalmula_flight2.utils.MF2GameModeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

public class AbadonedShip extends Block {
    public static final IntegerProperty EYES = IntegerProperty.create("eyes", 0, 7);
    public AbadonedShip() {
        super(BlockBehaviour.Properties.of(Material.METAL).strength(-1).noOcclusion().hasPostProcess((a, b, c) -> true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(EYES);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return level.isClientSide? useClient(state, level, pos, player, hand, hitResult) : useServer(state, level, pos, player, hand, hitResult);
    }
    private InteractionResult useClient(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        if (!player.getMainHandItem().is(Items.ENDER_EYE)) return InteractionResult.PASS;
        level.playSound(player, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1, level.random.nextFloat());
        return InteractionResult.SUCCESS;
    }

    private InteractionResult useServer(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(Items.ENDER_EYE)) return InteractionResult.PASS;
        int eyes = state.getValue(EYES);
        if (eyes < 7)
            state.setValue(EYES, eyes+1);
        summonShip(level, pos);
        if (MF2GameModeHelper.getGameMode(player, level.getServer()) != GameType.CREATIVE) stack.shrink(1);
        return InteractionResult.SUCCESS;
    }

    private void summonShip(Level level, BlockPos pos)
    {}
}
