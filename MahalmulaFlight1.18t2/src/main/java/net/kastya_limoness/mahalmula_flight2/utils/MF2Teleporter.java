package net.kastya_limoness.mahalmula_flight2.utils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

public class MF2Teleporter implements ITeleporter {
    private Vec3 dPos;
    public MF2Teleporter(Vec3 pos)
    {
        dPos = pos;
    }
    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        entity = repositionEntity.apply(false);
        entity.teleportToWithTicket(dPos.x, dPos.y, dPos.z);
        if (entity instanceof ServerPlayer)
            ((ServerPlayer)entity).teleportTo(destWorld, dPos.x, dPos.y, dPos.z, yaw, 0f);
        return entity;
    }
}
