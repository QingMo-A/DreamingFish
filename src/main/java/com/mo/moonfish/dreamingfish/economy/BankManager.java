package com.mo.moonfish.dreamingfish.economy;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.PersistentState;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BankManager extends PersistentState {
    private final Map<UUID, Double> bankBalances = new HashMap<>();

    public double getBankBalance(ServerPlayerEntity player) {
        return bankBalances.getOrDefault(player.getUuid(), 0.0);
    }

    public void deposit(ServerPlayerEntity player, double amount) {
        bankBalances.put(player.getUuid(), getBankBalance(player) + amount);
        markDirty();
    }

    public void withdraw(ServerPlayerEntity player, double amount) {
        deposit(player, -amount);
    }

    public void applyInterest() {
        for (UUID uuid : bankBalances.keySet()) {
            double balance = bankBalances.get(uuid);
            double interest = balance * 0.001;  // 0.1%的利息
            if(interest > 500) {
                bankBalances.put(uuid, balance + 500);
            } else {
                bankBalances.put(uuid, balance + interest);
            }
        }
        markDirty();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound bankNbt = new NbtCompound();
        for (Map.Entry<UUID, Double> entry : bankBalances.entrySet()) {
            bankNbt.putDouble(entry.getKey().toString(), entry.getValue());
        }
        nbt.put("BankBalances", bankNbt);
        return bankNbt;
    }

    public static BankManager fromNbt(NbtCompound nbt) {
        BankManager manager = new BankManager();
        NbtCompound bankNbt = nbt.getCompound("BankBalances");
        for (String key : bankNbt.getKeys()) {
            manager.bankBalances.put(UUID.fromString(key), bankNbt.getDouble(key));
        }
        return manager;
    }

    public static BankManager get(MinecraftServer server) {
        return server.getOverworld().getPersistentStateManager().getOrCreate(
                BankManager::fromNbt, BankManager::new, "bank");
    }
}
