package com.mo.economy.new_economy_system.bank;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.PersistentState;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BankManager extends PersistentState {
    private final Map<UUID, BankAccount> playerBankAccounts = new HashMap<>();

    public BankAccount getBankAccount(ServerPlayerEntity player) {
        return playerBankAccounts.computeIfAbsent(player.getUuid(), uuid -> new BankAccount());
    }

    public int[] getBalance(ServerPlayerEntity player) {
        BankAccount account = getBankAccount(player);
        return new int[] {
                account.getBalance("copper_coin"),
                account.getBalance("silver_coin"),
                account.getBalance("gold_coin")
        };
    }

    public void deposit(ServerPlayerEntity player, String coinType, int amount) {
        System.out.println("Depositing coins...");
        BankAccount account = getBankAccount(player);
        account.deposit(coinType, amount);
        markDirty();
        System.out.println("Data saved successfully.");
    }

    public boolean withdraw(ServerPlayerEntity player, String coinType, int amount) {
        BankAccount account = getBankAccount(player);
        boolean success = account.withdraw(coinType, amount);
        if (success) {
            markDirty();
            System.out.println("Data saved successfully after withdrawal.");
        }
        return success;
    }

    public void applyInterest() {
        for (BankAccount account : playerBankAccounts.values()) {
            account.applyInterest();
        }
        markDirty();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound bankNbt = new NbtCompound();
        for (Map.Entry<UUID, BankAccount> entry : playerBankAccounts.entrySet()) {
            NbtCompound accountNbt = new NbtCompound();
            entry.getValue().toNbt(accountNbt);
            bankNbt.put(entry.getKey().toString(), accountNbt);
        }
        nbt.put("PlayerBankAccounts", bankNbt);
        return nbt;
    }

    public static BankManager fromNbt(NbtCompound nbt) {
        BankManager manager = new BankManager();
        NbtCompound bankNbt = nbt.getCompound("PlayerBankAccounts");
        for (String key : bankNbt.getKeys()) {
            NbtCompound accountNbt = bankNbt.getCompound(key);
            BankAccount account = new BankAccount();
            account.fromNbt(accountNbt);
            manager.playerBankAccounts.put(UUID.fromString(key), account);
        }
        return manager;
    }

    public static BankManager get(MinecraftServer server) {
        return server.getOverworld().getPersistentStateManager().getOrCreate(
                BankManager::fromNbt, BankManager::new, "bank_manager");
    }
}
