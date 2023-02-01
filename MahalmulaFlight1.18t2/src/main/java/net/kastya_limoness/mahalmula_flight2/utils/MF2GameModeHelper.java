package net.kastya_limoness.mahalmula_flight2.utils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

public class MF2GameModeHelper {
    public static GameType getGameMode(Player player, MinecraftServer server)
    {
        return server.getPlayerList().getPlayer(player.getUUID()).gameMode.getGameModeForPlayer();
    }
}
