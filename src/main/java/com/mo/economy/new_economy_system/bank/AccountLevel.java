package com.mo.economy.new_economy_system.bank;

public class AccountLevel {

    private final int maxCopperCoins;
    private final int maxSilverCoins;
    private final int maxGoldCoins;

    private final double copperInterestRate;
    private final double silverInterestRate;
    private final double goldInterestRate;

    public AccountLevel(int maxCopper, int maxSilver, int maxGold, double copperRate, double silverRate, double goldRate) {
        this.maxCopperCoins = maxCopper;
        this.maxSilverCoins = maxSilver;
        this.maxGoldCoins = maxGold;
        this.copperInterestRate = copperRate;
        this.silverInterestRate = silverRate;
        this.goldInterestRate = goldRate;
    }

    // 获取各货币的最大存储量
    public int getMaxCopperCoins() {
        return maxCopperCoins;
    }

    public int getMaxSilverCoins() {
        return maxSilverCoins;
    }

    public int getMaxGoldCoins() {
        return maxGoldCoins;
    }

    // 获取各货币的利率
    public double getCopperInterestRate() {
        return copperInterestRate;
    }

    public double getSilverInterestRate() {
        return silverInterestRate;
    }

    public double getGoldInterestRate() {
        return goldInterestRate;
    }
}
