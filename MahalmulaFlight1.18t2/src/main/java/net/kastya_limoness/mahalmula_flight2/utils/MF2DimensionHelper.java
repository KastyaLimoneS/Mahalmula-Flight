package net.kastya_limoness.mahalmula_flight2.utils;

import com.mojang.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.function.Function;

public class MF2DimensionHelper {
    public static ServerLevel getWorldByName(String name, MinecraftServer server)
    {
        for (ServerLevel level : server.getAllLevels())
            if (level.dimension().location().toString().equals(name)) return level;
        return null;
    }

    public static Vec3 scaleByWorld(Vec3 pos, DimensionType current, DimensionType dest)
    {return pos.scale(current.coordinateScale() / dest.coordinateScale()).multiply(1, 0, 1).add(pos.multiply(0, 1, 0));}

    public static Integer safeHigh(Vec3 pos, ServerLevel world)
    {return find(new BlockPos(pos), world, BlockPos::above, 64).orElse(-1);}

    private static Optional<Integer> find(BlockPos pos, ServerLevel world, Function<BlockPos, BlockPos> func, int limit)
    {
        BlockPos mPos = pos;
        int result = -1;
        for (int i = 0; i < limit; i ++)
        {
            if (world.getBlockState(mPos).is(Blocks.AIR)) {result = mPos.getY(); break;}
            mPos = mPos.above();
        }
        return (result == -1)? Optional.empty() : Optional.of(result);
    }
}
