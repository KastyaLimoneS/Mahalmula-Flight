package net.kastya_limoness.mahalmula_flight2.teleportation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

public class MF2Teleporter implements ITeleporter {
    private Vector3d dPos;
    public MF2Teleporter(Vector3d pos)
    {
        dPos = pos;
    }
    @Override
    public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        entity = repositionEntity.apply(false);
        entity.teleportToWithTicket(dPos.x, dPos.y, dPos.z);
        if (entity instanceof ServerPlayerEntity)
            ((ServerPlayerEntity)entity).teleportTo(destWorld, dPos.x, dPos.y, dPos.z, yaw, 0f);
        return entity;
    }
}
