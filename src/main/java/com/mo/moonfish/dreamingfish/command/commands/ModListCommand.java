package com.mo.moonfish.dreamingfish.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ModListCommand {

    public static final Identifier GET_MOD_LIST_PACKET_ID = new Identifier("dreamingfish", "get_mod_list");

    public static ServerPlayerEntity player;

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("getmodlist")
                    .then(CommandManager.argument("player", StringArgumentType.string())
                            .suggests((context, builder) -> CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder))
                            .executes(context -> {
                                player = context.getSource().getPlayer();
                                String playerName = StringArgumentType.getString(context, "player");
                                ServerPlayerEntity targetPlayer = context.getSource().getServer().getPlayerManager().getPlayer(playerName);

                                if (targetPlayer != null) {
                                    // 发送请求给客户端
                                    ServerPlayNetworking.send(targetPlayer, GET_MOD_LIST_PACKET_ID, new PacketByteBuf(Unpooled.buffer()));

                                    context.getSource().sendFeedback(() -> Text.literal("请求已发送到玩家 " + playerName + " 的客户端。"), false);
                                } else {
                                    context.getSource().sendError(Text.literal("无法找到玩家: " + playerName));
                                }

                                return 1;
                            })));
        });

        // 注册监听客户端返回数据的处理方法
        ServerPlayNetworking.registerGlobalReceiver(GET_MOD_LIST_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            int size = buf.readInt();
            List<String> modList = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                modList.add(buf.readString(32767));
            }

            server.execute(() -> {
                handleModListResponse(player, modList);
            });
        });
    }

    // 处理客户端返回的模组列表
    public static void handleModListResponse(ServerPlayerEntity player, List<String> modList) {
        String modListStr = String.join("\n", modList);
        ModListCommand.player.sendMessage(Text.literal("玩家 " + player.getEntityName() + " 的模组文件列表: " + modListStr), false);
    }
}
