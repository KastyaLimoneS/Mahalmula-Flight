package net.kastya_limoness.mahalmula_flight2.teleportation;

import com.google.common.base.Function;
import com.mojang.datafixers.types.Func;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Optional;

public class MF2DimensionHelper {
    @Nullable
    public static ServerWorld getWorldByName(String name, MinecraftServer server)
    {
        for (ServerWorld world : server.getAllLevels())
            if (world.dimension().location().toString().equals(name)) return world;
        return null;
    }

    public static Vector3d scaleByWorld(Vector3d pos, DimensionType current, DimensionType dest)
        {return pos.scale(current.coordinateScale() / dest.coordinateScale()).multiply(1, 0, 1).add(pos.multiply(0, 1, 0));}

    public static Integer safeHigh(Vector3d pos, ServerWorld world)
        {return find(new BlockPos(pos), world, BlockPos::above, 64).orElse(-1);}

    private static Optional<Integer> find(BlockPos pos, ServerWorld world, Function<BlockPos, BlockPos> func, int limit)
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
