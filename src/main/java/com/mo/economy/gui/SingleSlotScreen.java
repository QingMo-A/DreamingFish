package com.mo.economy.gui;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class SingleSlotScreen extends CottonInventoryScreen<SingleSlotGuiDescription> {
    public SingleSlotScreen(SingleSlotGuiDescription gui, PlayerInventory inventory, Text title) {
        super(gui, inventory.player, title);
    }
}
