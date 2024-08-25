package com.mo.moonfish.dreamingfish.mixin;

import com.mo.moonfish.dreamingfish.economy.EconomyManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class PlayerDeathMixin {

    @Inject(method = "onDeath", at = @At("TAIL"))
    private void onPlayerDeath(DamageSource source, CallbackInfo info) {
        if ((Object) this instanceof ServerPlayerEntity player) {
            EconomyManager economyManager = EconomyManager.get(player.getServer());

            // 获取玩家的余额的一半
            double lostAmount = economyManager.getBalance(player) / 2;

            // 扣除玩家的余额
            economyManager.removeBalance(player, lostAmount);

            // 发送给玩家的信息
            player.sendMessage(Text.translatable("lose_money_from_death", String.format("%.2f", lostAmount)), false);

            // 检查是否是被另一个玩家杀死
            if (source.getAttacker() instanceof ServerPlayerEntity killer) {
                // 增加杀死玩家的余额
                economyManager.addBalance(killer, lostAmount);

                // 发送给杀死玩家的信息
                killer.sendMessage(Text.translatable("received_money_from_other_player", String.format("%.2f", lostAmount), player.getName().getString()), false);
            }
        }
    }
}
