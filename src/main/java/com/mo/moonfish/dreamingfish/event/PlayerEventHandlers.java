package com.mo.moonfish.dreamingfish.event;

import com.mo.moonfish.dreamingfish.scoreboard.ScoreboardManager;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerEventHandlers {

    public static void registerEvents() {
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            ScoreboardManager.initializeScoreboard(newPlayer);
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                ScoreboardManager.updateScoreboard(player);
            }
        });
    }
}
