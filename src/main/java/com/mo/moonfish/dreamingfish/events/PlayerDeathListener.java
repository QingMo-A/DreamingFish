package com.mo.moonfish.dreamingfish.events;

import com.mo.moonfish.dreamingfish.economy.EconomyManager;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class PlayerDeathListener {

    public static void register() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, source) -> {
            // 检查事件是否触发
            System.out.println("AFTER_KILLED_OTHER_ENTITY event triggered");

            // 检查被杀死的实体是否为玩家
            if (entity instanceof ServerPlayerEntity deadPlayer) {
                System.out.println("A player has died: " + deadPlayer.getName().getString());

                EconomyManager economyManager = EconomyManager.get(deadPlayer.getServer());

                // 获取被杀玩家的余额的一半
                double lostAmount = economyManager.getBalance(deadPlayer) / 2;

                // 扣除被杀玩家的余额
                economyManager.removeBalance(deadPlayer, lostAmount);

                // 发送给被杀玩家的信息
                deadPlayer.sendMessage(Text.literal("You lost $" + String.format("%.2f", lostAmount) + " on death!"), false);

                // 检查是否是被另一个玩家杀死
                if (source.getAttacker() instanceof ServerPlayerEntity killer) {
                    System.out.println("Killed by another player: " + killer.getName().getString());

                    // 增加杀死玩家的余额
                    economyManager.addBalance(killer, lostAmount);

                    // 发送给杀死玩家的信息
                    killer.sendMessage(Text.literal("You received $" + String.format("%.2f", lostAmount) + " from killing " + deadPlayer.getName().getString() + "!"), false);
                }
            }
        });
    }
}
