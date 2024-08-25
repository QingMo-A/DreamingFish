package com.mo.moonfish.dreamingfish.command.commands.tpa;

import com.mo.moonfish.dreamingfish.config.ConfigManager;
import com.mo.moonfish.dreamingfish.item.ModItems;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TPAHandler {
    private static final HashMap<UUID, UUID> tpaRequests = new HashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void init() {
        // 初始化逻辑
        ConfigManager.loadConfig(); // 加载配置
    }

    public static void sendTPARequest(ServerPlayerEntity requester, ServerPlayerEntity target, CommandContext<ServerCommandSource> context) {
        // 检查配置文件中的设置
        if (ConfigManager.getConfig().requireWormholePotion) {
            // 检查背包中是否有虫洞药水
            if (!hasWormholePotion(requester)) {
                requester.sendMessage(Text.translatable("no_wormhole_potion_in_inventory"), false);
                return;
            }
        }

        UUID targetUUID = target.getUuid();
        UUID requesterUUID = requester.getUuid();

        tpaRequests.put(targetUUID, requesterUUID);
        context.getSource().sendFeedback(() -> Text.translatable("teleport_request_sent", target.getName().getString()), false);
        target.sendMessage(Text.translatable("receive_requests_from_players", requester.getName().getString()), false);

        // 设置一个60秒的延迟任务来检查请求是否过期
        scheduler.schedule(() -> {
            if (tpaRequests.containsKey(targetUUID) && tpaRequests.get(targetUUID).equals(requesterUUID)) {
                tpaRequests.remove(targetUUID);
                requester.sendMessage(Text.translatable("teleport_request_expired", target.getName().getString()), false);
                target.sendMessage(Text.translatable("teleport_request_expired", requester.getName().getString()), false);
            }
        }, 60, TimeUnit.SECONDS);
    }

    public static void acceptTPARequest(ServerPlayerEntity target) {
        UUID requesterUUID = tpaRequests.remove(target.getUuid());
        if (requesterUUID != null) {
            ServerPlayerEntity requester = target.getServer().getPlayerManager().getPlayer(requesterUUID);
            if (requester != null) {
                // 检查配置文件中的设置
                if (ConfigManager.getConfig().requireWormholePotion) {
                    // 确认传送前消耗一瓶虫洞药水
                    if (consumeWormholePotion(requester)) {
                        requester.teleport(target.getServerWorld(), target.getX(), target.getY(), target.getZ(), target.getYaw(), target.getPitch());
                        target.sendMessage(Text.translatable("teleport_quest_accepted"), false);
                        requester.sendMessage(Text.translatable("teleporting_to_player", target.getName().getString()), false);
                    } else {
                        requester.sendMessage(Text.translatable("no_wormhole_potion_in_inventory"), false);
                        target.sendMessage(Text.translatable("teleport_request_failed_no_wormhole_potion"), false);
                    }
                } else {
                    // 不需要虫洞药水的情况下直接传送
                    requester.teleport(target.getServerWorld(), target.getX(), target.getY(), target.getZ(), target.getYaw(), target.getPitch());
                    target.sendMessage(Text.translatable("teleport_quest_accepted"), false);
                    requester.sendMessage(Text.translatable("teleporting_to_player", target.getName().getString()), false);
                }
            } else {
                target.sendMessage(Text.translatable("player_is_not_online"), false);
            }
        } else {
            target.sendMessage(Text.translatable("not_teleport_request_accepted"), false);
        }
    }

    public static void denyTPARequest(ServerPlayerEntity target) {
        UUID requesterUUID = tpaRequests.remove(target.getUuid());
        if (requesterUUID != null) {
            ServerPlayerEntity requester = target.getServer().getPlayerManager().getPlayer(requesterUUID);
            if (requester != null) {
                requester.sendMessage(Text.translatable("player_refuse_teleport_request", target.getName().getString()), false);
            }
            target.sendMessage(Text.translatable("teleport_request_denied"), false);
        } else {
            target.sendMessage(Text.translatable("not_teleport_request_denied"), false);
        }
    }

    // 检查玩家背包中是否有虫洞药水
    private static boolean hasWormholePotion(ServerPlayerEntity player) {
        return player.getInventory().contains(new ItemStack(ModItems.WORMHOLE_POTION));
    }

    // 消耗玩家背包中的一瓶虫洞药水
    private static boolean consumeWormholePotion(ServerPlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.getItem() == ModItems.WORMHOLE_POTION) {
                stack.decrement(1); // 减少一瓶虫洞药水
                return true;
            }
        }
        return false;
    }
}
