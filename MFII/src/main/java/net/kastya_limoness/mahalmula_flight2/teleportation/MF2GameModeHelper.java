package net.kastya_limoness.mahalmula_flight2.teleportation;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameType;

public class MF2GameModeHelper {
    public static GameType getGameMode(PlayerEntity player, MinecraftServer server)
    {
        return server.getPlayerList().getPlayer(player.getUUID()).gameMode.getGameModeForPlayer();
    }
}
