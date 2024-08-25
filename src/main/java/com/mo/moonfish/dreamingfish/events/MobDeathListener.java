package com.mo.moonfish.dreamingfish.events;

import com.mo.moonfish.dreamingfish.economy.EconomyManager;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Random;

public class MobDeathListener {
    private static final Random RANDOM = new Random();
    private static final double DROP_CHANCE = 25.0;  // 玩家有 25% 的概率获得奖励
    private static final double MIN_REWARD = 0.01;    // 最小奖励金额
    private static final double MAX_REWARD = 2.00;   // 最大奖励金额

    public static void register() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, source) -> {
            if (source.getAttacker() instanceof ServerPlayerEntity player && entity instanceof LivingEntity) {
                // 生成0到100的随机数
                double randomValue = RANDOM.nextDouble() * 100;

                // 检查是否有奖励
                if (randomValue < DROP_CHANCE) {  // 相当于25%几率
                    double reward = MIN_REWARD + (MAX_REWARD - MIN_REWARD) * (randomValue / 100);
                    EconomyManager economyManager = EconomyManager.get(player.getServer());
                    economyManager.addBalance(player, reward);

                    // 获取被杀死实体的名字，而不是攻击者的名字
                    Text entityName = entity.getDisplayName();
                    player.sendMessage(Text.translatable("get_money_from_mob", String.format("%.2f", reward)), false);
                }
            }
        });
    }
}
