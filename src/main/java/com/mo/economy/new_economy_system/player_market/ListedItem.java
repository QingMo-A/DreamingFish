package com.mo.economy.new_economy_system.player_market;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public class ListedItem {
    private UUID itemId; // 商品ID，用于唯一标识
    private String playerName;
    private UUID merchant; // 商家的玩家UUID
    private int copperPrice; // 铜币价格
    private int silverPrice; // 银币价格
    private int goldPrice;   // 金币价格
    private String itemName; // 商品名称
    private ItemStack itemStack; // 商品物品

    public ListedItem(UUID itemId, String playerName, UUID merchant, int copperPrice, int silverPrice, int goldPrice, String itemName, ItemStack itemStack) {
        this.itemId = itemId;
        this.playerName = playerName;
        this.merchant = merchant;
        this.copperPrice = copperPrice;
        this.silverPrice = silverPrice;
        this.goldPrice = goldPrice;
        this.itemName = itemName;
        this.itemStack = itemStack;
    }

    public UUID getItemId() {
        return itemId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public UUID getMerchant() {
        return merchant;
    }

    public int getCopperPrice() {
        return copperPrice;
    }

    public int getSilverPrice() {
        return silverPrice;
    }

    public int getGoldPrice() {
        return goldPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    // 将 ListedItem 数据写入 NBT（可选功能，方便数据保存）
    public void writeToNbt(NbtCompound nbt) {
        nbt.putUuid("ItemId", itemId);
        nbt.putString("PlayerName", playerName);
        nbt.putUuid("Merchant", merchant);
        nbt.putInt("CopperPrice", copperPrice);
        nbt.putInt("SilverPrice", silverPrice);
        nbt.putInt("GoldPrice", goldPrice);
        nbt.putString("ItemName", itemName);

        NbtCompound itemStackNbt = new NbtCompound();
        itemStack.writeNbt(itemStackNbt);
        nbt.put("ItemStack", itemStackNbt);
    }

    // 从 NBT 读取 ListedItem 数据（可选功能，方便数据加载）
    public static ListedItem fromNbt(NbtCompound nbt) {
        UUID itemId = nbt.getUuid("ItemId");
        String playerName = nbt.getString("PlayerName");
        UUID merchant = nbt.getUuid("Merchant");
        int copperPrice = nbt.getInt("CopperPrice");
        int silverPrice = nbt.getInt("SilverPrice");
        int goldPrice = nbt.getInt("GoldPrice");
        String itemName = nbt.getString("ItemName");
        ItemStack itemStack = ItemStack.fromNbt(nbt.getCompound("ItemStack"));

        return new ListedItem(itemId, playerName, merchant, copperPrice, silverPrice, goldPrice, itemName, itemStack);
    }
}
