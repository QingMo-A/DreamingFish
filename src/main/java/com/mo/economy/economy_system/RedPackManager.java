package com.mo.economy.economy_system;

import net.minecraft.nbt.NbtCompound;
import java.util.*;

public class RedPackManager {
    private final UUID id;
    private final UUID owner;
    private final double totalAmount;
    private final int totalCount;
    private final boolean isLucky;
    private final List<Double> amounts;
    private final Set<UUID> claimedPlayers;

    public RedPackManager(UUID id, UUID owner, double totalAmount, int totalCount, boolean isLucky) {
        this.id = id;
        this.owner = owner;
        this.totalAmount = totalAmount;
        this.totalCount = totalCount;
        this.isLucky = isLucky;
        this.claimedPlayers = new HashSet<>();
        this.amounts = generateAmounts(totalAmount, totalCount, isLucky);
    }

    private List<Double> generateAmounts(double totalAmount, int totalCount, boolean isLucky) {
        List<Double> amounts = new ArrayList<>();
        Random random = new Random();

        if (isLucky) {
            double remaining = totalAmount;
            for (int i = 0; i < totalCount - 1; i++) {
                double amount = Math.round(remaining * (random.nextDouble() * 2 / totalCount) * 100.0) / 100.0;
                amounts.add(amount);
                remaining -= amount;
            }
            amounts.add(Math.round(remaining * 100.0) / 100.0);
        } else {
            double amount = Math.round((totalAmount / totalCount) * 100.0) / 100.0;
            for (int i = 0; i < totalCount; i++) {
                amounts.add(amount);
            }
        }

        Collections.shuffle(amounts);
        return amounts;
    }

    public boolean isClaimedBy(UUID playerUuid) {
        return claimedPlayers.contains(playerUuid);
    }

    public double claim(UUID playerUuid) {
        if (claimedPlayers.contains(playerUuid) || amounts.isEmpty()) {
            return 0.0;
        }
        double amount = amounts.remove(0);
        claimedPlayers.add(playerUuid);
        return amount;
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putUuid("Id", id);
        nbt.putUuid("Owner", owner);
        nbt.putDouble("TotalAmount", totalAmount);
        nbt.putInt("TotalCount", totalCount);
        nbt.putBoolean("IsLucky", isLucky);

        NbtCompound amountsNbt = new NbtCompound();
        for (int i = 0; i < amounts.size(); i++) {
            amountsNbt.putDouble(String.valueOf(i), amounts.get(i));
        }
        nbt.put("Amounts", amountsNbt);

        NbtCompound claimedNbt = new NbtCompound();
        for (UUID claimedPlayer : claimedPlayers) {
            claimedNbt.putUuid(claimedPlayer.toString(), claimedPlayer);
        }
        nbt.put("ClaimedPlayers", claimedNbt);

        return nbt;
    }

    public static RedPackManager fromNbt(NbtCompound nbt) {
        UUID id = nbt.getUuid("Id");
        UUID owner = nbt.getUuid("Owner");
        double totalAmount = nbt.getDouble("TotalAmount");
        int totalCount = nbt.getInt("TotalCount");
        boolean isLucky = nbt.getBoolean("IsLucky");

        RedPackManager redPack = new RedPackManager(id, owner, totalAmount, totalCount, isLucky);

        NbtCompound amountsNbt = nbt.getCompound("Amounts");
        for (String key : amountsNbt.getKeys()) {
            redPack.amounts.add(amountsNbt.getDouble(key));
        }

        NbtCompound claimedNbt = nbt.getCompound("ClaimedPlayers");
        for (String key : claimedNbt.getKeys()) {
            redPack.claimedPlayers.add(UUID.fromString(key));
        }

        return redPack;
    }
}

