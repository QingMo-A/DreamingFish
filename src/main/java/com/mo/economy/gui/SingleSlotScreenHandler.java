package com.mo.economy.gui;

import com.mo.economy.MainForServer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class SingleSlotScreenHandler extends ScreenHandler {

    public SingleSlotScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ScreenHandlers.SINGLE_SLOT_SCREEN_HANDLER, syncId);

        // 添加一个物品槽
        this.addSlot(new Slot(playerInventory, 0, 80, 35)); // 格子的屏幕位置（x=80, y=35）

        // 玩家物品栏槽位
        int m, l;
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }

        // 热键栏槽位
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    // 实现 quickMove 方法
    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasStack()) {
            ItemStack currentStack = slot.getStack();
            itemStack = currentStack.copy();

            // 检查点击的是哪个槽位
            if (index < 1) {
                // 将物品从自定义格子移到玩家物品栏
                if (!this.insertItem(currentStack, 1, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // 将物品从玩家物品栏移到自定义格子
                if (!this.insertItem(currentStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (currentStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return itemStack;
    }
}
