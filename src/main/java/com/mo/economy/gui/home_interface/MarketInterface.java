package com.mo.economy.gui.home_interface;

import com.mo.economy.gui.ScreenHandlers;
import com.mo.economy.item.ModItems;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;

public class MarketInterface extends ScreenHandler {
    // 创建一个一格的物品栏
    private final SimpleInventory inventory = new SimpleInventory(1);
    // 创建一个九格的物品栏
    private final SimpleInventory show_inventory = new SimpleInventory(9);
    // 玩家市场面板
    private WPlainPanel playerMarketPane;
    // 搜索框
    private WTextField searchField;
    // 搜索按钮
    private WButton searchButton;
    // 商品面板1
    private WPlainPanel itemPane1;
    // 商品槽位1
    private WItemSlot ITEM_SLOT_1;
    // 商品价格标签1
    private WLabel PRICE_LABEL_1;
    // 商品卖家标签1
    private WLabel SELLER_LABEL_1;
    // 商品购买按钮1
    private WButton BUY_BUTTON_1;
    // 商品面板2
    private WPlainPanel itemPane2;
    // 商品槽位2
    private WItemSlot ITEM_SLOT_2;
    // 商品价格标签2
    private WLabel PRICE_LABEL_2;
    // 商品卖家标签2
    private WLabel SELLER_LABEL_2;
    // 商品购买按钮2
    private WButton BUY_BUTTON_2;
    // 商品面板3
    private WPlainPanel itemPane3;
    // 商品槽位3
    private WItemSlot ITEM_SLOT_3;
    // 商品价格标签3
    private WLabel PRICE_LABEL_3;
    // 商品卖家标签3
    private WLabel SELLER_LABEL_3;
    // 商品购买按钮3
    private WButton BUY_BUTTON_3;
    // 商品面板4
    private WPlainPanel itemPane4;
    // 商品槽位4
    private WItemSlot ITEM_SLOT_4;
    // 商品价格标签4
    private WLabel PRICE_LABEL_4;
    // 商品卖家标签4
    private WLabel SELLER_LABEL_4;
    // 商品购买按钮4
    private WButton BUY_BUTTON_4;
    // 商品面板5
    private WPlainPanel itemPane5;
    // 商品槽位5
    private WItemSlot ITEM_SLOT_5;
    // 商品价格标签5
    private WLabel PRICE_LABEL_5;
    // 商品卖家标签5
    private WLabel SELLER_LABEL_5;
    // 商品购买按钮5
    private WButton BUY_BUTTON_5;
    // 商品面板6
    private WPlainPanel itemPane6;
    // 商品槽位6
    private WItemSlot ITEM_SLOT_6;
    // 商品价格标签6
    private WLabel PRICE_LABEL_6;
    // 商品卖家标签6
    private WLabel SELLER_LABEL_6;
    // 商品购买按钮6
    private WButton BUY_BUTTON_6;
    // 商品面板7
    private WPlainPanel itemPane7;
    // 商品槽位7
    private WItemSlot ITEM_SLOT_7;
    // 商品价格标签7
    private WLabel PRICE_LABEL_7;
    // 商品卖家标签7
    private WLabel SELLER_LABEL_7;
    // 商品购买按钮7
    private WButton BUY_BUTTON_7;
    // 商品面板8
    private WPlainPanel itemPane8;
    // 商品槽位8
    private WItemSlot ITEM_SLOT_8;
    // 商品价格标签8
    private WLabel PRICE_LABEL_8;
    // 商品卖家标签8
    private WLabel SELLER_LABEL_8;
    // 商品购买按钮8
    private WButton BUY_BUTTON_8;
    // 商品面板9
    private WPlainPanel itemPane9;
    // 商品槽位9
    private WItemSlot ITEM_SLOT_9;
    // 商品价格标签9
    private WLabel PRICE_LABEL_9;
    // 商品卖家标签9
    private WLabel SELLER_LABEL_9;
    // 商品购买按钮9
    private WButton BUY_BUTTON_9;
    // 商品面板组
    private WPlainPanel[] itemPanes;
    // 商品槽位组
    private WItemSlot[] ITEM_SLOTS;
    // 商品价格标签组
    private WLabel[] PRICE_LABELS;
    // 商品卖家标签组
    private WLabel[] SELLER_LABELS;
    // 商品购买按钮组
    private WButton[] BUY_BUTTONS;
    // 上一页按钮
    private WButton previousPageButton;
    // 上架物品面板
    private WPlainPanel listItemPane;
    // 上架物品槽位
    private WItemSlot slot;
    // 上架物品铜币图标
    private WItem listItemCopperCoinIcon;
    // 上架物品铜币数量输入框
    private WTextField listItemCopperCoinCountTextField;
    // 上架物品银币图标
    private WItem listItemSilverCoinIcon;
    // 上架物品银币数量输入框
    private WTextField listItemSilverCoinCountTextField;
    // 上架物品金币图标
    private WItem listItemGoldCoinIcon;
    // 上架物品金币数量输入框
    private WTextField listItemGoldCoinCountTextField;
    // 上架物品按钮
    private WButton listItemButton;
    // 上架按钮
    private WButton listButton;
    // 下一页按钮
    private WButton nextPageButton;
    // 搜索框的X坐标
    private static final int SEARCH_FIELD_X = 200;
    // 搜索框的Y坐标
    private static final int SEARCH_FIELD_Y = 10;
    // 搜索按钮的X坐标
    private static final int SEARCH_BUTTON_X = SEARCH_FIELD_X + 215;
    // 搜索按钮的Y坐标
    private static final int SEARCH_BUTTON_Y = SEARCH_FIELD_Y;
    // 上架物品铜币图标的X坐标
    private static final int LIST_ITEM_COPPER_COIN_ICON_X = 40;
    // 上架物品铜币图标的Y坐标
    private static final int LIST_ITEM_COPPER_COIN_ICON_Y = 60;
    // 上架物品铜币数量输入框的X坐标
    private static final int LIST_ITEM_COPPER_COIN_TEXT_FIRED_X = LIST_ITEM_COPPER_COIN_ICON_X + 20;
    // 上架物品铜币数量输入框的Y坐标
    private static final int LIST_ITEM_COPPER_COIN_TEXT_FIRED_Y = LIST_ITEM_COPPER_COIN_ICON_Y;
    // 上架物品银币图标的X坐标
    private static final int LIST_ITEM_SILVER_COIN_ICON_X = LIST_ITEM_COPPER_COIN_ICON_X;
    // 上架物品银币图标的Y坐标
    private static final int LIST_ITEM_SILVER_COIN_ICON_Y = LIST_ITEM_COPPER_COIN_ICON_Y + 20;
    // 上架物品银币数量输入框的X坐标
    private static final int LIST_ITEM_SILVER_COIN_TEXT_FIRED_X = LIST_ITEM_SILVER_COIN_ICON_X + 20;
    // 上架物品银币数量输入框的Y坐标
    private static final int LIST_ITEM_SILVER_COIN_TEXT_FIRED_Y = LIST_ITEM_SILVER_COIN_ICON_Y;
    // 上架物品金币图标的X坐标
    private static final int LIST_ITEM_GOLD_COIN_ICON_X = LIST_ITEM_COPPER_COIN_ICON_X;
    // 上架物品金币图标的Y坐标
    private static final int LIST_ITEM_GOLD_COIN_ICON_Y = LIST_ITEM_SILVER_COIN_ICON_Y + 20;
    // 上架物品金币数量输入框的X坐标
    private static final int LIST_ITEM_GOLD_COIN_TEXT_FIRED_X = LIST_ITEM_GOLD_COIN_ICON_X + 20;
    // 上架物品金币数量输入框的Y坐标
    private static final int LIST_ITEM_GOLD_COIN_TEXT_FIRED_Y = LIST_ITEM_GOLD_COIN_ICON_Y;
    // 面板1的Y坐标
    private static final int ITEM_PANE_1_Y = 35;
    // 面板2的Y坐标
    private static final int ITEM_PANE_2_Y = ITEM_PANE_1_Y + 25;
    // 面板3的Y坐标
    private static final int ITEM_PANE_3_Y = ITEM_PANE_2_Y + 25;
    // 面板4的Y坐标
    private static final int ITEM_PANE_4_Y = ITEM_PANE_3_Y + 25;
    // 面板5的Y坐标
    private static final int ITEM_PANE_5_Y = ITEM_PANE_4_Y + 25;
    // 面板6的Y坐标
    private static final int ITEM_PANE_6_Y = ITEM_PANE_5_Y + 25;
    // 面板7的Y坐标
    private static final int ITEM_PANE_7_Y = ITEM_PANE_6_Y + 25;
    // 面板8的Y坐标
    private static final int ITEM_PANE_8_Y = ITEM_PANE_7_Y + 25;
    // 面板9的Y坐标
    private static final int ITEM_PANE_9_Y = ITEM_PANE_8_Y + 25;


