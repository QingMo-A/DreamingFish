package com.mo.economy.gui.home_interface;

import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.text.Text;

public class ItemPane {
    public WItemSlot itemSlot;
    public WLabel priceLabel;
    public WLabel sellerLabel;
    public WButton buyButton;

    public WPlainPanel getPanel(SimpleInventory inventory) {
        // 创建一个WPlainPanel来显示物品信息
        WPlainPanel itemPanel = new WPlainPanel();
        itemPanel.setSize(500, 20);  // 设置面板的大小

        // 创建一个不可交互的WItemSlot
        itemSlot = WItemSlot.of(inventory, 0);
        itemSlot.setModifiable(false);  // 禁止玩家与slot互动
        itemSlot.setSize(18, 18);
        itemPanel.add(itemSlot, 30, 1);  // 将itemSlot放置在(0, 0)位置

        // 添加显示价格信息的WLabel
        priceLabel = new WLabel(Text.literal("Copper: " + 0 +
                " | Silver: " + 0 +
                " | Gold: " + 0));
        priceLabel.setSize(100, 20);  // 设置标签的大小
        itemPanel.add(priceLabel, 80, 7);  // 将priceLabel放置在(30, 0)位置

        // 添加显示卖家名称的WLabel
        sellerLabel = new WLabel(Text.literal("1"));
        sellerLabel.setSize(60, 20);  // 设置标签大小
        // itemPanel.add(sellerLabel,  80 + MinecraftClient.getInstance().textRenderer.getWidth(priceLabel.getText().getString()) + 20, 7);  // 将sellerLabel放置在(140, 0)位置
        itemPanel.add(sellerLabel,  itemPanel.getWidth() - 90 - MinecraftClient.getInstance().textRenderer.getWidth(sellerLabel.getText().getString()) - 20, 7);  // 将sellerLabel放置在(140, 0)位置

        // 添加一个"购买"按钮
        buyButton = new WButton(Text.literal("Buy")) {
            @Override
            public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
                super.paint(context, x, y, mouseX, mouseY);  // 确保调用父类的绘制方法
                this.setSize(70, 20);  // 强制按钮大小为100x20
            }
        };
        buyButton.setOnClick(() -> {
            // 实现购买逻辑
        });
        itemPanel.add(buyButton, itemPanel.getWidth() - 90, 0);  // 将buyButton放置在(220, 0)位置

        return itemPanel;
    }
}
