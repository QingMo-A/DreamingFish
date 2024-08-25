package com.mo.moonfish.dreamingfish.economy;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.PersistentState;
import net.minecraft.nbt.NbtCompound;

import java.util.*;

public class EconomyManager extends PersistentState {
    private final Map<UUID, Double> balances = new HashMap<>();
    private final Map<UUID, Welfare> welfares = new HashMap<>();

    public double getBalance(ServerPlayerEntity player) {
        return balances.getOrDefault(player.getUuid(), 0.0);
    }

    public void addBalance(ServerPlayerEntity player, double amount) {
        balances.put(player.getUuid(), getBalance(player) + amount);
        markDirty();
    }

    public void removeBalance(ServerPlayerEntity player, double amount) {
        addBalance(player, -amount);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound balancesNbt = new NbtCompound();
        for (Map.Entry<UUID, Double> entry : balances.entrySet()) {
            balancesNbt.putDouble(entry.getKey().toString(), entry.getValue());
        }
        nbt.put("Balances", balancesNbt);

        NbtCompound welfaresNbt = new NbtCompound();
        for (Map.Entry<UUID, Welfare> entry : welfares.entrySet()) {
            welfaresNbt.put(entry.getKey().toString(), entry.getValue().toNbt());
        }
        nbt.put("Welfares", welfaresNbt);

        return nbt;
    }

    public static EconomyManager fromNbt(NbtCompound nbt) {
        EconomyManager manager = new EconomyManager();

        NbtCompound balancesNbt = nbt.getCompound("Balances");
        for (String key : balancesNbt.getKeys()) {
            manager.balances.put(UUID.fromString(key), balancesNbt.getDouble(key));
        }

        NbtCompound welfaresNbt = nbt.getCompound("Welfares");
        for (String key : welfaresNbt.getKeys()) {
            manager.welfares.put(UUID.fromString(key), Welfare.fromNbt(welfaresNbt.getCompound(key)));
        }

        return manager;
    }

    public static EconomyManager get(MinecraftServer server) {
        return server.getOverworld().getPersistentStateManager().getOrCreate(
                EconomyManager::fromNbt, EconomyManager::new, "economy"
        );
    }

    public UUID createWelfare(ServerPlayerEntity player, double totalAmount, int count, boolean isLucky) {
        if (getBalance(player) < totalAmount) {
            return null; // 余额不足
        }

        UUID welfareId = UUID.randomUUID();
        Welfare welfare = new Welfare(welfareId, player.getUuid(), totalAmount, count, isLucky);
        welfares.put(welfareId, welfare);
        removeBalance(player, totalAmount);
        markDirty();
        return welfareId;
    }

    public double claimWelfare(UUID welfareId, ServerPlayerEntity player) {
        Welfare welfare = welfares.get(welfareId);
        if (welfare == null || welfare.isClaimedBy(player.getUuid())) {
            return 0.0; // 红包不存在或已被该玩家领取
        }

        double amount = welfare.claim(player.getUuid());
        markDirty();

        // 保留两位小数
        return Math.round(amount * 100.0) / 100.0;
    }

    public void addBalanceByUUID(UUID playerUuid, double amount) {
        balances.put(playerUuid, balances.getOrDefault(playerUuid, 0.0) + amount);
        markDirty();
    }
}
