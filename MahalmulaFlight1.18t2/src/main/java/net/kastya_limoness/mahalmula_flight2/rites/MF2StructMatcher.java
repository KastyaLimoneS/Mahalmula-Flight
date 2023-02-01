package net.kastya_limoness.mahalmula_flight2.rites;

import com.mojang.datafixers.util.Function3;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;

public class MF2StructMatcher {
    public static Map<BlockPos, Block> SHIP_STRUCT = new HashMap<BlockPos, Block>() {
        {
            put(new BlockPos(-1, 0, 0), Blocks.PURPUR_BLOCK);
            put(new BlockPos(0, 0, -1), Blocks.PURPUR_BLOCK);
            put(new BlockPos(1, 0, 0), Blocks.PURPUR_BLOCK);
            put(new BlockPos(0, 0, 1), Blocks.PURPUR_BLOCK);
            put(new BlockPos(-1, 0, -1), Blocks.PURPUR_BLOCK);
            put(new BlockPos(1, 0, -1), Blocks.PURPUR_BLOCK);
            put(new BlockPos(-1, 0, 1), Blocks.PURPUR_BLOCK);
            put(new BlockPos(1, 0, 1), Blocks.PURPUR_BLOCK);
            put(new BlockPos(0, 0, 0), Blocks.BEACON);
        }
    };

    public static void doWithPattern(Level world, BlockPos center, Map<BlockPos, Block> patter, Function3<Level, BlockPos, Block, ?> whatToDo)
    {
        patter.forEach((key, value) -> whatToDo.apply(world, center.offset(key), value));}
    public static boolean match(Level world, BlockPos center, Map<BlockPos, Block> pattern)
    {
        return pattern.entrySet().stream().allMatch(entry -> world.getBlockState(center.offset(entry.getKey())).getBlock().equals(entry.getValue()));
    }
}
