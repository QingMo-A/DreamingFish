package com.mo.economy.gui;

import com.mo.economy.MainForServer;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class HomePane extends WPlainPanel {
    // 自定义背景材质的 Identifier
    private static final Identifier CUSTOM_BACKGROUND = new Identifier(MainForServer.MOD_ID, "textures/gui/background.png");

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
        // 调用父类的 paint 方法（绘制子组件），这将绘制按钮等其他组件
        super.paint(context, x, y, mouseX, mouseY);

        // 绘制背景图片，确保它在所有组件之前绘制
        context.drawTexture(CUSTOM_BACKGROUND, x, y, 0, 0, this.getWidth(), this.getHeight());
    }
}
