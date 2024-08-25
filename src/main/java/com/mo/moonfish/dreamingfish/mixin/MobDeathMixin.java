package com.mo.moonfish.dreamingfish.mixin;

import com.mo.moonfish.DreamingFish;
import com.mo.moonfish.dreamingfish.config.ConfigManager;
import com.mo.moonfish.dreamingfish.economy.EconomyManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class MobDeathMixin {

    @Shadow public abstract void endCombat();

    private static final Random RANDOM = new Random();
    private static final double DROP_CHANCE = 10.0;  // 玩家有 10% 的概率获得奖励
    private static final double MIN_REWARD = 0.01;   // 最小奖励金额
    private static final double MAX_REWARD = 2.00;    // 最大奖励金额

    @Inject(method = "onDeath", at = @At("TAIL"))
    private void onMobDeath(DamageSource source, CallbackInfo info) {
        if (source.getAttacker() instanceof ServerPlayerEntity player) {

            LivingEntity killedEntity = (LivingEntity) (Object) this;

            // 检查是否启用黑名单
            ConfigManager.Config config = ConfigManager.getConfig();
            if (config.enableMobBlacklist) {
                String entityId = killedEntity.getType().getTranslationKey();
                if (config.mobBlacklist.contains(entityId)) {
                    return; // 该生物在黑名单中，不执行奖励逻辑
                }
            }

            // 生成0到100的随机数
            double randomValue = RANDOM.nextDouble() * 100;

            // 检查是否有奖励
            if (randomValue < DROP_CHANCE) {  // 相当于10%几率
                double reward = MIN_REWARD + (MAX_REWARD - MIN_REWARD) * (randomValue / 100);
                EconomyManager economyManager = EconomyManager.get(player.getServer());
                economyManager.addBalance(player, reward);

                Text entityName = killedEntity.getDisplayName();

                // 发送奖励信息给玩家
                player.sendMessage(Text.translatable("get_money_from_mob", entityName, String.format("%.2f", reward)), false);
            }
        }
    }
}
