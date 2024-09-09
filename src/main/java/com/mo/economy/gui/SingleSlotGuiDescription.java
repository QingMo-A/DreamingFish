package com.mo.economy.gui;

import com.mo.economy.MainForServer;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandlerContext;

public class SingleSlotGuiDescription extends SyncedGuiDescription {

    // 创建一个自定义的库存，包含1个槽位
    private final SimpleInventory inventory = new SimpleInventory(1);

    public SingleSlotGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ScreenHandlers.SINGLE_SLOT_SCREEN_HANDLER, syncId, playerInventory);  // 传递参数给父类
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(186, 166);

        // 创建一个自定义的物品槽，并连接到自定义库存（customInventory）
        WItemSlot slot = WItemSlot.of(inventory, 0);  // 物品槽连接到自定义的SimpleInventory
        root.add(slot, 4, 1);  // 将槽位添加到网格布局中

        // 添加玩家物品栏
        root.add(this.createPlayerInventoryPanel(), 1, 4);  // 在面板底部添加玩家物品栏

        root.validate(this);
    }
}
