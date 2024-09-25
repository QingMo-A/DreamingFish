/*
package com.mo.economy.gui.home_interface;

import com.mo.economy.item.ModItems;
import com.mo.economy.new_economy_system.bank.AccountLevel;
import com.mo.economy.new_economy_system.bank.AccountLevels;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class BankInterface {
    public static WLabel bankLevelLabel;
    private static AccountLevel accountLevel;
    private static final int BACK_LEVEL_LABEL_X = 130;
    private static final int BACK_LEVEL_LABEL_Y = 40;
    private static WLabel COPPER_COIN_COUNT_LABEL;
    private static WLabel SILVER_COIN_COUNT_LABEL;
    private static WLabel GOLD_COIN_COUNT_LABEL;



    private static final int COPPER_COIN_ICON_X = BACK_LEVEL_LABEL_X + 70;
    private static final int COPPER_COIN_ICON_Y = BACK_LEVEL_LABEL_Y;

    private static final int COPPER_COIN_COUNT_X = COPPER_COIN_ICON_X + 20;
    private static final int SILVER_COIN_ICON_X = COPPER_COIN_COUNT_X + 20;
    private static final int SILVER_COIN_COUNT_X = SILVER_COIN_ICON_X + 20;
    private static final int GOLD_COIN_ICON_X = SILVER_COIN_COUNT_X + 20;
    private static final int GOLD_COIN_COUNT_X = GOLD_COIN_ICON_X + 20;

    // 存入数额输入框
    private static final int DEPOSIT_TEXT_FIELD_X = 85;
    private static final int DEPOSIT_TEXT_FIELD_Y = 70;
    // 存入铜币按钮
    private static final int DEPOSIT_COPPER_COIN_BUTTON_X = DEPOSIT_TEXT_FIELD_X + 105;
    private static final int DEPOSIT_COPPER_COIN_BUTTON_Y = DEPOSIT_TEXT_FIELD_Y;
    // 存入银币按钮
    private static final int DEPOSIT_SILVER_COIN_BUTTON_X = DEPOSIT_COPPER_COIN_BUTTON_X + 75;
    private static final int DEPOSIT_SILVER_COIN_BUTTON_Y = DEPOSIT_COPPER_COIN_BUTTON_Y;
    // 存入金币按钮
    private static final int DEPOSIT_GOLD_COIN_BUTTON_X = DEPOSIT_SILVER_COIN_BUTTON_X + 75;
    private static final int DEPOSIT_GOLD_COIN_BUTTON_Y = DEPOSIT_COPPER_COIN_BUTTON_Y;

    // 存入数额输入框
    private static final int WITHDRAW_TEXT_FIELD_X = DEPOSIT_TEXT_FIELD_X;
    private static final int WITHDRAW_TEXT_FIELD_Y = DEPOSIT_TEXT_FIELD_Y + 30;
    // 取出铜币按钮
    private static final int WITHDRAW_COPPER_COIN_BUTTON_X = DEPOSIT_COPPER_COIN_BUTTON_X;
    private static final int WITHDRAW_COPPER_COIN_BUTTON_Y = DEPOSIT_COPPER_COIN_BUTTON_Y + 30;
    // 取出银币按钮
    private static final int WITHDRAW_SILVER_COIN_BUTTON_X = DEPOSIT_SILVER_COIN_BUTTON_X;
    private static final int WITHDRAW_SILVER_COIN_BUTTON_Y = WITHDRAW_COPPER_COIN_BUTTON_Y;
    // 取出金币按钮
    private static final int WITHDRAW_GOLD_COIN_BUTTON_X = DEPOSIT_GOLD_COIN_BUTTON_X;
    private static final int WITHDRAW_GOLD_COIN_BUTTON_Y = WITHDRAW_COPPER_COIN_BUTTON_Y;

    public WPlainPanel getBankInterface() {
        WPlainPanel bankPane = new WPlainPanel();
        bankPane.setSize(500, 300);  // 设置面板大小

        bankLevelLabel = new WLabel(Text.literal("银行账户等级: 1"));
        bankPane.add(bankLevelLabel, BACK_LEVEL_LABEL_X, BACK_LEVEL_LABEL_Y);

        // 创建一个 WItem 组件用于渲染物品
        WItem copperCoinIcon = new WItem(new ItemStack(ModItems.COPPER_COIN));  // 创建 WItem，用于显示物品
        bankPane.add(copperCoinIcon, COPPER_COIN_ICON_X, COPPER_COIN_ICON_Y);

        // 铜币数量
        COPPER_COIN_COUNT_LABEL = new WLabel(Text.literal("0"));
        bankPane.add(COPPER_COIN_COUNT_LABEL, COPPER_COIN_COUNT_X, COPPER_COIN_ICON_Y);

        // 创建一个 WItem 组件用于渲染物品
        WItem silverCoinIcon = new WItem(new ItemStack(ModItems.SILVER_COIN));  // 创建 WItem，用于显示物品
        bankPane.add(silverCoinIcon, SILVER_COIN_ICON_X, COPPER_COIN_ICON_Y);

        // 银币数量
        SILVER_COIN_COUNT_LABEL = new WLabel(Text.literal("0"));
        bankPane.add(SILVER_COIN_COUNT_LABEL, SILVER_COIN_COUNT_X, COPPER_COIN_ICON_Y);

        // 创建一个 WItem 组件用于渲染物品
        WItem goldCoinIcon = new WItem(new ItemStack(ModItems.GOLD_COIN));  // 创建 WItem，用于显示物品
        bankPane.add(goldCoinIcon, GOLD_COIN_ICON_X, COPPER_COIN_ICON_Y);

        // 金币数量
        GOLD_COIN_COUNT_LABEL = new WLabel(Text.literal("0"));
        bankPane.add(GOLD_COIN_COUNT_LABEL, GOLD_COIN_COUNT_X, COPPER_COIN_ICON_Y);

        requestBalance();  // 请求余额
        requestBankLevel(); // 请求银行等级

        accountLevel = AccountLevels.getPlayerAccountLevel(Integer.parseInt(bankLevelLabel.getText().toString().replaceAll("[^0-9]", "")));

        // 存入数额输入框
        WTextField depositField = new WTextField() {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(100, 20);  // 强制按钮大小为100x20
            }
        };
        bankPane.add(depositField, DEPOSIT_TEXT_FIELD_X, DEPOSIT_TEXT_FIELD_Y);
        // 创建存入铜币按钮
        WButton depositButtonByCopperCoin = new WButton(Text.translatable("gui.home_interface.bank.deposit_copper_coin_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        depositButtonByCopperCoin.setOnClick(() -> {
            if (!(depositField.getText().isEmpty()) && Integer.parseInt(depositField.getText()) > 0) {
                System.out.println("Deposit Copper");
                int count = Integer.parseInt(depositField.getText());
                if (hasEnoughItems(playerInventory, new ItemStack(ModItems.COPPER_COIN), count)) {
                    if (Integer.parseInt(COPPER_COIN_COUNT_LABEL.getText().getString()) + count <= accountLevel.getMaxCopperCoins()) {
                        removeItemsFromPlayerInventory(playerInventory, new ItemStack(ModItems.COPPER_COIN), count);
                        sendBankOperation(DEPOSIT, COPPER_COIN, count);
                        player.sendMessage(Text.translatable("gui.home_interface.bank.deposit_copper_coins", count), false);
                    } else {
                        player.sendMessage(Text.translatable("gui.home_interface.bank.deposit_copper_coins", accountLevel.getMaxCopperCoins()), false);
                    }
                }
                requestBalance();  // 请求余额
            } else {
                player.sendMessage(Text.translatable("gui.home_interface.bank.text_field.is_empty"), false);
            }
            depositField.setText("");
        });
        bankPane.add(depositButtonByCopperCoin, DEPOSIT_COPPER_COIN_BUTTON_X, DEPOSIT_COPPER_COIN_BUTTON_Y);
        // 创建存入银币按钮
        WButton depositButtonBySilverCoin = new WButton(Text.translatable("gui.home_interface.bank.deposit_silver_coin_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        depositButtonBySilverCoin.setOnClick(() -> {
            if (!(depositField.getText().isEmpty()) && Integer.parseInt(depositField.getText()) > 0) {
                System.out.println("Deposit Silver");
                int count = Integer.parseInt(depositField.getText());
                if (hasEnoughItems(playerInventory, new ItemStack(ModItems.SILVER_COIN), count)) {
                    if (Integer.parseInt(SILVER_COIN_COUNT_LABEL.getText().getString()) + count <= accountLevel.getMaxSilverCoins()) {
                        removeItemsFromPlayerInventory(playerInventory, new ItemStack(ModItems.SILVER_COIN), count);
                        sendBankOperation(DEPOSIT, SILVER_COIN, count);
                        player.sendMessage(Text.translatable("gui.home_interface.bank.deposit_silver_coins", count), false);
                    } else {
                        player.sendMessage(Text.translatable("gui.home_interface.bank.deposit_silver_coins.max_deposit", accountLevel.getMaxSilverCoins()), false);
                    }
                }
                requestBalance();  // 请求余额
            } else {
                player.sendMessage(Text.translatable("gui.home_interface.bank.text_field.is_empty"), false);
            }
            depositField.setText("");
        });
        bankPane.add(depositButtonBySilverCoin, DEPOSIT_SILVER_COIN_BUTTON_X, DEPOSIT_SILVER_COIN_BUTTON_Y);
        // 创建存入金币按钮
        WButton depositButtonByGoldCoin = new WButton(Text.translatable("gui.home_interface.bank.deposit_gold_coin_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        depositButtonByGoldCoin.setOnClick(() -> {
            if (!(depositField.getText().isEmpty()) && Integer.parseInt(depositField.getText()) > 0) {
                System.out.println("Deposit Gold");
                int count = Integer.parseInt(depositField.getText());
                if (hasEnoughItems(playerInventory, new ItemStack(ModItems.GOLD_COIN), count)) {
                    if (Integer.parseInt(GOLD_COIN_COUNT_LABEL.getText().getString()) + count <= accountLevel.getMaxGoldCoins()) {
                        removeItemsFromPlayerInventory(playerInventory, new ItemStack(ModItems.GOLD_COIN), count);
                        sendBankOperation(DEPOSIT, GOLD_COIN, count);
                        player.sendMessage(Text.translatable("gui.home_interface.bank.deposit_gold_coins", count), false);
                    } else {
                        player.sendMessage(Text.translatable("gui.home_interface.bank.deposit_gold_coins.max_deposit", accountLevel.getMaxGoldCoins()), false);
                    }
                }
                requestBalance();  // 请求余额
            } else {
                player.sendMessage(Text.translatable("gui.home_interface.bank.text_field.is_empty"), false);
            }
            depositField.setText("");
        });
        bankPane.add(depositButtonByGoldCoin, DEPOSIT_GOLD_COIN_BUTTON_X, DEPOSIT_GOLD_COIN_BUTTON_Y);

        // 取出数额输入框
        WTextField withdrawField = new WTextField() {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(100, 20);  // 强制按钮大小为100x20
            }
        };
        bankPane.add(withdrawField, WITHDRAW_TEXT_FIELD_X, WITHDRAW_TEXT_FIELD_Y);
        // 创建取出铜币按钮
        WButton withdrawButtonByCopperCoin = new WButton(Text.translatable("gui.home_interface.bank.withdraw_copper_coin_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        withdrawButtonByCopperCoin.setOnClick(() -> {
            if (!(withdrawField.getText().isEmpty()) && Integer.parseInt(withdrawField.getText()) > 0) {
                System.out.println("Withdraw Copper");
                int count = Integer.parseInt(withdrawField.getText());
                if (hasEnoughSpace(playerInventory, new ItemStack(ModItems.COPPER_COIN), count)) {
                    addItemsToPlayerInventory(player, new ItemStack(ModItems.COPPER_COIN), count);
                    sendBankOperation(WITHDRAW, COPPER_COIN, count);
                    player.sendMessage(Text.translatable("gui.home_interface.bank.withdraw_copper_coins", count), false);
                }
                requestBalance();  // 请求余额
            } else {
                player.sendMessage(Text.translatable("gui.home_interface.bank.text_field.is_empty"), false);
            }
            withdrawField.setText("");
        });
        bankPane.add(withdrawButtonByCopperCoin, WITHDRAW_COPPER_COIN_BUTTON_X, WITHDRAW_COPPER_COIN_BUTTON_Y);
        // 创建取出银币按钮
        WButton withdrawButtonBySilverCoin = new WButton(Text.translatable("gui.home_interface.bank.withdraw_silver_coin_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        withdrawButtonBySilverCoin.setOnClick(() -> {
            if (!(withdrawField.getText().isEmpty()) && Integer.parseInt(withdrawField.getText()) > 0) {
                System.out.println("Withdraw Silver");
                int count = Integer.parseInt(withdrawField.getText());
                if (hasEnoughSpace(playerInventory, new ItemStack(ModItems.SILVER_COIN), count)) {
                    addItemsToPlayerInventory(player, new ItemStack(ModItems.SILVER_COIN), count);
                    sendBankOperation(WITHDRAW, SILVER_COIN, count);
                    player.sendMessage(Text.translatable("gui.home_interface.bank.withdraw_silver_coins", count), false);
                }
                requestBalance();  // 请求余额
            } else {
                player.sendMessage(Text.translatable("gui.home_interface.bank.text_field.is_empty"), false);
            }
            withdrawField.setText("");
        });
        bankPane.add(withdrawButtonBySilverCoin, WITHDRAW_SILVER_COIN_BUTTON_X, WITHDRAW_SILVER_COIN_BUTTON_Y);
        // 创建取出金币按钮
        WButton withdrawButtonByGoldCoin = new WButton(Text.translatable("gui.home_interface.bank.withdraw_gold_coin_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        withdrawButtonByGoldCoin.setOnClick(() -> {
            if (!(withdrawField.getText().isEmpty()) && Integer.parseInt(withdrawField.getText()) > 0) {
                System.out.println("Withdraw Gold");
                int count = Integer.parseInt(withdrawField.getText());
                if (hasEnoughSpace(playerInventory, new ItemStack(ModItems.GOLD_COIN), count)) {
                    addItemsToPlayerInventory(player, new ItemStack(ModItems.GOLD_COIN), count);
                    sendBankOperation(WITHDRAW, GOLD_COIN, count);
                    player.sendMessage(Text.translatable("gui.home_interface.bank.withdraw_gold_coins", count), false);
                }
                requestBalance();  // 请求余额
            } else {
                player.sendMessage(Text.translatable("gui.home_interface.bank.text_field.is_empty"), false);
            }
            withdrawField.setText("");
        });
        bankPane.add(withdrawButtonByGoldCoin, WITHDRAW_GOLD_COIN_BUTTON_X, WITHDRAW_GOLD_COIN_BUTTON_Y);

        return bankPane;
    }
}
*/
