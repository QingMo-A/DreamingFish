package com.mo.economy.gui.home_interface;

import com.mo.economy.MainForServer;
import com.mo.economy.gui.ScreenHandlers;
import com.mo.economy.item.ModItems;
import com.mo.economy.network.client.ListItemPacket;
import com.mo.economy.network.client.RemoveListedItemPacket;
import com.mo.economy.network.server.*;
import com.mo.economy.new_economy_system.bank.AccountLevel;
import com.mo.economy.new_economy_system.bank.AccountLevels;
import com.mo.economy.new_economy_system.player_market.ListedItem;
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
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import java.util.List;
import java.util.UUID;

public class HomeInterface extends SyncedGuiDescription {
    private final BankInterface bankInterface = new BankInterface();
    private final MarketInterface marketInterface = new MarketInterface();
    private final ListedItemInterface listedItemInterface = new ListedItemInterface();

    private PlayerEntity player;
    private PlayerInventory playerInventory;

    private static final Identifier BANK_OPERATION_PACKET_ID = new Identifier(MainForServer.MOD_ID, "bank_operation");

    private WButton bankButton;
    private WButton officialStoreButton;
    private WButton playerMarketButton;
    private WButton myListedItemButton;

    // 170
    private static final int BANK_ICON_X = 130;
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

    private static final int MY_LISTED_ITEM_ICON_X = BANK_ICON_X;
    private static final int MY_LISTED_ITEM_ICON_Y = PLAYER_MARKET_ICON_Y + 30;
    private static final int MY_LISTED_ITEM_BUTTON_X = BANK_BUTTON_X;
    private static final int MY_LISTED_ITEM_BUTTON_Y = MY_LISTED_ITEM_ICON_Y;

    private AccountLevel accountLevel;

