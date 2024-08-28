package com.mo.moonfish.dreamingfish.command.commands.economy;

import com.mo.moonfish.dreamingfish.economy.BankManager;
import com.mo.moonfish.dreamingfish.economy.EconomyManager;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static com.mojang.brigadier.arguments.DoubleArgumentType.doubleArg;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.command.argument.EntityArgumentType.getPlayer;
import static net.minecraft.command.argument.EntityArgumentType.player;

public class EconomyCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        // 查看余额
        dispatcher.register(literal("balance")
                .executes(ctx -> {
                    double balance = EconomyManager.get(ctx.getSource().getServer()).getBalance(ctx.getSource().getPlayer());
                    ctx.getSource().sendFeedback(() -> Text.translatable("player_balance", String.format("%.2f", balance)), false);
                    return 1;
                })
        );

        // 查看银行存款余额
        dispatcher.register(literal("bankbalance")
                .executes(ctx -> {
                    double bankBalance = BankManager.get(ctx.getSource().getServer()).getBankBalance(ctx.getSource().getPlayer());
                    ctx.getSource().sendFeedback(() -> Text.translatable("player_bank_balance", String.format("%.2f", bankBalance)), false);
                    return 1;
                })
        );

        // 存钱到银行
        dispatcher.register(literal("deposit")
                .then(argument("amount", doubleArg(0.01))
                        .executes(ctx -> {
                            ServerPlayerEntity player = ctx.getSource().getPlayer();
                            double amount = ctx.getArgument("amount", Double.class);

                            EconomyManager economyManager = EconomyManager.get(ctx.getSource().getServer());
                            BankManager bankManager = BankManager.get(ctx.getSource().getServer());

                            // 检查玩家的余额是否足够
                            if (economyManager.getBalance(player) < amount) {
                                ctx.getSource().sendFeedback(() -> Text.translatable("insufficient_balance"), false);
                                return 0;
                            }

                            // 执行存款
                            economyManager.removeBalance(player, amount);
                            bankManager.deposit(player, amount);

                            // 反馈给玩家
                            player.sendMessage(Text.translatable("deposit_success", String.format("%.2f", amount), bankManager.getBankBalance(player)), false);
                            return 1;
                        }))
        );

        // 从银行取钱
        dispatcher.register(literal("withdraw")
                .then(argument("amount", doubleArg(0.01))
                        .executes(ctx -> {
                            ServerPlayerEntity player = ctx.getSource().getPlayer();
                            double amount = ctx.getArgument("amount", Double.class);

                            BankManager bankManager = BankManager.get(ctx.getSource().getServer());
                            EconomyManager economyManager = EconomyManager.get(ctx.getSource().getServer());

                            // 检查银行存款是否足够
                            if (bankManager.getBankBalance(player) < amount) {
                                ctx.getSource().sendFeedback(() -> Text.translatable("insufficient_bank_balance"), false);
                                return 0;
                            }

                            // 执行取款
                            bankManager.withdraw(player, amount);
                            economyManager.addBalance(player, amount);

                            // 反馈给玩家
                            player.sendMessage(Text.translatable("withdrew_success", String.format("%.2f", amount), bankManager.getBankBalance(player)), false);
                            return 1;
                        }))
        );

        // 添加货币
        dispatcher.register(literal("addmoney")
                .requires(source -> source.hasPermissionLevel(2)) // 设置权限级别，2表示管理员级别
                .then(argument("player", player())
                        .then(argument("amount", doubleArg(0.01))
                                .executes(ctx -> {
                                    ServerPlayerEntity player = getPlayer(ctx, "player");
                                    double amount = ctx.getArgument("amount", Double.class);
                                    EconomyManager.get(ctx.getSource().getServer()).addBalance(player, amount);
                                    ctx.getSource().sendFeedback(() -> Text.translatable("add_player_money", String.format("%.2f", amount), player.getName().getString()), false);
                                    return 1;
                                })))
        );

        // 移除货币
        dispatcher.register(literal("removemoney")
                .requires(source -> source.hasPermissionLevel(2)) // 设置权限级别，2表示管理员级别
                .then(argument("player", player())
                        .then(argument("amount", doubleArg(0.01))
                                .executes(ctx -> {
                                    ServerPlayerEntity player = getPlayer(ctx, "player");
                                    double amount = ctx.getArgument("amount", Double.class);
                                    EconomyManager.get(ctx.getSource().getServer()).removeBalance(player, amount);
                                    ctx.getSource().sendFeedback(() -> Text.translatable("remove_player_money", String.format("%.2f", amount), player.getName().getString()), false);
                                    return 1;
                                })))
        );

        // 转账功能
        dispatcher.register(literal("transfer")
                .then(argument("target", player())
                        .then(argument("amount", doubleArg(0.01))
                                .executes(ctx -> {
                                    ServerPlayerEntity sender = ctx.getSource().getPlayer();
                                    ServerPlayerEntity target = getPlayer(ctx, "target");
                                    double amount = ctx.getArgument("amount", Double.class);

                                    EconomyManager economyManager = EconomyManager.get(ctx.getSource().getServer());

                                    // 检查发送者的余额是否足够
                                    if (economyManager.getBalance(sender) < amount) {
                                        ctx.getSource().sendFeedback(() -> Text.translatable("insufficient_balance"), false);
                                        return 0;
                                    }

                                    // 执行转账
                                    economyManager.removeBalance(sender, amount);
                                    economyManager.addBalance(target, amount);

                                    // 反馈给发送者和接收者
                                    sender.sendMessage(Text.translatable("transfer_success", String.format("%.2f", amount), target.getName().getString()), false);
                                    target.sendMessage(Text.translatable("receive_transfer", String.format("%.2f", amount), sender.getName().getString()), false);

                                    return 1;
                                })))
        );
    }
}