    public MarketInterface(int syncId) {
        super(ScreenHandlers.HOME_INTERFACE_SCREEN_HANDLER, syncId);
        playerMarketPane = new WPlainPanel();
        playerMarketPane.setSize(500, 300);  // 设置面板大小

        searchField = new WTextField() {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(210, 20);  // 强制按钮大小为100x20
            }
        };
        playerMarketPane.add(searchField, SEARCH_FIELD_X, SEARCH_FIELD_Y);

        searchButton = new WButton(Text.translatable("gui.home_interface.search_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        playerMarketPane.add(searchButton, SEARCH_BUTTON_X, SEARCH_BUTTON_Y);

        // 创建一个WPlainPanel来显示物品信息
        itemPane1 = new WPlainPanel();
        itemPane1.setSize(500, 20);  // 设置面板的大小

        // 创建一个不可交互的WItemSlot
        ITEM_SLOT_1 = WItemSlot.of(show_inventory, 0);
        ITEM_SLOT_1.setModifiable(false);  // 禁止玩家与slot互动
        ITEM_SLOT_1.setSize(18, 18);
        itemPane1.add(ITEM_SLOT_1, 30, 1);  // 将itemSlot放置在(0, 0)位置

        // 添加显示价格信息的WLabel
        PRICE_LABEL_1 = new WLabel(Text.literal("Copper: " + 0 +
                " | Silver: " + 0 +
                " | Gold: " + 0));
        PRICE_LABEL_1.setSize(100, 20);  // 设置标签的大小
        itemPane1.add(PRICE_LABEL_1, 80, 7);  // 将priceLabel放置在(30, 0)位置

        // 添加显示卖家名称的WLabel
        SELLER_LABEL_1 = new WLabel(Text.literal("1"));
        SELLER_LABEL_1.setSize(60, 20);  // 设置标签大小
        // itemPanel.add(sellerLabel,  80 + MinecraftClient.getInstance().textRenderer.getWidth(priceLabel.getText().getString()) + 20, 7);  // 将sellerLabel放置在(140, 0)位置
        itemPane1.add(SELLER_LABEL_1,  280, 7);  // 将sellerLabel放置在(140, 0)位置

        // 添加一个"购买"按钮
        BUY_BUTTON_1 = new WButton(Text.translatable("gui.home_interface.buy_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        itemPane1.add(BUY_BUTTON_1, itemPane1.getWidth() - 90, 0);  // 将buyButton放置在(220, 0)位置
        // 将itemPanel添加到主面板或其他容器中
        playerMarketPane.add(itemPane1, 0, ITEM_PANE_1_Y);



        // 创建一个WPlainPanel来显示物品信息
        itemPane2 = new WPlainPanel();
        itemPane2.setSize(500, 20);  // 设置面板的大小

        // 创建一个不可交互的WItemSlot
        ITEM_SLOT_2 = WItemSlot.of(show_inventory, 1);
        ITEM_SLOT_2.setModifiable(false);  // 禁止玩家与slot互动
        ITEM_SLOT_2.setSize(18, 18);
        itemPane2.add(ITEM_SLOT_2, 30, 1);  // 将itemSlot放置在(0, 0)位置

        // 添加显示价格信息的WLabel
        PRICE_LABEL_2 = new WLabel(Text.literal("Copper: " + 0 +
                " | Silver: " + 0 +
                " | Gold: " + 0));
        PRICE_LABEL_2.setSize(100, 20);  // 设置标签的大小
        itemPane2.add(PRICE_LABEL_2, 80, 7);  // 将priceLabel放置在(30, 0)位置

        // 添加显示卖家名称的WLabel
        SELLER_LABEL_2 = new WLabel(Text.literal("1"));
        SELLER_LABEL_2.setSize(60, 20);  // 设置标签大小
        // itemPanel.add(sellerLabel,  80 + MinecraftClient.getInstance().textRenderer.getWidth(priceLabel.getText().getString()) + 20, 7);  // 将sellerLabel放置在(140, 0)位置
        itemPane2.add(SELLER_LABEL_2,  280, 7);  // 将sellerLabel放置在(140, 0)位置

        // 添加一个"购买"按钮
        BUY_BUTTON_2 = new WButton(Text.translatable("gui.home_interface.buy_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        itemPane2.add(BUY_BUTTON_2, itemPane2.getWidth() - 90, 0);  // 将buyButton放置在(220, 0)位置
        // 将itemPanel添加到主面板或其他容器中
        playerMarketPane.add(itemPane2, 0, ITEM_PANE_2_Y);



        // 创建一个WPlainPanel来显示物品信息
        itemPane3 = new WPlainPanel();
        itemPane3.setSize(500, 20);  // 设置面板的大小

        // 创建一个不可交互的WItemSlot
        ITEM_SLOT_3 = WItemSlot.of(show_inventory, 2);
        ITEM_SLOT_3.setModifiable(false);  // 禁止玩家与slot互动
        ITEM_SLOT_3.setSize(18, 18);
        itemPane3.add(ITEM_SLOT_3, 30, 1);  // 将itemSlot放置在(0, 0)位置

        // 添加显示价格信息的WLabel
        PRICE_LABEL_3 = new WLabel(Text.literal("Copper: " + 0 +
                " | Silver: " + 0 +
                " | Gold: " + 0));
        PRICE_LABEL_3.setSize(100, 20);  // 设置标签的大小
        itemPane3.add(PRICE_LABEL_3, 80, 7);  // 将priceLabel放置在(30, 0)位置

        // 添加显示卖家名称的WLabel
        SELLER_LABEL_3 = new WLabel(Text.literal("1"));
        SELLER_LABEL_3.setSize(60, 20);  // 设置标签大小
        // itemPanel.add(sellerLabel,  80 + MinecraftClient.getInstance().textRenderer.getWidth(priceLabel.getText().getString()) + 20, 7);  // 将sellerLabel放置在(140, 0)位置
        itemPane3.add(SELLER_LABEL_3,  280, 7);  // 将sellerLabel放置在(140, 0)位置

        // 添加一个"购买"按钮
        BUY_BUTTON_3 = new WButton(Text.translatable("gui.home_interface.buy_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        itemPane3.add(BUY_BUTTON_3, itemPane3.getWidth() - 90, 0);  // 将buyButton放置在(220, 0)位置
        // 将itemPanel添加到主面板或其他容器中
        playerMarketPane.add(itemPane3, 0, ITEM_PANE_3_Y);



        // 创建一个WPlainPanel来显示物品信息
        itemPane4 = new WPlainPanel();
        itemPane4.setSize(500, 20);  // 设置面板的大小

        // 创建一个不可交互的WItemSlot
        ITEM_SLOT_4 = WItemSlot.of(show_inventory, 3);
        ITEM_SLOT_4.setModifiable(false);  // 禁止玩家与slot互动
        ITEM_SLOT_4.setSize(18, 18);
        itemPane4.add(ITEM_SLOT_4, 30, 1);  // 将itemSlot放置在(0, 0)位置

        // 添加显示价格信息的WLabel
        PRICE_LABEL_4 = new WLabel(Text.literal("Copper: " + 0 +
                " | Silver: " + 0 +
                " | Gold: " + 0));
        PRICE_LABEL_4.setSize(100, 20);  // 设置标签的大小
        itemPane4.add(PRICE_LABEL_4, 80, 7);  // 将priceLabel放置在(30, 0)位置

        // 添加显示卖家名称的WLabel
        SELLER_LABEL_4 = new WLabel(Text.literal("1"));
        SELLER_LABEL_4.setSize(60, 20);  // 设置标签大小
        // itemPanel.add(sellerLabel,  80 + MinecraftClient.getInstance().textRenderer.getWidth(priceLabel.getText().getString()) + 20, 7);  // 将sellerLabel放置在(140, 0)位置
        itemPane4.add(SELLER_LABEL_4,  280, 7);  // 将sellerLabel放置在(140, 0)位置

        // 添加一个"购买"按钮
        BUY_BUTTON_4 = new WButton(Text.translatable("gui.home_interface.buy_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        itemPane4.add(BUY_BUTTON_4, itemPane4.getWidth() - 90, 0);  // 将buyButton放置在(220, 0)位置
        // 将itemPanel添加到主面板或其他容器中
        playerMarketPane.add(itemPane4, 0, ITEM_PANE_4_Y);



        // 创建一个WPlainPanel来显示物品信息
        itemPane5 = new WPlainPanel();
        itemPane5.setSize(500, 20);  // 设置面板的大小

        // 创建一个不可交互的WItemSlot
        ITEM_SLOT_5 = WItemSlot.of(show_inventory, 4);
        ITEM_SLOT_5.setModifiable(false);  // 禁止玩家与slot互动
        ITEM_SLOT_5.setSize(18, 18);
        itemPane5.add(ITEM_SLOT_5, 30, 1);  // 将itemSlot放置在(0, 0)位置

        // 添加显示价格信息的WLabel
        PRICE_LABEL_5 = new WLabel(Text.literal("Copper: " + 0 +
                " | Silver: " + 0 +
                " | Gold: " + 0));
        PRICE_LABEL_5.setSize(100, 20);  // 设置标签的大小
        itemPane5.add(PRICE_LABEL_5, 80, 7);  // 将priceLabel放置在(30, 0)位置

        // 添加显示卖家名称的WLabel
        SELLER_LABEL_5 = new WLabel(Text.literal("1"));
        SELLER_LABEL_5.setSize(60, 20);  // 设置标签大小
        // itemPanel.add(sellerLabel,  80 + MinecraftClient.getInstance().textRenderer.getWidth(priceLabel.getText().getString()) + 20, 7);  // 将sellerLabel放置在(140, 0)位置
        itemPane5.add(SELLER_LABEL_5,  280, 7);  // 将sellerLabel放置在(140, 0)位置

        // 添加一个"购买"按钮
        BUY_BUTTON_5 = new WButton(Text.translatable("gui.home_interface.buy_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        itemPane5.add(BUY_BUTTON_5, itemPane5.getWidth() - 90, 0);  // 将buyButton放置在(220, 0)位置
        // 将itemPanel添加到主面板或其他容器中
        playerMarketPane.add(itemPane5, 0, ITEM_PANE_5_Y);



        // 创建一个WPlainPanel来显示物品信息
        itemPane6 = new WPlainPanel();
        itemPane6.setSize(500, 20);  // 设置面板的大小

        // 创建一个不可交互的WItemSlot
        ITEM_SLOT_6 = WItemSlot.of(show_inventory, 5);
        ITEM_SLOT_6.setModifiable(false);  // 禁止玩家与slot互动
        ITEM_SLOT_6.setSize(18, 18);
        itemPane6.add(ITEM_SLOT_6, 30, 1);  // 将itemSlot放置在(0, 0)位置

        // 添加显示价格信息的WLabel
        PRICE_LABEL_6 = new WLabel(Text.literal("Copper: " + 0 +
                " | Silver: " + 0 +
                " | Gold: " + 0));
        PRICE_LABEL_6.setSize(100, 20);  // 设置标签的大小
        itemPane6.add(PRICE_LABEL_6, 80, 7);  // 将priceLabel放置在(30, 0)位置

        // 添加显示卖家名称的WLabel
        SELLER_LABEL_6 = new WLabel(Text.literal("1"));
        SELLER_LABEL_6.setSize(60, 20);  // 设置标签大小
        // itemPanel.add(sellerLabel,  80 + MinecraftClient.getInstance().textRenderer.getWidth(priceLabel.getText().getString()) + 20, 7);  // 将sellerLabel放置在(140, 0)位置
        itemPane6.add(SELLER_LABEL_6,  280, 7);  // 将sellerLabel放置在(140, 0)位置

        // 添加一个"购买"按钮
        BUY_BUTTON_6 = new WButton(Text.translatable("gui.home_interface.buy_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        itemPane6.add(BUY_BUTTON_6, itemPane6.getWidth() - 90, 0);  // 将buyButton放置在(220, 0)位置
        // 将itemPanel添加到主面板或其他容器中
        playerMarketPane.add(itemPane6, 0, ITEM_PANE_6_Y);



        // 创建一个WPlainPanel来显示物品信息
        itemPane7 = new WPlainPanel();
        itemPane7.setSize(500, 20);  // 设置面板的大小

        // 创建一个不可交互的WItemSlot
        ITEM_SLOT_7 = WItemSlot.of(show_inventory, 6);
        ITEM_SLOT_7.setModifiable(false);  // 禁止玩家与slot互动
        ITEM_SLOT_7.setSize(18, 18);
        itemPane7.add(ITEM_SLOT_7, 30, 1);  // 将itemSlot放置在(0, 0)位置

        // 添加显示价格信息的WLabel
        PRICE_LABEL_7 = new WLabel(Text.literal("Copper: " + 0 +
                " | Silver: " + 0 +
                " | Gold: " + 0));
        PRICE_LABEL_7.setSize(100, 20);  // 设置标签的大小
        itemPane7.add(PRICE_LABEL_7, 80, 7);  // 将priceLabel放置在(30, 0)位置

        // 添加显示卖家名称的WLabel
        SELLER_LABEL_7 = new WLabel(Text.literal("1"));
        SELLER_LABEL_7.setSize(60, 20);  // 设置标签大小
        // itemPanel.add(sellerLabel,  80 + MinecraftClient.getInstance().textRenderer.getWidth(priceLabel.getText().getString()) + 20, 7);  // 将sellerLabel放置在(140, 0)位置
        itemPane7.add(SELLER_LABEL_7,  280, 7);  // 将sellerLabel放置在(140, 0)位置

        // 添加一个"购买"按钮
        BUY_BUTTON_7 = new WButton(Text.translatable("gui.home_interface.buy_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        itemPane7.add(BUY_BUTTON_7, itemPane7.getWidth() - 90, 0);  // 将buyButton放置在(220, 0)位置
        // 将itemPanel添加到主面板或其他容器中
        playerMarketPane.add(itemPane7, 0, ITEM_PANE_7_Y);



        // 创建一个WPlainPanel来显示物品信息
        itemPane8 = new WPlainPanel();
        itemPane8.setSize(500, 20);  // 设置面板的大小

        // 创建一个不可交互的WItemSlot
        ITEM_SLOT_8 = WItemSlot.of(show_inventory, 7);
        ITEM_SLOT_8.setModifiable(false);  // 禁止玩家与slot互动
        ITEM_SLOT_8.setSize(18, 18);
        itemPane8.add(ITEM_SLOT_8, 30, 1);  // 将itemSlot放置在(0, 0)位置

        // 添加显示价格信息的WLabel
        PRICE_LABEL_8 = new WLabel(Text.literal("Copper: " + 0 +
                " | Silver: " + 0 +
                " | Gold: " + 0));
        PRICE_LABEL_8.setSize(100, 20);  // 设置标签的大小
        itemPane8.add(PRICE_LABEL_8, 80, 7);  // 将priceLabel放置在(30, 0)位置

        // 添加显示卖家名称的WLabel
        SELLER_LABEL_8 = new WLabel(Text.literal("1"));
        SELLER_LABEL_8.setSize(60, 20);  // 设置标签大小
        // itemPanel.add(sellerLabel,  80 + MinecraftClient.getInstance().textRenderer.getWidth(priceLabel.getText().getString()) + 20, 7);  // 将sellerLabel放置在(140, 0)位置
        itemPane8.add(SELLER_LABEL_8,  280, 7);  // 将sellerLabel放置在(140, 0)位置

        // 添加一个"购买"按钮
        BUY_BUTTON_8 = new WButton(Text.translatable("gui.home_interface.buy_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        itemPane8.add(BUY_BUTTON_8, itemPane8.getWidth() - 90, 0);  // 将buyButton放置在(220, 0)位置
        // 将itemPanel添加到主面板或其他容器中
        playerMarketPane.add(itemPane8, 0, ITEM_PANE_8_Y);

        // 创建一个WPlainPanel来显示物品信息
        itemPane9 = new WPlainPanel();
        itemPane9.setSize(500, 20);  // 设置面板的大小

        // 创建一个不可交互的WItemSlot
        ITEM_SLOT_9 = WItemSlot.of(show_inventory, 8);
        ITEM_SLOT_9.setModifiable(false);  // 禁止玩家与slot互动
        ITEM_SLOT_9.setSize(18, 18);
        itemPane9.add(ITEM_SLOT_9, 30, 1);  // 将itemSlot放置在(0, 0)位置

        // 添加显示价格信息的WLabel
        PRICE_LABEL_9 = new WLabel(Text.literal("Copper: " + 0 +
                " | Silver: " + 0 +
                " | Gold: " + 0));
        PRICE_LABEL_9.setSize(100, 20);  // 设置标签的大小
        itemPane9.add(PRICE_LABEL_9, 80, 7);  // 将priceLabel放置在(30, 0)位置

        // 添加显示卖家名称的WLabel
        SELLER_LABEL_9 = new WLabel(Text.literal("1"));
        SELLER_LABEL_9.setSize(60, 20);  // 设置标签大小
        // itemPanel.add(sellerLabel,  80 + MinecraftClient.getInstance().textRenderer.getWidth(priceLabel.getText().getString()) + 20, 7);  // 将sellerLabel放置在(140, 0)位置
        itemPane9.add(SELLER_LABEL_9,  280, 7);  // 将sellerLabel放置在(140, 0)位置

        // 添加一个"购买"按钮
        BUY_BUTTON_9 = new WButton(Text.translatable("gui.home_interface.buy_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        itemPane9.add(BUY_BUTTON_9, itemPane9.getWidth() - 90, 0);  // 将buyButton放置在(220, 0)位置
        // 将itemPanel添加到主面板或其他容器中
        playerMarketPane.add(itemPane9, 0, ITEM_PANE_9_Y);

        itemPanes = new WPlainPanel[]{itemPane1, itemPane2, itemPane3, itemPane4, itemPane5, itemPane6, itemPane7, itemPane8, itemPane9};
        ITEM_SLOTS = new WItemSlot[]{ITEM_SLOT_1, ITEM_SLOT_2, ITEM_SLOT_3, ITEM_SLOT_4, ITEM_SLOT_5, ITEM_SLOT_6, ITEM_SLOT_7, ITEM_SLOT_8, ITEM_SLOT_9};
        PRICE_LABELS = new WLabel[]{PRICE_LABEL_1, PRICE_LABEL_2, PRICE_LABEL_3, PRICE_LABEL_4, PRICE_LABEL_5, PRICE_LABEL_6, PRICE_LABEL_7, PRICE_LABEL_8, PRICE_LABEL_9};
        SELLER_LABELS = new WLabel[]{SELLER_LABEL_1, SELLER_LABEL_2, SELLER_LABEL_3, SELLER_LABEL_4, SELLER_LABEL_5, SELLER_LABEL_6, SELLER_LABEL_7, SELLER_LABEL_8, SELLER_LABEL_9};
        BUY_BUTTONS = new WButton[]{BUY_BUTTON_1, BUY_BUTTON_2, BUY_BUTTON_3, BUY_BUTTON_4, BUY_BUTTON_5, BUY_BUTTON_6, BUY_BUTTON_7, BUY_BUTTON_8, BUY_BUTTON_9};

        previousPageButton = new WButton(Text.translatable("gui.home_interface.previous_page_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(20, 20);  // 强制按钮大小为100x20
            }
        };
        playerMarketPane.add(previousPageButton, 20, playerMarketPane.getHeight() - 30);

        listItemPane = new WPlainPanel();
        listItemPane.setSize(200, 300);  // 设置面板大小

        // 创建一个自定义的物品槽，并连接到自定义库存（customInventory）
        slot = WItemSlot.of(inventory, 0);  // 物品槽连接到自定义的SimpleInventory
        listItemPane.add(slot, 90, 30);  // 将槽位添加到网格布局中

        // 创建一个 WItem 组件用于渲染物品
        listItemCopperCoinIcon = new WItem(new ItemStack(ModItems.COPPER_COIN));  // 创建 WItem，用于显示物品
        listItemPane.add(listItemCopperCoinIcon, LIST_ITEM_COPPER_COIN_ICON_X, LIST_ITEM_COPPER_COIN_ICON_Y);
        // 数额输入框
        listItemCopperCoinCountTextField = new WTextField() {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(100, 10);  // 强制按钮大小为100x20
            }
        };
        listItemCopperCoinCountTextField.setText("0");
        listItemPane.add(listItemCopperCoinCountTextField, LIST_ITEM_COPPER_COIN_TEXT_FIRED_X, LIST_ITEM_COPPER_COIN_TEXT_FIRED_Y);

        // 创建一个 WItem 组件用于渲染物品
        listItemSilverCoinIcon = new WItem(new ItemStack(ModItems.SILVER_COIN));  // 创建 WItem，用于显示物品
        listItemPane.add(listItemSilverCoinIcon, LIST_ITEM_SILVER_COIN_ICON_X, LIST_ITEM_SILVER_COIN_ICON_Y);
        // 数额输入框
        listItemSilverCoinCountTextField = new WTextField() {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(100, 20);  // 强制按钮大小为100x20
            }
        };
        listItemSilverCoinCountTextField.setText("0");
        listItemPane.add(listItemSilverCoinCountTextField, LIST_ITEM_SILVER_COIN_TEXT_FIRED_X, LIST_ITEM_SILVER_COIN_TEXT_FIRED_Y);

        // 创建一个 WItem 组件用于渲染物品
        listItemGoldCoinIcon = new WItem(new ItemStack(ModItems.GOLD_COIN));  // 创建 WItem，用于显示物品
        listItemPane.add(listItemGoldCoinIcon, LIST_ITEM_GOLD_COIN_ICON_X, LIST_ITEM_GOLD_COIN_ICON_Y);
        // 数额输入框
        listItemGoldCoinCountTextField = new WTextField() {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(100, 20);  // 强制按钮大小为100x20
            }
        };
        listItemGoldCoinCountTextField.setText("0");
        listItemPane.add(listItemGoldCoinCountTextField, LIST_ITEM_GOLD_COIN_TEXT_FIRED_X, LIST_ITEM_GOLD_COIN_TEXT_FIRED_Y);

        // 上架按钮
        listItemButton = new WButton(Text.translatable("gui.home_interface.list_item_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(100, 20);  // 强制按钮大小为100x20
            }
        };
        listItemPane.add(listItemButton, listItemPane.getWidth() / 2 - 50, listItemPane.getHeight() - 120);

        listButton = new WButton(Text.translatable("gui.home_interface.list_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(100, 20);  // 强制按钮大小为100x20
            }
        };
        playerMarketPane.add(listButton, playerMarketPane.getWidth() / 2 - 50, playerMarketPane.getHeight() - 30);

        nextPageButton = new WButton(Text.translatable("gui.home_interface.next_page_button")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(20, 20);  // 强制按钮大小为100x20
            }
        };
        playerMarketPane.add(nextPageButton, playerMarketPane.getWidth() - 40, playerMarketPane.getHeight() - 30);
    }

    public WPlainPanel getPlayerMarketPane() {
        return playerMarketPane;
    }

    public void setPlayerMarketPane(WPlainPanel playerMarketPane) {
        this.playerMarketPane = playerMarketPane;
    }

    public WPlainPanel getItemPane1() {
        return itemPane1;
    }

    public void setItemPane1(WPlainPanel itemPane1) {
        this.itemPane1 = itemPane1;
    }

    public WItemSlot getITEM_SLOT_1() {
        return ITEM_SLOT_1;
    }

    public void setITEM_SLOT_1(WItemSlot ITEM_SLOT_1) {
        this.ITEM_SLOT_1 = ITEM_SLOT_1;
    }

    public WLabel getPRICE_LABEL_1() {
        return PRICE_LABEL_1;
    }

    public void setPRICE_LABEL_1(WLabel PRICE_LABEL_1) {
        this.PRICE_LABEL_1 = PRICE_LABEL_1;
    }

    public WLabel getSELLER_LABEL_1() {
        return SELLER_LABEL_1;
    }

    public void setSELLER_LABEL_1(WLabel SELLER_LABEL_1) {
        this.SELLER_LABEL_1 = SELLER_LABEL_1;
    }

    public WButton getBUY_BUTTON_1() {
        return BUY_BUTTON_1;
    }

    public void setBUY_BUTTON_1(WButton BUY_BUTTON_1) {
        this.BUY_BUTTON_1 = BUY_BUTTON_1;
    }

    public WPlainPanel getItemPane2() {
        return itemPane2;
    }

    public void setItemPane2(WPlainPanel itemPane2) {
        this.itemPane2 = itemPane2;
    }

    public WItemSlot getITEM_SLOT_2() {
        return ITEM_SLOT_2;
    }

    public void setITEM_SLOT_2(WItemSlot ITEM_SLOT_2) {
        this.ITEM_SLOT_2 = ITEM_SLOT_2;
    }

    public WLabel getPRICE_LABEL_2() {
        return PRICE_LABEL_2;
    }

    public void setPRICE_LABEL_2(WLabel PRICE_LABEL_2) {
        this.PRICE_LABEL_2 = PRICE_LABEL_2;
    }

    public WLabel getSELLER_LABEL_2() {
        return SELLER_LABEL_2;
    }

    public void setSELLER_LABEL_2(WLabel SELLER_LABEL_2) {
        this.SELLER_LABEL_2 = SELLER_LABEL_2;
    }

    public WButton getBUY_BUTTON_2() {
        return BUY_BUTTON_2;
    }

    public void setBUY_BUTTON_2(WButton BUY_BUTTON_2) {
        this.BUY_BUTTON_2 = BUY_BUTTON_2;
    }

    public WPlainPanel getItemPane3() {
        return itemPane3;
    }

    public void setItemPane3(WPlainPanel itemPane3) {
        this.itemPane3 = itemPane3;
    }

    public WItemSlot getITEM_SLOT_3() {
        return ITEM_SLOT_3;
    }

    public void setITEM_SLOT_3(WItemSlot ITEM_SLOT_3) {
        this.ITEM_SLOT_3 = ITEM_SLOT_3;
    }

    public WLabel getPRICE_LABEL_3() {
        return PRICE_LABEL_3;
    }

    public void setPRICE_LABEL_3(WLabel PRICE_LABEL_3) {
        this.PRICE_LABEL_3 = PRICE_LABEL_3;
    }

    public WLabel getSELLER_LABEL_3() {
        return SELLER_LABEL_3;
    }

    public void setSELLER_LABEL_3(WLabel SELLER_LABEL_3) {
        this.SELLER_LABEL_3 = SELLER_LABEL_3;
    }

    public WButton getBUY_BUTTON_3() {
        return BUY_BUTTON_3;
    }

    public void setBUY_BUTTON_3(WButton BUY_BUTTON_3) {
        this.BUY_BUTTON_3 = BUY_BUTTON_3;
    }

    public WPlainPanel getItemPane4() {
        return itemPane4;
    }

    public void setItemPane4(WPlainPanel itemPane4) {
        this.itemPane4 = itemPane4;
    }

    public WItemSlot getITEM_SLOT_4() {
        return ITEM_SLOT_4;
    }

    public void setITEM_SLOT_4(WItemSlot ITEM_SLOT_4) {
        this.ITEM_SLOT_4 = ITEM_SLOT_4;
    }

    public WLabel getPRICE_LABEL_4() {
        return PRICE_LABEL_4;
    }

    public void setPRICE_LABEL_4(WLabel PRICE_LABEL_4) {
        this.PRICE_LABEL_4 = PRICE_LABEL_4;
    }

    public WLabel getSELLER_LABEL_4() {
        return SELLER_LABEL_4;
    }

    public void setSELLER_LABEL_4(WLabel SELLER_LABEL_4) {
        this.SELLER_LABEL_4 = SELLER_LABEL_4;
    }

    public WButton getBUY_BUTTON_4() {
        return BUY_BUTTON_4;
    }

    public void setBUY_BUTTON_4(WButton BUY_BUTTON_4) {
        this.BUY_BUTTON_4 = BUY_BUTTON_4;
    }

    public WPlainPanel getItemPane5() {
        return itemPane5;
    }

    public void setItemPane5(WPlainPanel itemPane5) {
        this.itemPane5 = itemPane5;
    }

    public WItemSlot getITEM_SLOT_5() {
        return ITEM_SLOT_5;
    }

    public void setITEM_SLOT_5(WItemSlot ITEM_SLOT_5) {
        this.ITEM_SLOT_5 = ITEM_SLOT_5;
    }

    public WLabel getPRICE_LABEL_5() {
        return PRICE_LABEL_5;
    }

    public void setPRICE_LABEL_5(WLabel PRICE_LABEL_5) {
        this.PRICE_LABEL_5 = PRICE_LABEL_5;
    }

    public WLabel getSELLER_LABEL_5() {
        return SELLER_LABEL_5;
    }

    public void setSELLER_LABEL_5(WLabel SELLER_LABEL_5) {
        this.SELLER_LABEL_5 = SELLER_LABEL_5;
    }

    public WButton getBUY_BUTTON_5() {
        return BUY_BUTTON_5;
    }

    public void setBUY_BUTTON_5(WButton BUY_BUTTON_5) {
        this.BUY_BUTTON_5 = BUY_BUTTON_5;
    }

    public WPlainPanel getItemPane6() {
        return itemPane6;
    }

    public void setItemPane6(WPlainPanel itemPane6) {
        this.itemPane6 = itemPane6;
    }

    public WItemSlot getITEM_SLOT_6() {
        return ITEM_SLOT_6;
    }

    public void setITEM_SLOT_6(WItemSlot ITEM_SLOT_6) {
        this.ITEM_SLOT_6 = ITEM_SLOT_6;
    }

    public WLabel getPRICE_LABEL_6() {
        return PRICE_LABEL_6;
    }

    public void setPRICE_LABEL_6(WLabel PRICE_LABEL_6) {
        this.PRICE_LABEL_6 = PRICE_LABEL_6;
    }

    public WLabel getSELLER_LABEL_6() {
        return SELLER_LABEL_6;
    }

    public void setSELLER_LABEL_6(WLabel SELLER_LABEL_6) {
        this.SELLER_LABEL_6 = SELLER_LABEL_6;
    }

    public WButton getBUY_BUTTON_6() {
        return BUY_BUTTON_6;
    }

    public void setBUY_BUTTON_6(WButton BUY_BUTTON_6) {
        this.BUY_BUTTON_6 = BUY_BUTTON_6;
    }

    public WPlainPanel getItemPane7() {
        return itemPane7;
    }

    public void setItemPane7(WPlainPanel itemPane7) {
        this.itemPane7 = itemPane7;
    }

    public WItemSlot getITEM_SLOT_7() {
        return ITEM_SLOT_7;
    }

    public void setITEM_SLOT_7(WItemSlot ITEM_SLOT_7) {
        this.ITEM_SLOT_7 = ITEM_SLOT_7;
    }

    public WLabel getPRICE_LABEL_7() {
        return PRICE_LABEL_7;
    }

    public void setPRICE_LABEL_7(WLabel PRICE_LABEL_7) {
        this.PRICE_LABEL_7 = PRICE_LABEL_7;
    }

    public WLabel getSELLER_LABEL_7() {
        return SELLER_LABEL_7;
    }

    public void setSELLER_LABEL_7(WLabel SELLER_LABEL_7) {
        this.SELLER_LABEL_7 = SELLER_LABEL_7;
    }

    public WButton getBUY_BUTTON_7() {
        return BUY_BUTTON_7;
    }

    public void setBUY_BUTTON_7(WButton BUY_BUTTON_7) {
        this.BUY_BUTTON_7 = BUY_BUTTON_7;
    }

    public WPlainPanel getItemPane8() {
        return itemPane8;
    }

    public void setItemPane8(WPlainPanel itemPane8) {
        this.itemPane8 = itemPane8;
    }

    public WItemSlot getITEM_SLOT_8() {
        return ITEM_SLOT_8;
    }

    public void setITEM_SLOT_8(WItemSlot ITEM_SLOT_8) {
        this.ITEM_SLOT_8 = ITEM_SLOT_8;
    }

    public WLabel getPRICE_LABEL_8() {
        return PRICE_LABEL_8;
    }

    public void setPRICE_LABEL_8(WLabel PRICE_LABEL_8) {
        this.PRICE_LABEL_8 = PRICE_LABEL_8;
    }

    public WLabel getSELLER_LABEL_8() {
        return SELLER_LABEL_8;
    }

    public void setSELLER_LABEL_8(WLabel SELLER_LABEL_8) {
        this.SELLER_LABEL_8 = SELLER_LABEL_8;
    }

    public WButton getBUY_BUTTON_8() {
        return BUY_BUTTON_8;
    }

    public void setBUY_BUTTON_8(WButton BUY_BUTTON_8) {
        this.BUY_BUTTON_8 = BUY_BUTTON_8;
    }

    public WPlainPanel getItemPane9() {
        return itemPane9;
    }

    public void setItemPane9(WPlainPanel itemPane9) {
        this.itemPane9 = itemPane9;
    }

    public WItemSlot getITEM_SLOT_9() {
        return ITEM_SLOT_9;
    }

    public void setITEM_SLOT_9(WItemSlot ITEM_SLOT_9) {
        this.ITEM_SLOT_9 = ITEM_SLOT_9;
    }

    public WLabel getPRICE_LABEL_9() {
        return PRICE_LABEL_9;
    }

    public void setPRICE_LABEL_9(WLabel PRICE_LABEL_9) {
        this.PRICE_LABEL_9 = PRICE_LABEL_9;
    }

    public WLabel getSELLER_LABEL_9() {
        return SELLER_LABEL_9;
    }

    public void setSELLER_LABEL_9(WLabel SELLER_LABEL_9) {
        this.SELLER_LABEL_9 = SELLER_LABEL_9;
    }

    public WButton getBUY_BUTTON_9() {
        return BUY_BUTTON_9;
    }

    public void setBUY_BUTTON_9(WButton BUY_BUTTON_9) {
        this.BUY_BUTTON_9 = BUY_BUTTON_9;
    }

    public WPlainPanel[] getItemPanes() {
        return itemPanes;
    }

    public void setItemPanes(WPlainPanel[] itemPanes) {
        this.itemPanes = itemPanes;
    }

    public WItemSlot[] getITEM_SLOTS() {
        return ITEM_SLOTS;
    }

    public void setITEM_SLOTS(WItemSlot[] ITEM_SLOTS) {
        this.ITEM_SLOTS = ITEM_SLOTS;
    }

    public WLabel[] getPRICE_LABELS() {
        return PRICE_LABELS;
    }

    public void setPRICE_LABELS(WLabel[] PRICE_LABELS) {
        this.PRICE_LABELS = PRICE_LABELS;
    }

    public WLabel[] getSELLER_LABELS() {
        return SELLER_LABELS;
    }

    public void setSELLER_LABELS(WLabel[] SELLER_LABELS) {
        this.SELLER_LABELS = SELLER_LABELS;
    }

    public WButton[] getBUY_BUTTONS() {
        return BUY_BUTTONS;
    }

    public void setBUY_BUTTONS(WButton[] BUY_BUTTONS) {
        this.BUY_BUTTONS = BUY_BUTTONS;
    }

    public WButton getPreviousPageButton() {
        return previousPageButton;
    }

    public void setPreviousPageButton(WButton previousPageButton) {
        this.previousPageButton = previousPageButton;
    }

    public WPlainPanel getListItemPane() {
        return listItemPane;
    }

    public void setListItemPane(WPlainPanel listItemPane) {
        this.listItemPane = listItemPane;
    }

    public WItemSlot getSlot() {
        return slot;
    }

    public void setSlot(WItemSlot slot) {
        this.slot = slot;
    }

    public WItem getListItemCopperCoinIcon() {
        return listItemCopperCoinIcon;
    }

    public void setListItemCopperCoinIcon(WItem listItemCopperCoinIcon) {
        this.listItemCopperCoinIcon = listItemCopperCoinIcon;
    }

    public WTextField getListItemCopperCoinCountTextField() {
        return listItemCopperCoinCountTextField;
    }

    public void setListItemCopperCoinCountTextField(WTextField listItemCopperCoinCountTextField) {
        this.listItemCopperCoinCountTextField = listItemCopperCoinCountTextField;
    }

    public WItem getListItemSilverCoinIcon() {
        return listItemSilverCoinIcon;
    }

    public void setListItemSilverCoinIcon(WItem listItemSilverCoinIcon) {
        this.listItemSilverCoinIcon = listItemSilverCoinIcon;
    }

    public WTextField getListItemSilverCoinCountTextField() {
        return listItemSilverCoinCountTextField;
    }

    public void setListItemSilverCoinCountTextField(WTextField listItemSilverCoinCountTextField) {
        this.listItemSilverCoinCountTextField = listItemSilverCoinCountTextField;
    }

    public WItem getListItemGoldCoinIcon() {
        return listItemGoldCoinIcon;
    }

    public void setListItemGoldCoinIcon(WItem listItemGoldCoinIcon) {
        this.listItemGoldCoinIcon = listItemGoldCoinIcon;
    }

    public WTextField getListItemGoldCoinCountTextField() {
        return listItemGoldCoinCountTextField;
    }

    public void setListItemGoldCoinCountTextField(WTextField listItemGoldCoinCountTextField) {
        this.listItemGoldCoinCountTextField = listItemGoldCoinCountTextField;
    }

    public WButton getListItemButton() {
        return listItemButton;
    }

    public void setListItemButton(WButton listItemButton) {
        this.listItemButton = listItemButton;
    }

    public WButton getListButton() {
        return listButton;
    }

    public void setListButton(WButton listButton) {
        this.listButton = listButton;
    }

    public WButton getNextPageButton() {
        return nextPageButton;
    }

    public void setNextPageButton(WButton nextPageButton) {
        this.nextPageButton = nextPageButton;
    }

    public SimpleInventory getShow_inventory() {
        return show_inventory;
    }

    public SimpleInventory getInventory() {
        return inventory;
    }

    public WTextField getSearchField() {
        return searchField;
    }

    public void setSearchField(WTextField searchField) {
        this.searchField = searchField;
    }

    public WButton getSearchButton() {
        return searchButton;
    }

    public void setSearchButton(WButton searchButton) {
        this.searchButton = searchButton;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return false;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        // 关闭界面时，处理物品的返还
        dropInventory(player, this.inventory);
    }
}
