package com.mo.economy.new_economy_system.bank;

public class AccountLevels {
    public static final AccountLevel LEVEL_1 = new AccountLevel(
            100, 50, 25,
            0.01, 0.005, 0.002);

    public static final AccountLevel LEVEL_2 = new AccountLevel(
            200, 100, 50,
            0.02, 0.01, 0.004);

    public static final AccountLevel LEVEL_3 = new AccountLevel(
            500, 250, 100,
            0.03, 0.015, 0.006);

    public static final AccountLevel LEVEL_4 = new AccountLevel(
            1000, 500, 250,
            0.04, 0.02, 0.008);

    public static final AccountLevel LEVEL_5 = new AccountLevel(
            2000, 1000, 500,
            0.05, 0.025, 0.01);

    public static AccountLevel getPlayerAccountLevel(int level) {
        return switch (level) {
            case 1 -> LEVEL_1;
            case 2 -> LEVEL_2;
            case 3 -> LEVEL_3;
            case 4 -> LEVEL_4;
            case 5 -> LEVEL_5;
            default -> LEVEL_1;
        };
    }
}
