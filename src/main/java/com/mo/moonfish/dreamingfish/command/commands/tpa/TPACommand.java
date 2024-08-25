package com.mo.moonfish.dreamingfish.command.commands.tpa;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.command.CommandSource;

public class TPACommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("tpa")
                .then(CommandManager.argument("target", StringArgumentType.string())
                        .suggests((context, builder) -> CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder))
                        .executes(context -> {
                            ServerPlayerEntity requester = context.getSource().getPlayer();
                            String targetName = StringArgumentType.getString(context, "target");
                            ServerPlayerEntity target = context.getSource().getServer().getPlayerManager().getPlayer(targetName);
                            if (target != null) {
                                TPAHandler.sendTPARequest(requester, target, context);
                            } else {
                                context.getSource().sendError(Text.translatable("player_not_found"));
                            }
                            return 1;
                        })
                )
        );

        dispatcher.register(CommandManager.literal("tpaccept")
                .executes(context -> {
                    ServerPlayerEntity target = context.getSource().getPlayer();
                    TPAHandler.acceptTPARequest(target);
                    return 1;
                })
        );

        dispatcher.register(CommandManager.literal("tpdeny")
                .executes(context -> {
                    ServerPlayerEntity target = context.getSource().getPlayer();
                    TPAHandler.denyTPARequest(target);
                    return 1;
                })
        );
    }
}

