package com.mo.economy.new_economy_system.bank;

import net.minecraft.nbt.NbtCompound;

public class BankAccount {

    private int copperCoins;
    private int silverCoins;
    private int goldCoins;
    private int level; // 当前账户等级

    public BankAccount() {
        this.copperCoins = 0;
        this.silverCoins = 0;
        this.goldCoins = 0;
        this.level = 1; // 默认账户等级为1
    }

    // 获取账户等级
    public AccountLevel getAccountLevel() {
        switch (level) {
            case 2: return AccountLevels.LEVEL_2;
            case 3: return AccountLevels.LEVEL_3;
            case 4: return AccountLevels.LEVEL_4;
            case 5: return AccountLevels.LEVEL_5;
            default: return AccountLevels.LEVEL_1;
        }
    }

    // 升级账户等级
    public void upgradeLevel() {
        if (level < 5) {
            level++;
        }
    }

    public int getLevel() {
        return level;
    }


    // 存款功能，考虑最大存储限制
    public void deposit(String coinType, int amount) {
        AccountLevel currentLevel = getAccountLevel();

        switch (coinType) {
            case "copper_coin" -> {
                if (copperCoins + amount <= currentLevel.getMaxCopperCoins()) {
                    copperCoins += amount;
                    System.out.println("Deposited " + amount + " copper coins.");
                } else {
                    System.out.println("Copper deposit exceeds limit!");
                }
            }
            case "silver_coin" -> {
                if (silverCoins + amount <= currentLevel.getMaxSilverCoins()) {
                    silverCoins += amount;
                    System.out.println("Deposited " + amount + " silver coins.");
                } else {
                    System.out.println("Silver deposit exceeds limit!");
                }
            }
            case "gold_coin" -> {
                if (goldCoins + amount <= currentLevel.getMaxGoldCoins()) {
                    goldCoins += amount;
                    System.out.println("Deposited " + amount + " gold coins.");
                } else {
                    System.out.println("Gold deposit exceeds limit!");
                }
            }
        }

        // 输出当前账户的所有余额
        System.out.println("New balance: Copper = " + copperCoins + ", Silver = " + silverCoins + ", Gold = " + goldCoins);
    }

    // 取款功能
    public boolean withdraw(String coinType, int amount) {
        switch (coinType) {
            case "copper_coin" -> {
                if (copperCoins >= amount) {
                    copperCoins -= amount;
                    System.out.println("Withdrew " + amount + " copper coins.");
                    return true;
                }
            }
            case "silver_coin" -> {
                if (silverCoins >= amount) {
                    silverCoins -= amount;
                    System.out.println("Withdrew " + amount + " silver coins.");
                    return true;
                }
            }
            case "gold_coin" -> {
                if (goldCoins >= amount) {
                    goldCoins -= amount;
                    System.out.println("Withdrew " + amount + " gold coins.");
                    return true;
                }
            }
        }
        return false; // 余额不足
    }

    // 计算利息并增加余额
    public void applyInterest() {
        AccountLevel currentLevel = getAccountLevel();

        copperCoins += (int) (copperCoins * currentLevel.getCopperInterestRate());
        silverCoins += (int) (silverCoins * currentLevel.getSilverInterestRate());
        goldCoins += (int) (goldCoins * currentLevel.getGoldInterestRate());
    }

    // 查看余额
    public int getBalance(String coinType) {
        return switch (coinType) {
            case "copper_coin" -> copperCoins;
            case "silver_coin" -> silverCoins;
            case "gold_coin" -> goldCoins;
            default -> 0;
        };
    }

    // 将账户数据保存到 NBT
    public void toNbt(NbtCompound nbt) {
        System.out.println("Saving account to NBT...");
        nbt.putInt("copper_coins", copperCoins);
        nbt.putInt("silver_coins", silverCoins);
        nbt.putInt("gold_coins", goldCoins);
        nbt.putInt("account_level", level);
        System.out.println("Saved: Copper = " + copperCoins + ", Silver = " + silverCoins + ", Gold = " + goldCoins);
    }

    // 从 NBT 读取账户数据
    public void fromNbt(NbtCompound nbt) {
        System.out.println("Loading account from NBT...");
        this.copperCoins = nbt.getInt("copper_coins");
        this.silverCoins = nbt.getInt("silver_coins");
        this.goldCoins = nbt.getInt("gold_coins");
        this.level = nbt.getInt("account_level");
        System.out.println("Loaded: Copper = " + copperCoins + ", Silver = " + silverCoins + ", Gold = " + goldCoins);
    }
}
