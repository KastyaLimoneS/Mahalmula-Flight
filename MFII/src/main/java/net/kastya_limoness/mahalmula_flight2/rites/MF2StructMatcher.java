package net.kastya_limoness.mahalmula_flight2.rites;

import com.mojang.datafixers.util.Function3;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

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

    public static void doWithPattern(World world, BlockPos center, Map<BlockPos, Block> patter, Function3<World, BlockPos, Block, ?> whatToDo)
        {
            patter.forEach((key, value) -> whatToDo.apply(world, center.offset(key), value));}
    public static boolean match(World world, BlockPos center, Map<BlockPos, Block> pattern)
    {
        return pattern.entrySet().stream().allMatch(entry -> world.getBlockState(center.offset(entry.getKey())).getBlock().equals(entry.getValue()));
    }
}
