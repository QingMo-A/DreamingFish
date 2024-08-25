package com.mo.moonfish.dreamingfish.command.commands.economy;

import com.mo.moonfish.dreamingfish.economy.EconomyManager;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.UUID;

public class WelfareCommand {

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("redpack")
                    .then(CommandManager.argument("type", StringArgumentType.string())
                            .then(CommandManager.argument("count", IntegerArgumentType.integer(1))
                                    .then(CommandManager.argument("amount", DoubleArgumentType.doubleArg())
                                            .executes(context -> handleWelfareCommand(
                                                    context.getSource(),
                                                    StringArgumentType.getString(context, "type"),
                                                    IntegerArgumentType.getInteger(context, "count"),
                                                    DoubleArgumentType.getDouble(context, "amount")
                                            ))))));

            dispatcher.register(CommandManager.literal("claim")
                    .then(CommandManager.argument("welfareId", StringArgumentType.string())
                            .executes(context -> handleClaimCommand(
                                    context.getSource(),
                                    StringArgumentType.getString(context, "welfareId")
                            ))));
        });
    }

    public static int handleWelfareCommand(ServerCommandSource source, String type, int count, double amount) {
        ServerPlayerEntity player = source.getPlayer();
        EconomyManager economyManager = EconomyManager.get(source.getServer());

        boolean isLucky = type.equalsIgnoreCase("lucky");

        UUID welfareId = economyManager.createWelfare(player, amount, count, isLucky);

        if (welfareId != null) {
            // 创建红包成功，向所有在线玩家发送消息
            sendWelfareAnnouncement(source, player.getName().getString(), welfareId);
            return 1;
        } else {
            player.sendMessage(Text.translatable("economy.red_packet_creation_failed").formatted(Formatting.RED), false);
            return 0;
        }
    }

    public static int handleClaimCommand(ServerCommandSource source, String welfareIdStr) {
        ServerPlayerEntity player = source.getPlayer();
        EconomyManager economyManager = EconomyManager.get(source.getServer());

        UUID welfareId = UUID.fromString(welfareIdStr);
        double amount = economyManager.claimWelfare(welfareId, player);

        if (amount > 0) {
            // 创建领取成功的消息
            Text successMessage = Text.translatable("economy.player_red_packet_claimed_success", player.getName().getString(), String.format("%.2f", amount)).formatted(Formatting.GREEN);

            // 为玩家增加余额
            economyManager.addBalance(player, amount);

            // 将消息发送给所有在线玩家
            for (ServerPlayerEntity onlinePlayer : source.getServer().getPlayerManager().getPlayerList()) {
                onlinePlayer.sendMessage(successMessage, false);
            }
            return 1;
        } else {
            player.sendMessage(Text.translatable("economy.claim_failure").formatted(Formatting.RED), false);
            return 0;
        }
    }


    private static void sendWelfareAnnouncement(ServerCommandSource source, String playerName, UUID welfareId) {
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
