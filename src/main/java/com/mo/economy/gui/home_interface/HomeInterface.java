package com.mo.economy.gui.home_interface;

import com.mo.economy.MainForServer;
import com.mo.economy.gui.ScreenHandlers;
import com.mo.economy.item.ModItems;
import com.mo.economy.network.server.RequestBalancePacket;
import com.mo.economy.network.server.RequestBankLevelPacket;
import com.mo.economy.new_economy_system.bank.AccountLevel;
import com.mo.economy.new_economy_system.bank.AccountLevels;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class HomeInterface extends SyncedGuiDescription {
    private static final Identifier BANK_OPERATION_PACKET_ID = new Identifier(MainForServer.MOD_ID, "bank_operation");

    private static final String DEPOSIT = "deposit";
    private static final String WITHDRAW = "withdraw";
    private static final String COPPER_COIN = "copper_coin";
    private static final String SILVER_COIN = "silver_coin";
    private static final String GOLD_COIN = "gold_coin";

    private static final int BANK_ICON_X = 170;
    private static final int BANK_ICON_Y = 60;
    private static final int BANK_BUTTON_X = BANK_ICON_X + 30;
    private static final int BANK_BUTTON_Y = BANK_ICON_Y;

    private static final int OFFICIAL_STORE_ICON_X = BANK_ICON_X;
    private static final int OFFICIAL_STORE_ICON_Y = BANK_ICON_Y + 30;
    private static final int OFFICIAL_STORE_BUTTON_X = BANK_BUTTON_X;
    private static final int OFFICIAL_STORE_BUTTON_Y = OFFICIAL_STORE_ICON_Y;

    private static final int PLAYER_MARKET_ICON_X = BANK_ICON_X;
    private static final int PLAYER_MARKET_ICON_Y = OFFICIAL_STORE_ICON_Y + 30;
    private static final int PLAYER_MARKET_BUTTON_X = BANK_BUTTON_X;
    private static final int PLAYER_MARKET_BUTTON_Y = PLAYER_MARKET_ICON_Y;

    public static WLabel bankLevelLabel;
    private static AccountLevel accountLevel;
    private static final int BACK_LEVEL_LABEL_X = 130;
    private static final int BACK_LEVEL_LABEL_Y = 40;

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

    private static WLabel COPPER_COIN_COUNT_LABEL;
    private static WLabel SILVER_COIN_COUNT_LABEL;
    private static WLabel GOLD_COIN_COUNT_LABEL;
    private static int COPPER_COIN_COUNT;
    private static int SILVER_COIN_COUNT;
    private static int GOLD_COIN_COUNT;

    public HomeInterface(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ScreenHandlers.HOME_INTERFACE_SCREEN_HANDLER, syncId, playerInventory);

        // 通过 playerInventory 获取当前打开 GUI 的玩家实例
        PlayerEntity player = playerInventory.player;

        WPlainPanel homePane = new WPlainPanel();
        setRootPanel(homePane);
        homePane.setSize(500, 300);  // 设置面板大小

        // 创建一个标签
        WLabel label = new WLabel(Text.translatable("gui.home_interface.test_label"));
        homePane.add(label, 50, 20);  // 将标签添加到指定坐标 (50, 20)

        // =====================================================================================================

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


        // 创建银行按钮
        WButton bankButton = new WButton(Text.translatable("gui.home_interface.bank_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(100, 20);  // 强制按钮大小为100x20
            }
        };
        // 添加按钮点击事件
        bankButton.setOnClick(() -> {
            // 获取当前的 Screen 实例并更新标题
            if (MinecraftClient.getInstance().currentScreen instanceof HomeInterfaceScreen) {
                HomeInterfaceScreen currentScreen = (HomeInterfaceScreen) MinecraftClient.getInstance().currentScreen;

                // 更新标题为新的文本
                currentScreen.updateTitle(Text.translatable("gui.home_interface.bank_title"));
            }
            setRootPanel(bankPane);
            bankPane.validate(this);  // 重新验证新面板布局

            // 强制刷新当前的屏幕
            MinecraftClient.getInstance().setScreen(MinecraftClient.getInstance().currentScreen);
        });
        homePane.add(bankButton, BANK_BUTTON_X, BANK_BUTTON_Y);

        // 创建一个 WItem 组件用于渲染物品
        WItem bankIcon = new WItem(new ItemStack(ModItems.BANK_ICON));  // 创建 WItem，用于显示物品
        homePane.add(bankIcon, BANK_ICON_X, BANK_ICON_Y);

        // =====================================================================================================

        // 创建一个按钮
        WButton officialStoreButton = new WButton(Text.translatable("gui.home_interface.official_store_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(100, 20);  // 强制按钮大小为100x20
            }
        };
        // 添加按钮点击事件
        officialStoreButton.setOnClick(() -> {


        });
        homePane.add(officialStoreButton, OFFICIAL_STORE_BUTTON_X, OFFICIAL_STORE_BUTTON_Y);

        // 创建一个 WItem 组件用于渲染物品
        WItem systemStoreIcon = new WItem(new ItemStack(ModItems.SYSTEM_STORE_ICON));  // 创建 WItem，用于显示物品
        homePane.add(systemStoreIcon, OFFICIAL_STORE_ICON_X, OFFICIAL_STORE_ICON_Y);

        // =====================================================================================================

        // 创建一个按钮
        WButton playerMarketButton = new WButton(Text.translatable("gui.home_interface.player_market_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(100, 20);  // 强制按钮大小为100x20
            }
        };
        // 添加按钮点击事件
        playerMarketButton.setOnClick(() -> {


        });
        homePane.add(playerMarketButton, PLAYER_MARKET_BUTTON_X, PLAYER_MARKET_BUTTON_Y);

        // 创建一个 WItem 组件用于渲染物品
        WItem playerMarketIcon = new WItem(new ItemStack(ModItems.PLAYER_MARKET_ICON));  // 创建 WItem，用于显示物品
        homePane.add(playerMarketIcon, PLAYER_MARKET_ICON_X, PLAYER_MARKET_ICON_Y);


        /*// 添加一个用于渲染玩家模型的组件
        WPlayerModel playerModel = new WPlayerModel();
        root.add(playerModel, 250, 100);  // 在 (250, 100) 位置显示玩家模型*/


        homePane.validate(this);  // 验证布局


        // 延迟标题更新，确保 Screen 已加载
        MinecraftClient.getInstance().execute(() -> {
            if (MinecraftClient.getInstance().currentScreen instanceof HomeInterfaceScreen) {
                HomeInterfaceScreen currentScreen = (HomeInterfaceScreen) MinecraftClient.getInstance().currentScreen;
                // 更新标题为新的文本
                currentScreen.updateTitle(Text.translatable("gui.home_interface.main_title", player.getName().getString()));
            }
        });
    }

    // 自定义的 WWidget 用于渲染玩家模型
    private static class WPlayerModel extends WWidget {

        @Override
        public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
            // 获取 Minecraft 实例中的当前玩家
            MinecraftClient mc = MinecraftClient.getInstance();
            AbstractClientPlayerEntity playerEntity = (AbstractClientPlayerEntity) mc.player;

            // 渲染玩家模型
            renderPlayerModel(x + 50, y + 75, 30, mouseX - (x + 50), mouseY - (y + 75), playerEntity);
        }

        // 仿照 InventoryScreen 渲染玩家模型
        private void renderPlayerModel(int posX, int posY, int scale, float mouseX, float mouseY, AbstractClientPlayerEntity playerEntity) {
            MatrixStack matrixStack = new MatrixStack();
            matrixStack.push();
            matrixStack.translate(posX, posY, 1050.0F);  // 设定渲染位置
            matrixStack.scale(scale, scale, scale);  // 缩放模型大小
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));  // 旋转修正倒立问题

            // 设定头部的旋转角度
            float bodyYaw = (float) Math.atan(mouseX / 40.0F) * 20.0F;
            float headYaw = (float) Math.atan(mouseX / 40.0F) * 40.0F;
            float pitch = (float) Math.atan(mouseY / 40.0F) * 20.0F;

            // 设置旋转状态
            playerEntity.bodyYaw = bodyYaw;
            playerEntity.setYaw(headYaw);
            playerEntity.setPitch(pitch);
            playerEntity.prevHeadYaw = headYaw;
            playerEntity.headYaw = headYaw;

            // 禁用显示手臂移动
            playerEntity.handSwingProgress = 0.0F;

            // 渲染玩家模型
            MinecraftClient.getInstance().getEntityRenderDispatcher().render(
                    playerEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixStack,
                    MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers(), 15728880);

            // 恢复原始状态
            matrixStack.pop();
        }

        @Override
        public int getWidth() {
            return 100;  // 设定模型的显示区域宽度
        }

        @Override
        public int getHeight() {
            return 100;  // 设定模型的显示区域高度
        }
    }

    public static void updateMoney(int[] balances) {
        System.out.println("Update Player Money");

        HomeInterface.COPPER_COIN_COUNT_LABEL.setText(Text.literal(String.valueOf(balances[0])));
        HomeInterface.SILVER_COIN_COUNT_LABEL.setText(Text.literal(String.valueOf(balances[1])));
        HomeInterface.GOLD_COIN_COUNT_LABEL.setText(Text.literal(String.valueOf(balances[2])));
    }

    // 在玩家点击存款或取款时调用这个方法
    public static void sendBankOperation(String operationType, String coinType, int amount) {
        System.out.println("sendBankOperation");
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(operationType);
        buf.writeString(coinType);
        buf.writeInt(amount);

        // 发送到服务端
        ClientPlayNetworking.send(BANK_OPERATION_PACKET_ID, buf);
    }

    public static void requestBalance() {
        // 发送请求余额的数据包到服务端
        ClientPlayNetworking.send(RequestBalancePacket.ID, PacketByteBufs.create());
    }

    public static boolean hasEnoughItems(PlayerInventory inventory, ItemStack requiredItem, int requiredAmount) {
        int itemCount = 0;

        // 遍历玩家背包的所有格子
        for (ItemStack stack : inventory.main) {
            if (ItemStack.areItemsEqual(stack, requiredItem)) {
                itemCount += stack.getCount();  // 统计物品数量
            }
            if (itemCount >= requiredAmount) {
                return true;  // 如果数量足够，返回 true
            }
        }

        inventory.player.sendMessage(Text.translatable("gui.home_interface.bank.not_enough_items"), false);
        return false;  // 如果遍历完背包后数量不足，返回 false
    }

    public void removeItemsFromPlayerInventory(PlayerInventory inventory, ItemStack itemToRemove, int amountToRemove) {
        int remainingToRemove = amountToRemove;

        // 遍历玩家背包的所有格子
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);

            if (ItemStack.areItemsEqual(stack, itemToRemove)) {
                if (stack.getCount() <= remainingToRemove) {
                    // 如果当前格子中的物品数量小于等于需要移除的数量，清空该格子
                    remainingToRemove -= stack.getCount();
                    inventory.setStack(i, ItemStack.EMPTY);
                } else {
                    // 如果当前格子中的物品数量大于需要移除的数量，只减少相应数量
                    stack.decrement(remainingToRemove);
                    remainingToRemove = 0;
                    break;
                }
            }
        }
    }

    public boolean hasEnoughSpace(PlayerInventory inventory, ItemStack itemToStore, int requiredAmount) {
        int remainingAmount = requiredAmount;

        // 遍历背包，检查是否有空格或是否可以堆叠物品
        for (ItemStack stack : inventory.main) {
            // 如果该格子为空，则可以存放物品
            if (stack.isEmpty()) {
                return true;  // 有空格存放物品
            }

            // 如果该格子中的物品可以与新物品堆叠
            if (ItemStack.areItemsEqual(stack, itemToStore) && stack.getCount() < stack.getMaxCount()) {
                int availableSpace = stack.getMaxCount() - stack.getCount();
                remainingAmount -= availableSpace;

                // 如果剩余的数量已经可以放入背包，则返回 true
                if (remainingAmount <= 0) {
                    return true;
                }
            }
        }

        inventory.player.sendMessage(Text.translatable("gui.home_interface.bank.not_enough_space"), false);
        return false;  // 没有足够空间存放物品
    }

    public void addItemsToPlayerInventory(PlayerEntity player, ItemStack itemToAdd, int amountToAdd) {
        PlayerInventory inventory = player.getInventory();
        int remainingToAdd = amountToAdd;

        // 遍历背包，先尝试将物品堆叠到已有的相同物品上
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);

            if (ItemStack.areItemsEqual(stack, itemToAdd) && stack.getCount() < stack.getMaxCount()) {
                int spaceAvailable = stack.getMaxCount() - stack.getCount();
                int toAdd = Math.min(spaceAvailable, remainingToAdd);

                stack.increment(toAdd);
                remainingToAdd -= toAdd;

                if (remainingToAdd <= 0) {
                    return; // 已经添加完所有物品
                }
            }
        }

        // 如果还有剩余的物品没有添加，寻找空格添加
        while (remainingToAdd > 0) {
            int toAdd = Math.min(itemToAdd.getMaxCount(), remainingToAdd);

            ItemStack newStack = itemToAdd.copy();
            newStack.setCount(toAdd);
            if (!inventory.insertStack(newStack)) {
                // 如果背包已满，物品将无法插入
                System.out.println("背包已满，无法添加更多物品");
                break;
            }
            remainingToAdd -= toAdd;
        }
    }

    // 客户端发送查询银行等级的请求
    public static void requestBankLevel() {
        PacketByteBuf buf = PacketByteBufs.create();
        ClientPlayNetworking.send(RequestBankLevelPacket.ID, buf);
    }
}
