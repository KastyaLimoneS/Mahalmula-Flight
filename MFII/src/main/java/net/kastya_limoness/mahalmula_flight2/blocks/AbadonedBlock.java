package net.kastya_limoness.mahalmula_flight2.blocks;

import net.kastya_limoness.mahalmula_flight2.entities.MF2EntityTypes;
import net.kastya_limoness.mahalmula_flight2.teleportation.MF2GameModeHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class AbadonedBlock extends Block {
    public static IntegerProperty EYES = IntegerProperty.create("eyes", 0, 7);
    public AbadonedBlock() {
        super(AbstractBlock.Properties.of(Material.METAL).strength(-1).noOcclusion().hasPostProcess((p_test_1_, p_test_2_, p_test_3_) -> false));
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        /*if (state.getValue(EYES) < 7)
            if (player.getMainHandItem().getItem().equals(Items.ENDER_EYE))
            {
                if (world.isClientSide) world.playSound(player, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1, world.random.nextFloat());
                if (!world.isClientSide) {world.setBlockAndUpdate(pos, state.setValue(EYES, state.getValue(EYES) + 1)); if (MF2GameModeHelper.getGameMode(player, world.getServer()) != GameType.CREATIVE) player.getMainHandItem().shrink(1);}
            }
        else {world.removeBlock(pos, false); MF2EntityTypes.MAHALMULA_SHIP_TYPE.get().spawn((ServerWorld) world, null, null, pos, SpawnReason.TRIGGERED, false, false);}
        */
        if (player.getMainHandItem().getItem().equals(Items.ENDER_EYE))
        {
            if (state.getValue(EYES) < 7)
            {
                if (!world.isClientSide) world.setBlockAndUpdate(pos, state.setValue(EYES, state.getValue(EYES) + 1));
            }
            else
            {
                if (!world.isClientSide) {world.removeBlock(pos, false); MF2EntityTypes.MAHALMULA_SHIP_TYPE.get().spawn((ServerWorld) world, null, null, pos, SpawnReason.TRIGGERED, false, false);}
            }
            if (!world.isClientSide) if (MF2GameModeHelper.getGameMode(player, world.getServer()) != GameType.CREATIVE) player.getMainHandItem().shrink(1);
            if (world.isClientSide) world.playSound(player, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1, world.random.nextFloat());
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(EYES);
    }
}
