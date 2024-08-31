package com.mo.moonfish.dreamingfish.economy.message;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.UUID;

public class RedPackMessage {
    public static void sendWelfareAnnouncement(ServerCommandSource source, String playerName, UUID welfareId) {
        MutableText message = Text.translatable("economy.player_sends_red_packet_announcement", playerName, welfareId.toString()).formatted(Formatting.GOLD)
                .append(" ")
                .append(Text.translatable("economy.claim").formatted(Formatting.RED).styled(style ->
                        style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/claim " + welfareId))
                ));

        // 向所有在线玩家发送消息
        for (ServerPlayerEntity onlinePlayer : source.getServer().getPlayerManager().getPlayerList()) {
            onlinePlayer.sendMessage(message, false);
        }
    }
}
