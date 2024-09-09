package com.mo.economy.gui.home_interface;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class HomeInterfaceScreen extends CottonInventoryScreen<HomeInterface> {
    private Text dynamicTitle; // 用于存储动态更新的标题

    public HomeInterfaceScreen(HomeInterface gui, PlayerInventory playerInventory, Text title) {
        super(gui, playerInventory.player, title);
        this.dynamicTitle = title; // 初始化标题
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // 绘制自定义的动态标题
        context.drawText(textRenderer, dynamicTitle, this.x + 10, this.y + 7, 0x000000, false);
    }

    // 提供一个方法来更新标题
    public void updateTitle(Text newTitle) {
        this.dynamicTitle = newTitle;
    }
}