    private final int itemsPerPage = 9;  // 每页显示的商品数量
    private int page = 1;
    private int totalItems;
    private int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / itemsPerPage));

    private List<ListedItem> marketItems;
    private List<ListedItem> marketItemsBySearching;

    public HomeInterface(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ScreenHandlers.HOME_INTERFACE_SCREEN_HANDLER, syncId, playerInventory);
        HomeInterfaceManager.setPlayerInterface(playerInventory.player.getUuid(), this);


        // 通过 playerInventory 获取当前打开 GUI 的玩家实例
        setPlayerInventory(playerInventory);
        setPlayer(playerInventory.player);

        WPlainPanel homePane = new WPlainPanel();
        setRootPanel(homePane);
        homePane.setSize(500, 300);  // 设置面板

        // 创建一个标签
        WLabel label = new WLabel(Text.translatable("gui.home_interface.test_label"));
        homePane.add(label, 50, 20);  // 将标签添加到指定坐标 (50, 20)

        // =====================================================================================================

        requestBalance();  // 请求余额
        requestBankLevel(); // 请求银行等级

        // 创建银行按钮
        bankButton = new WButton(Text.translatable("gui.home_interface.bank_button")) {
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
            setRootPanel(bankInterface.getBankPane());
            bankInterface.getBankPane().validate(this);  // 重新验证新面板布局

            // 强制刷新当前的屏幕
            MinecraftClient.getInstance().setScreen(MinecraftClient.getInstance().currentScreen);

            accountLevel = AccountLevels.getPlayerAccountLevel(Integer.parseInt(bankInterface.getBankLevelLabel().getText().toString().replaceAll("[^0-9]", "")));

            bankInterface.getDepositButtonByCopperCoin().setOnClick(() -> {
                if (!(bankInterface.getDepositField().getText().isEmpty()) && Integer.parseInt(bankInterface.getDepositField().getText()) > 0) {
                    System.out.println("Deposit Copper");
                    int count = Integer.parseInt(bankInterface.getDepositField().getText());
                    if (hasEnoughItems(playerInventory, new ItemStack(ModItems.COPPER_COIN), count)) {
                        if (Integer.parseInt(bankInterface.getCopperCoinCountLabel().getText().getString()) + count <= accountLevel.getMaxCopperCoins()) {
                            removeItemsFromPlayerInventory(playerInventory, new ItemStack(ModItems.COPPER_COIN), count);
                            sendBankOperation(bankInterface.getDEPOSIT(), bankInterface.getCOPPER_COIN(), count);
                            player.sendMessage(Text.translatable("gui.home_interface.bank.deposit_copper_coins", count), false);
                        } else {
                            player.sendMessage(Text.translatable("gui.home_interface.bank.deposit_copper_coins", accountLevel.getMaxCopperCoins()), false);
                        }
                    }
                    requestBalance();  // 请求余额
                } else {
                    player.sendMessage(Text.translatable("gui.home_interface.bank.text_field.is_empty"), false);
                }
                bankInterface.getDepositField().setText("");
            });
            bankInterface.getDepositButtonBySilverCoin().setOnClick(() -> {
                if (!(bankInterface.getDepositField().getText().isEmpty()) && Integer.parseInt(bankInterface.getDepositField().getText()) > 0) {
                    System.out.println("Deposit Silver");
                    int count = Integer.parseInt(bankInterface.getDepositField().getText());
                    if (hasEnoughItems(playerInventory, new ItemStack(ModItems.SILVER_COIN), count)) {
                        if (Integer.parseInt(bankInterface.getSilverCoinCountLabel().getText().getString()) + count <= accountLevel.getMaxSilverCoins()) {
                            removeItemsFromPlayerInventory(playerInventory, new ItemStack(ModItems.SILVER_COIN), count);
                            sendBankOperation(bankInterface.getDEPOSIT(), bankInterface.getSILVER_COIN(), count);
                            player.sendMessage(Text.translatable("gui.home_interface.bank.deposit_silver_coins", count), false);
                        } else {
                            player.sendMessage(Text.translatable("gui.home_interface.bank.deposit_silver_coins.max_deposit", accountLevel.getMaxSilverCoins()), false);
                        }
                    }
                    requestBalance();  // 请求余额
                } else {
                    player.sendMessage(Text.translatable("gui.home_interface.bank.text_field.is_empty"), false);
                }
                bankInterface.getDepositField().setText("");
            });
            bankInterface.getDepositButtonByGoldCoin().setOnClick(() -> {
                if (!(bankInterface.getDepositField().getText().isEmpty()) && Integer.parseInt(bankInterface.getDepositField().getText()) > 0) {
                    System.out.println("Deposit Gold");
                    int count = Integer.parseInt(bankInterface.getDepositField().getText());
                    if (hasEnoughItems(playerInventory, new ItemStack(ModItems.GOLD_COIN), count)) {
                        if (Integer.parseInt(bankInterface.getGoldCoinCountLabel().getText().getString()) + count <= accountLevel.getMaxGoldCoins()) {
                            removeItemsFromPlayerInventory(playerInventory, new ItemStack(ModItems.GOLD_COIN), count);
                            sendBankOperation(bankInterface.getDEPOSIT(), bankInterface.getGOLD_COIN(), count);
                            player.sendMessage(Text.translatable("gui.home_interface.bank.deposit_gold_coins", count), false);
                        } else {
                            player.sendMessage(Text.translatable("gui.home_interface.bank.deposit_gold_coins.max_deposit", accountLevel.getMaxGoldCoins()), false);
                        }
                    }
                    requestBalance();  // 请求余额
                } else {
                    player.sendMessage(Text.translatable("gui.home_interface.bank.text_field.is_empty"), false);
                }
                bankInterface.getDepositField().setText("");
            });

            bankInterface.getWithdrawButtonByCopperCoin().setOnClick(() -> {
                if (!(bankInterface.getWithdrawField().getText().isEmpty()) && Integer.parseInt(bankInterface.getWithdrawField().getText()) > 0) {
                    System.out.println("Withdraw Copper");
                    int count = Integer.parseInt(bankInterface.getWithdrawField().getText());
                    if (hasEnoughSpace(playerInventory, new ItemStack(ModItems.COPPER_COIN), count)) {
                        addItemsToPlayerInventory(player, new ItemStack(ModItems.COPPER_COIN), count);
                        sendBankOperation(bankInterface.getWITHDRAW(), bankInterface.getCOPPER_COIN(), count);
                        player.sendMessage(Text.translatable("gui.home_interface.bank.withdraw_copper_coins", count), false);
                    }
                    requestBalance();  // 请求余额
                } else {
                    player.sendMessage(Text.translatable("gui.home_interface.bank.text_field.is_empty"), false);
                }
                bankInterface.getWithdrawField().setText("");
            });
            bankInterface.getWithdrawButtonBySilverCoin().setOnClick(() -> {
                if (!(bankInterface.getWithdrawField().getText().isEmpty()) && Integer.parseInt(bankInterface.getWithdrawField().getText()) > 0) {
                    System.out.println("Withdraw Silver");
                    int count = Integer.parseInt(bankInterface.getWithdrawField().getText());
                    if (hasEnoughSpace(playerInventory, new ItemStack(ModItems.SILVER_COIN), count)) {
                        addItemsToPlayerInventory(player, new ItemStack(ModItems.SILVER_COIN), count);
                        sendBankOperation(bankInterface.getWITHDRAW(), bankInterface.getSILVER_COIN(), count);
                        player.sendMessage(Text.translatable("gui.home_interface.bank.withdraw_silver_coins", count), false);
                    }
                    requestBalance();  // 请求余额
                } else {
                    player.sendMessage(Text.translatable("gui.home_interface.bank.text_field.is_empty"), false);
                }
                bankInterface.getWithdrawField().setText("");
            });
            bankInterface.getWithdrawButtonByGoldCoin().setOnClick(() -> {
                if (!(bankInterface.getWithdrawField().getText().isEmpty()) && Integer.parseInt(bankInterface.getWithdrawField().getText()) > 0) {
                    System.out.println("Withdraw Gold");
                    int count = Integer.parseInt(bankInterface.getWithdrawField().getText());
                    if (hasEnoughSpace(playerInventory, new ItemStack(ModItems.GOLD_COIN), count)) {
                        addItemsToPlayerInventory(player, new ItemStack(ModItems.GOLD_COIN), count);
                        sendBankOperation(bankInterface.getWITHDRAW(), bankInterface.getGOLD_COIN(), count);
                        player.sendMessage(Text.translatable("gui.home_interface.bank.withdraw_gold_coins", count), false);
                    }
                    requestBalance();  // 请求余额
                } else {
                    player.sendMessage(Text.translatable("gui.home_interface.bank.text_field.is_empty"), false);
                }
                bankInterface.getWithdrawField().setText("");
            });
        });
        homePane.add(bankButton, BANK_BUTTON_X, BANK_BUTTON_Y);

        // 创建一个 WItem 组件用于渲染物品
        WItem bankIcon = new WItem(new ItemStack(ModItems.BANK_ICON));  // 创建 WItem，用于显示物品
        homePane.add(bankIcon, BANK_ICON_X, BANK_ICON_Y);

        // =====================================================================================================

        // 创建一个按钮
        officialStoreButton = new WButton(Text.translatable("gui.home_interface.official_store_button")) {
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

        marketInterface.getSearchButton().setOnClick(() -> {
           if (!(marketInterface.getSearchField().getText().isEmpty())) {
               setPage(1);
               requestSearchMarketList(marketInterface.getSearchField().getText());
           } else {
               setPage(1);
               requestMarketList();
           }
           marketInterface.getSearchField().setText("");
        });

        marketInterface.getPreviousPageButton().setOnClick(() -> {
            changePage(-1);
        });

        // 上架按钮
        marketInterface.getListItemButton().setOnClick(() -> {
            if (
                    !(marketInterface.getInventory().getStack(0).isEmpty()) &&
                    !(marketInterface.getInventory().getStack(0).isOf(ModItems.COPPER_COIN)) &&
                    !(marketInterface.getInventory().getStack(0).isOf(ModItems.SILVER_COIN)) &&
                    !(marketInterface.getInventory().getStack(0).isOf(ModItems.GOLD_COIN))
            )
            {
                String copperText = marketInterface.getListItemCopperCoinCountTextField().getText();
                String silverText = marketInterface.getListItemSilverCoinCountTextField().getText();
                String goldText = marketInterface.getListItemGoldCoinCountTextField().getText();
                if (
                        // 确保所有输入框都不为空
                        !copperText.isEmpty() && !silverText.isEmpty() && !goldText.isEmpty() &&

                        // 至少一个大于0
                        (Integer.parseInt(copperText) > 0 || Integer.parseInt(silverText) > 0 || Integer.parseInt(goldText) > 0) &&

                        // 其他两个都 >= 0
                        Integer.parseInt(copperText) >= 0 && Integer.parseInt(silverText) >= 0 && Integer.parseInt(goldText) >= 0
                )
                {
                    int copperCoinCount = Integer.parseInt(marketInterface.getListItemCopperCoinCountTextField().getText());
                    int silverCoinCount = Integer.parseInt(marketInterface.getListItemSilverCoinCountTextField().getText());
                    int goldCoinCount = Integer.parseInt(marketInterface.getListItemGoldCoinCountTextField().getText());

                    int copperCoinTax = copperCoinCount / 5;
                    int silverCoinTax = silverCoinCount / 5;
                    int goldCoinTax = goldCoinCount / 5;

                    if (
                            hasEnoughItems(playerInventory, new ItemStack(ModItems.COPPER_COIN), copperCoinTax) &&
                                    hasEnoughItems(playerInventory, new ItemStack(ModItems.SILVER_COIN), silverCoinTax) &&
                                    hasEnoughItems(playerInventory, new ItemStack(ModItems.GOLD_COIN), goldCoinTax)
                    ) {
                        ItemStack itemToList = marketInterface.getInventory().getStack(0);
                        marketInterface.getInventory().setStack(0, ItemStack.EMPTY);

                        // 发送上架商品的数据包
                        ListItemPacket.sendListItemPacket(UUID.randomUUID(),player.getName().getString(), player.getUuid(), copperCoinCount, silverCoinCount, goldCoinCount, itemToList);
                        removeItemsFromPlayerInventory(playerInventory, new ItemStack(ModItems.COPPER_COIN), copperCoinTax);
                        removeItemsFromPlayerInventory(playerInventory, new ItemStack(ModItems.SILVER_COIN), silverCoinTax);
                        removeItemsFromPlayerInventory(playerInventory, new ItemStack(ModItems.GOLD_COIN), goldCoinTax);
                        // 获取当前的 Screen 实例并更新标题
                        if (MinecraftClient.getInstance().currentScreen instanceof HomeInterfaceScreen) {
                            HomeInterfaceScreen currentScreen = (HomeInterfaceScreen) MinecraftClient.getInstance().currentScreen;

                            // 更新标题为新的文本
                            currentScreen.updateTitle(Text.translatable("gui.home_interface.player_market_title"));
                        }
                        setRootPanel(marketInterface.getPlayerMarketPane());
                        marketInterface.getPlayerMarketPane().validate(this);  // 重新验证新面板布局

                        // 强制刷新当前的屏幕
                        MinecraftClient.getInstance().setScreen(MinecraftClient.getInstance().currentScreen);

                        requestMarketList();

                        page = 1;
                        updateMarketHome(page);

                        marketInterface.getListItemCopperCoinCountTextField().setText("0");
                        marketInterface.getListItemSilverCoinCountTextField().setText("0");
                        marketInterface.getListItemGoldCoinCountTextField().setText("0");
                    } else {
                        player.sendMessage(Text.translatable("gui.home_interface.list_item_tax_payment_failed"));
                    }
                } else {
                    player.sendMessage(Text.translatable("gui.home_interface.list_item.unauthorized_price"));
                }
            } else {
                player.sendMessage(Text.translatable("gui.home_interface.list_item.unauthorized_item"));
            }
        });

        // 添加玩家物品栏
        marketInterface.getListItemPane().add(this.createPlayerInventoryPanel(), 20, 205);  // 在面板底部添加玩家物品栏

        marketInterface.getListButton().setOnClick(() -> {
            // 获取当前的 Screen 实例并更新标题
            if (MinecraftClient.getInstance().currentScreen instanceof HomeInterfaceScreen) {
                HomeInterfaceScreen currentScreen = (HomeInterfaceScreen) MinecraftClient.getInstance().currentScreen;

                // 更新标题为新的文本
                currentScreen.updateTitle(Text.translatable("gui.home_interface.list_title"));
            }
            setRootPanel(marketInterface.getListItemPane());
            marketInterface.getListItemPane().validate(this);  // 重新验证新面板布局

            // 强制刷新当前的屏幕
            MinecraftClient.getInstance().setScreen(MinecraftClient.getInstance().currentScreen);
        });

        // 下一页按钮
        marketInterface.getNextPageButton().setOnClick(() -> {
            System.out.println("Items: " + totalItems);
            System.out.println("Pages: " + totalPages);
            changePage(1);
        });

        // 创建一个按钮
        playerMarketButton = new WButton(Text.translatable("gui.home_interface.player_market_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(100, 20);  // 强制按钮大小为100x20
            }
        };
        // 添加按钮点击事件
        playerMarketButton.setOnClick(() -> {
            requestMarketList();
            // 获取当前的 Screen 实例并更新标题
            if (MinecraftClient.getInstance().currentScreen instanceof HomeInterfaceScreen) {
                HomeInterfaceScreen currentScreen = (HomeInterfaceScreen) MinecraftClient.getInstance().currentScreen;

                // 更新标题为新的文本
                currentScreen.updateTitle(Text.translatable("gui.home_interface.player_market_title"));
            }
            setRootPanel(marketInterface.getPlayerMarketPane());
            marketInterface.getPlayerMarketPane().validate(this);  // 重新验证新面板布局

            // 强制刷新当前的屏幕
            MinecraftClient.getInstance().setScreen(MinecraftClient.getInstance().currentScreen);
        });
        homePane.add(playerMarketButton, PLAYER_MARKET_BUTTON_X, PLAYER_MARKET_BUTTON_Y);

        // 创建一个 WItem 组件用于渲染物品
        WItem playerMarketIcon = new WItem(new ItemStack(ModItems.PLAYER_MARKET_ICON));  // 创建 WItem，用于显示物品
        homePane.add(playerMarketIcon, PLAYER_MARKET_ICON_X, PLAYER_MARKET_ICON_Y);



        myListedItemButton = new WButton(Text.translatable("gui.home_interface.my_listed_item_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(100, 20);  // 强制按钮大小为100x20
            }
        };
        myListedItemButton.setOnClick(() -> {
            requestSearchMarketList(player.getUuid());
            // 获取当前的 Screen 实例并更新标题
            if (MinecraftClient.getInstance().currentScreen instanceof HomeInterfaceScreen) {
                HomeInterfaceScreen currentScreen = (HomeInterfaceScreen) MinecraftClient.getInstance().currentScreen;

                // 更新标题为新的文本
                currentScreen.updateTitle(Text.translatable("gui.home_interface.listed_items_title"));
            }
            setRootPanel(listedItemInterface.getListedItemPane());
            listedItemInterface.getListedItemPane().validate(this);  // 重新验证新面板布局

            // 强制刷新当前的屏幕
            MinecraftClient.getInstance().setScreen(MinecraftClient.getInstance().currentScreen);
        });
        homePane.add(myListedItemButton, MY_LISTED_ITEM_BUTTON_X, MY_LISTED_ITEM_BUTTON_Y);

        // 创建一个 WItem 组件用于渲染物品
        WItem myListedItemIcon = new WItem(new ItemStack(Items.CHEST_MINECART));  // 创建 WItem，用于显示物品
        homePane.add(myListedItemIcon, MY_LISTED_ITEM_ICON_X, MY_LISTED_ITEM_ICON_Y);



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

    // 更新余额的方法
    public void updateBalance(int[] balances) {
        bankInterface.updateBalance(balances);
    }

    // 更新余额的方法
    public void updateBankLevel(int bankLevel) {
        bankInterface.updateBankLevel(bankLevel);
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
        ClientPlayNetworking.send(RequestBankLevelPacket.ID, PacketByteBufs.create());
    }

    public static void requestMarketList() {
        // 向服务器发送请求市场列表的数据包
        ClientPlayNetworking.send(RequestMarketListPacket.ID, PacketByteBufs.create());
    }

    public static void requestSearchMarketList(String itemName) {
        // 向服务器发送请求市场列表的数据包
        RequestSearchMarketListPacket.sendSearchMarketListPacket(itemName);
    }

    public static void requestSearchMarketList(UUID playerUUID) {
        // 向服务器发送请求市场列表的数据包
        RequestSearchByPlayerMarketListPacket.sendSearchMarketListPacket(playerUUID);
    }

    public void updateMarketHome(int page) {
        int startIndex = (page - 1) * itemsPerPage;  // 计算当前页的起始索引
        int endIndex = Math.min(startIndex + itemsPerPage, marketItems.size());  // 计算当前页的结束索引

        for (int i = 0; i < itemsPerPage; i++) {
            int itemIndex = startIndex + i;

            if (itemIndex < marketItems.size()) {
                // 获取当前页的物品，并更新对应槽位和标签
                ListedItem item = marketItems.get(itemIndex);
                // 获取商品ID
                UUID itemUUID = item.getItemId();
                // 铜币价格
                int priceCopper = item.getCopperPrice();
                // 银币价格
                int priceSilver = item.getSilverPrice();
                // 金币价格
                int priceGold = item.getGoldPrice();
                // 为槽位设置物品
                marketInterface.getShow_inventory().setStack(i, item.getItemStack());

                // 更新价格标签
                /*marketInterface.getPRICE_LABELS()[i].setText(Text.literal("Copper: " + item.getCopperPrice() +
                        " | Silver: " + item.getSilverPrice() +
                        " | Gold: " + item.getGoldPrice()));*/
                marketInterface.getPRICE_LABELS()[i].setText(Text.translatable("gui.home_interface.item_price", priceCopper, priceSilver, priceGold));
                // 更新卖家标签
                marketInterface.getSELLER_LABELS()[i].setText(Text.literal(item.getPlayerName()));

                // 更新按钮功能
                marketInterface.getBUY_BUTTONS()[i].setOnClick(() -> {
                    System.out.println("Button Clicked!!");
                    System.out.println(priceCopper + " | " + priceSilver + " | " + priceGold);
                    // 检查玩家是否有足够的钱
                    if (
                                    hasEnoughItems(getPlayerInventory(), new ItemStack(ModItems.COPPER_COIN), priceCopper) &&
                                    hasEnoughItems(getPlayerInventory(), new ItemStack(ModItems.SILVER_COIN), priceSilver) &&
                                    hasEnoughItems(getPlayerInventory(), new ItemStack(ModItems.GOLD_COIN), priceGold)
                    ) {
                        System.out.println("Money Enough!!!");
                        // 检查玩家背包是否有空位
                        if (hasEnoughSpace(getPlayerInventory(), item.getItemStack(), item.getItemStack().getCount())) {
                            System.out.println("Space Enough!!!");
                            // 重置页面
                            setPage(1);
                            // 发送移除商品数据包
                            RemoveListedItemPacket.sendRemoveListedItemPacket(itemUUID);
                            // 获取市场物品列表
                            requestMarketList();
                            // 添加购买的商品到玩家物品栏
                            addItemsToPlayerInventory(getPlayer(), item.getItemStack(), item.getItemStack().getCount());
                            // 清除对应的货币
                            removeItemsFromPlayerInventory(getPlayerInventory(), new ItemStack(ModItems.COPPER_COIN), priceCopper);
                            removeItemsFromPlayerInventory(getPlayerInventory(), new ItemStack(ModItems.SILVER_COIN), priceSilver);
                            removeItemsFromPlayerInventory(getPlayerInventory(), new ItemStack(ModItems.GOLD_COIN), priceGold);
                            // 给玩家发送消息
                            getPlayer().sendMessage(Text.literal("Buy successfully!!"));
                        }
                    }
                });
                System.out.println("Update Button Action!!!");
            } else {
                // 如果没有足够的物品，清空对应槽位和标签
                marketInterface.getShow_inventory().setStack(i, ItemStack.EMPTY);
                marketInterface.getPRICE_LABELS()[i].setText(Text.literal("null"));
                marketInterface.getSELLER_LABELS()[i].setText(Text.literal("null"));
                marketInterface.getBUY_BUTTONS()[i].setOnClick(() -> {System.out.println("1");});
            }
        }
    }

    public void updateMarketSearchingPage(int page) {
        int startIndex = (page - 1) * itemsPerPage;  // 计算当前页的起始索引
        int endIndex = Math.min(startIndex + itemsPerPage, marketItemsBySearching.size());  // 计算当前页的结束索引

        for (int i = 0; i < itemsPerPage; i++) {
            int itemIndex = startIndex + i;

            if (itemIndex < marketItemsBySearching.size()) {
                // 获取当前页的物品，并更新对应槽位和标签
                ListedItem item = marketItemsBySearching.get(itemIndex);
                // 获取商品ID
                UUID itemUUID = item.getItemId();
                // 铜币价格
                int priceCopper = item.getCopperPrice();
                // 银币价格
                int priceSilver = item.getSilverPrice();
                // 金币价格
                int priceGold = item.getGoldPrice();
                // 为槽位设置物品
                listedItemInterface.getShow_inventory().setStack(i, item.getItemStack());

                // 更新价格标签
                /*marketInterface.getPRICE_LABELS()[i].setText(Text.literal("Copper: " + item.getCopperPrice() +
                        " | Silver: " + item.getSilverPrice() +
                        " | Gold: " + item.getGoldPrice()));*/
                listedItemInterface.getPRICE_LABELS()[i].setText(Text.translatable("gui.home_interface.item_price", priceCopper, priceSilver, priceGold));
                // 更新卖家标签
                listedItemInterface.getSELLER_LABELS()[i].setText(Text.literal(item.getPlayerName()));

                // 更新按钮功能
                listedItemInterface.getDISCONTINUED_BUTTONS()[i].setOnClick(() -> {
                    System.out.println("Button Clicked!!");
                    System.out.println(priceCopper + " | " + priceSilver + " | " + priceGold);
                    // 检查玩家背包是否有空位
                    if (hasEnoughSpace(getPlayerInventory(), item.getItemStack(), item.getItemStack().getCount())) {
                        System.out.println("Space Enough!!!");
                        // 重置页面
                        setPage(1);
                        // 发送移除商品数据包
                        RemoveListedItemPacket.sendRemoveListedItemPacket(itemUUID);
                        // 获取市场物品列表
                        requestSearchMarketList(player.getUuid());
                        // 添加购买的商品到玩家物品栏
                        addItemsToPlayerInventory(getPlayer(), item.getItemStack(), item.getItemStack().getCount());
                        // 给玩家发送消息
                        getPlayer().sendMessage(Text.literal("Discontinue successfully!!"));
                    }
                });
            } else {
                // 如果没有足够的物品，清空对应槽位和标签
                listedItemInterface.getShow_inventory().setStack(i, ItemStack.EMPTY);
                listedItemInterface.getPRICE_LABELS()[i].setText(Text.literal("null"));
                listedItemInterface.getSELLER_LABELS()[i].setText(Text.literal("null"));
                listedItemInterface.getDISCONTINUED_BUTTONS()[i].setOnClick(() -> {
                    System.out.println("1");
                });
            }
        }
    }

    // 用于更新页面并控制页面增减
    public void changePage(int delta) {
        System.out.println("ChangePage!!");
        System.out.println("TotalPages: " + totalPages);
        page = Math.max(1, Math.min(totalPages, page + delta));  // 确保页数在有效范围内
        updateMarketHome(page);
    }

    public BankInterface getBankInterface() {
        return bankInterface;
    }

    public List<ListedItem> getMarketItems() {
        return marketItems;
    }

    public void setMarketItems(List<ListedItem> marketItems) {
        this.marketItems = marketItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public AccountLevel getAccountLevel() {
        return accountLevel;
    }

    public void setAccountLevel(AccountLevel accountLevel) {
        this.accountLevel = accountLevel;
    }

    public MarketInterface getMarketInterface() {
        return marketInterface;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }

    public PlayerInventory getPlayerInventory() {
        return playerInventory;
    }

    public void setPlayerInventory(PlayerInventory playerInventory) {
        this.playerInventory = playerInventory;
    }

    public List<ListedItem> getMarketItemsBySearching() {
        return marketItemsBySearching;
    }

    public void setMarketItemsBySearching(List<ListedItem> marketItemsBySearching) {
        this.marketItemsBySearching = marketItemsBySearching;
    }
}
