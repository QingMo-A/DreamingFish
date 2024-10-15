package com.mo.economy.gui.home_interface;

import com.mo.economy.item.ModItems;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class BankInterface {
    // 银行面板
    private WPlainPanel bankPane;
    // 银行等级标签
    private WLabel bankLevelLabel;
    // 银行等级标签的X坐标
    private final int BACK_LEVEL_LABEL_X = 150;
    // 银行等级标签的Y坐标
    private final int BACK_LEVEL_LABEL_Y = 40;
    // 铜币图标
    private WItem copperCoinIcon;
    // 铜币图标的X坐标
    private final int COPPER_COIN_ICON_X = BACK_LEVEL_LABEL_X + 70;
    // 铜币图标的Y坐标
    private final int COPPER_COIN_ICON_Y = BACK_LEVEL_LABEL_Y;
    // 铜币数量标签
    private WLabel copperCoinCountLabel;
    // 铜币数量标签的X坐标
    private final int COPPER_COIN_COUNT_X = COPPER_COIN_ICON_X + 20;
    // 银币图标
    private WItem silverCoinIcon;
    // 银币图标的X坐标
    private final int SILVER_COIN_ICON_X = COPPER_COIN_COUNT_X + 20;
    // 银币数量标签
    private WLabel silverCoinCountLabel;
    // 银币数量标签的X坐标
    private final int SILVER_COIN_COUNT_X = SILVER_COIN_ICON_X + 20;
    // 金币图标
    private WItem goldCoinIcon;
    // 金币图标的X坐标
    private final int GOLD_COIN_ICON_X = SILVER_COIN_COUNT_X + 20;
    // 金币数量标签
    private WLabel goldCoinCountLabel;
    // 金币数量标签的X坐标
    private final int GOLD_COIN_COUNT_X = GOLD_COIN_ICON_X + 20;
    // 存款输入框
    private WTextField depositField;
    // 存款输入框的X坐标
    private final int DEPOSIT_TEXT_FIELD_X = 85;
    // 存款输入框的Y坐标
    private final int DEPOSIT_TEXT_FIELD_Y = 70;
    // 存入铜币按钮
    private WButton depositButtonByCopperCoin;
    // 存入铜币按钮的X坐标
    private final int DEPOSIT_COPPER_COIN_BUTTON_X = DEPOSIT_TEXT_FIELD_X + 105;
    // 存入铜币按钮的Y坐标
    private final int DEPOSIT_COPPER_COIN_BUTTON_Y = DEPOSIT_TEXT_FIELD_Y;
    // 存入银币按钮
    private WButton depositButtonBySilverCoin;
    // 存入银币按钮的X坐标
    private final int DEPOSIT_SILVER_COIN_BUTTON_X = DEPOSIT_COPPER_COIN_BUTTON_X + 75;
    // 存入银币按钮的Y坐标
    private final int DEPOSIT_SILVER_COIN_BUTTON_Y = DEPOSIT_COPPER_COIN_BUTTON_Y;
    // 存入金币按钮
    private WButton depositButtonByGoldCoin;
    // 存入金币按钮的X坐标
    private final int DEPOSIT_GOLD_COIN_BUTTON_X = DEPOSIT_SILVER_COIN_BUTTON_X + 75;
    // 存入金币按钮的Y坐标
    private final int DEPOSIT_GOLD_COIN_BUTTON_Y = DEPOSIT_COPPER_COIN_BUTTON_Y;
    // 取款输入框
    private WTextField withdrawField;
    // 取款输入框的X坐标
    private final int WITHDRAW_TEXT_FIELD_X = DEPOSIT_TEXT_FIELD_X;
    // 取款输入框的Y坐标
    private final int WITHDRAW_TEXT_FIELD_Y = DEPOSIT_TEXT_FIELD_Y + 30;
    // 取出铜币按钮
    private WButton withdrawButtonByCopperCoin;
    // 取出铜币按钮的X坐标
    private final int WITHDRAW_COPPER_COIN_BUTTON_X = DEPOSIT_COPPER_COIN_BUTTON_X;
    // 取出铜币按钮的Y坐标
    private final int WITHDRAW_COPPER_COIN_BUTTON_Y = DEPOSIT_COPPER_COIN_BUTTON_Y + 30;
    // 取出银币按钮
    private WButton withdrawButtonBySilverCoin;
    // 取出银币按钮的X坐标
    private final int WITHDRAW_SILVER_COIN_BUTTON_X = DEPOSIT_SILVER_COIN_BUTTON_X;
    // 取出银币按钮的Y坐标
    private final int WITHDRAW_SILVER_COIN_BUTTON_Y = WITHDRAW_COPPER_COIN_BUTTON_Y;
    // 取出金币按钮
    private WButton withdrawButtonByGoldCoin;
    // 取出金币按钮的X坐标
    private final int WITHDRAW_GOLD_COIN_BUTTON_X = DEPOSIT_GOLD_COIN_BUTTON_X;
    // 取出金币按钮的Y坐标
    private final int WITHDRAW_GOLD_COIN_BUTTON_Y = WITHDRAW_COPPER_COIN_BUTTON_Y;

    private final String DEPOSIT = "deposit";
    private final String WITHDRAW = "withdraw";
    private final String COPPER_COIN = "copper_coin";
    private final String SILVER_COIN = "silver_coin";
    private final String GOLD_COIN = "gold_coin";

    public BankInterface() {
        // 创建银行面板
        bankPane = new WPlainPanel();
        // 设置面板大小
        bankPane.setSize(500, 300);

        // 银行等级标签
        bankLevelLabel = new WLabel(Text.literal("银行账户等级: null"));
        // 设置标签位置
        bankPane.add(bankLevelLabel, BACK_LEVEL_LABEL_X, BACK_LEVEL_LABEL_Y);

        // 铜币图标
        copperCoinIcon = new WItem(new ItemStack(ModItems.COPPER_COIN));
        // 设置图标位置
        bankPane.add(copperCoinIcon, COPPER_COIN_ICON_X, COPPER_COIN_ICON_Y);

        copperCoinCountLabel = new WLabel(Text.literal("null"));
        bankPane.add(copperCoinCountLabel, COPPER_COIN_COUNT_X, COPPER_COIN_ICON_Y);

        silverCoinIcon = new WItem(new ItemStack(ModItems.SILVER_COIN));
        bankPane.add(silverCoinIcon, SILVER_COIN_ICON_X, COPPER_COIN_ICON_Y);

        silverCoinCountLabel = new WLabel(Text.literal("null"));
        bankPane.add(silverCoinCountLabel, SILVER_COIN_COUNT_X, COPPER_COIN_ICON_Y);

        goldCoinIcon = new WItem(new ItemStack(ModItems.GOLD_COIN));
        bankPane.add(goldCoinIcon, GOLD_COIN_ICON_X, COPPER_COIN_ICON_Y);

        goldCoinCountLabel = new WLabel(Text.literal("null"));
        bankPane.add(goldCoinCountLabel, GOLD_COIN_COUNT_X, COPPER_COIN_ICON_Y);

        depositField = new WTextField() {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(100, 20);  // 强制按钮大小为100x20
            }
        };
        bankPane.add(depositField, DEPOSIT_TEXT_FIELD_X, DEPOSIT_TEXT_FIELD_Y);

        // 创建存入铜币按钮
        depositButtonByCopperCoin = new WButton(Text.translatable("gui.home_interface.bank.deposit_copper_coin_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        bankPane.add(depositButtonByCopperCoin, DEPOSIT_COPPER_COIN_BUTTON_X, DEPOSIT_COPPER_COIN_BUTTON_Y);

        // 创建存入银币按钮
        depositButtonBySilverCoin = new WButton(Text.translatable("gui.home_interface.bank.deposit_silver_coin_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        bankPane.add(depositButtonBySilverCoin, DEPOSIT_SILVER_COIN_BUTTON_X, DEPOSIT_SILVER_COIN_BUTTON_Y);

        // 创建存入金币按钮
        depositButtonByGoldCoin = new WButton(Text.translatable("gui.home_interface.bank.deposit_gold_coin_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        bankPane.add(depositButtonByGoldCoin, DEPOSIT_GOLD_COIN_BUTTON_X, DEPOSIT_GOLD_COIN_BUTTON_Y);

        withdrawField = new WTextField() {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(100, 20);  // 强制按钮大小为100x20
            }
        };
        bankPane.add(withdrawField, WITHDRAW_TEXT_FIELD_X, WITHDRAW_TEXT_FIELD_Y);

        // 创建取出铜币按钮
        withdrawButtonByCopperCoin = new WButton(Text.translatable("gui.home_interface.bank.withdraw_copper_coin_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        bankPane.add(withdrawButtonByCopperCoin, WITHDRAW_COPPER_COIN_BUTTON_X, WITHDRAW_COPPER_COIN_BUTTON_Y);

        // 创建取出银币按钮
        withdrawButtonBySilverCoin = new WButton(Text.translatable("gui.home_interface.bank.withdraw_silver_coin_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        bankPane.add(withdrawButtonBySilverCoin, WITHDRAW_SILVER_COIN_BUTTON_X, WITHDRAW_SILVER_COIN_BUTTON_Y);

        // 创建取出金币按钮
        withdrawButtonByGoldCoin = new WButton(Text.translatable("gui.home_interface.bank.withdraw_gold_coin_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        bankPane.add(withdrawButtonByGoldCoin, WITHDRAW_GOLD_COIN_BUTTON_X, WITHDRAW_GOLD_COIN_BUTTON_Y);


    }

    public WPlainPanel getBankPane() {
        return bankPane;
    }

    public void setBankPane(WPlainPanel bankPane) {
        this.bankPane = bankPane;
    }

    public WLabel getBankLevelLabel() {
        return bankLevelLabel;
    }

    public void setBankLevelLabel(WLabel bankLevelLabel) {
        this.bankLevelLabel = bankLevelLabel;
    }

    public WItem getCopperCoinIcon() {
        return copperCoinIcon;
    }

    public void setCopperCoinIcon(WItem copperCoinIcon) {
        this.copperCoinIcon = copperCoinIcon;
    }

    public WLabel getCopperCoinCountLabel() {
        return copperCoinCountLabel;
    }

    public void setCopperCoinCountLabel(WLabel copperCoinCountLabel) {
        this.copperCoinCountLabel = copperCoinCountLabel;
    }

    public WItem getSilverCoinIcon() {
        return silverCoinIcon;
    }

    public void setSilverCoinIcon(WItem silverCoinIcon) {
        this.silverCoinIcon = silverCoinIcon;
    }

    public WLabel getSilverCoinCountLabel() {
        return silverCoinCountLabel;
    }

    public void setSilverCoinCountLabel(WLabel silverCoinCountLabel) {
        this.silverCoinCountLabel = silverCoinCountLabel;
    }

    public WItem getGoldCoinIcon() {
        return goldCoinIcon;
    }

    public void setGoldCoinIcon(WItem goldCoinIcon) {
        this.goldCoinIcon = goldCoinIcon;
    }

    public WLabel getGoldCoinCountLabel() {
        return goldCoinCountLabel;
    }

    public void setGoldCoinCountLabel(WLabel goldCoinCountLabel) {
        this.goldCoinCountLabel = goldCoinCountLabel;
    }

    public WTextField getDepositField() {
        return depositField;
    }

    public void setDepositField(WTextField depositField) {
        this.depositField = depositField;
    }

    public WButton getDepositButtonByCopperCoin() {
        return depositButtonByCopperCoin;
    }

    public void setDepositButtonByCopperCoin(WButton depositButtonByCopperCoin) {
        this.depositButtonByCopperCoin = depositButtonByCopperCoin;
    }

    public WButton getDepositButtonBySilverCoin() {
        return depositButtonBySilverCoin;
    }

    public void setDepositButtonBySilverCoin(WButton depositButtonBySilverCoin) {
        this.depositButtonBySilverCoin = depositButtonBySilverCoin;
    }

    public WButton getDepositButtonByGoldCoin() {
        return depositButtonByGoldCoin;
    }

    public void setDepositButtonByGoldCoin(WButton depositButtonByGoldCoin) {
        this.depositButtonByGoldCoin = depositButtonByGoldCoin;
    }

    public WTextField getWithdrawField() {
        return withdrawField;
    }

    public void setWithdrawField(WTextField withdrawField) {
        this.withdrawField = withdrawField;
    }

    public WButton getWithdrawButtonByCopperCoin() {
        return withdrawButtonByCopperCoin;
    }

    public void setWithdrawButtonByCopperCoin(WButton withdrawButtonByCopperCoin) {
        this.withdrawButtonByCopperCoin = withdrawButtonByCopperCoin;
    }

    public WButton getWithdrawButtonBySilverCoin() {
        return withdrawButtonBySilverCoin;
    }

    public void setWithdrawButtonBySilverCoin(WButton withdrawButtonBySilverCoin) {
        this.withdrawButtonBySilverCoin = withdrawButtonBySilverCoin;
    }

    public WButton getWithdrawButtonByGoldCoin() {
        return withdrawButtonByGoldCoin;
    }

    public void setWithdrawButtonByGoldCoin(WButton withdrawButtonByGoldCoin) {
        this.withdrawButtonByGoldCoin = withdrawButtonByGoldCoin;
    }

    public String getDEPOSIT() {
        return DEPOSIT;
    }

    public String getWITHDRAW() {
        return WITHDRAW;
    }

    public String getCOPPER_COIN() {
        return COPPER_COIN;
    }

    public String getSILVER_COIN() {
        return SILVER_COIN;
    }

    public String getGOLD_COIN() {
        return GOLD_COIN;
    }

    public void updateBalance(int[] balances) {
        System.out.println("UpdateBalance!!!");
        copperCoinCountLabel.setText(Text.literal(String.valueOf(balances[0])));
        silverCoinCountLabel.setText(Text.literal(String.valueOf(balances[1])));
        goldCoinCountLabel.setText(Text.literal(String.valueOf(balances[2])));
    }

    public void updateBankLevel(int bankLevel) {
        System.out.println("UpdateBankLevel!!!");
        bankLevelLabel.setText(Text.literal("银行账户等级: " + bankLevel));
    }
}
