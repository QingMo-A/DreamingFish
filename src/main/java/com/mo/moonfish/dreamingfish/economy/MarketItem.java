package com.mo.moonfish.dreamingfish.economy;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public class MarketItem {
    private final UUID itemId;
    private final ItemStack itemStack;
    private final double price;
    private final UUID owner;
    private final String ownerName; // 添加所有者名字字段

    public MarketItem(UUID itemId, ItemStack itemStack, double price, UUID owner, String ownerName) {
        this.itemId = itemId;
        this.itemStack = itemStack;
        this.price = price;
        this.owner = owner;
        this.ownerName = ownerName; // 初始化所有者名字字段
    }

    public UUID getItemId() {
        return itemId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getItemName() {
        return itemStack.getName().getString();
    }

    // 新增的获取数量的方法
    public int getQuantity() {
        return itemStack.getCount();  // 获取上架物品的数量
    }

    public double getPrice() {
        return price;
    }

    public UUID getOwner() {
        return owner;
    }

    // 将 MarketItem 写入 NBT
    public void writeNbt(NbtCompound nbt) {
        nbt.putUuid("ItemId", itemId);

        NbtCompound itemStackNbt = new NbtCompound();
        itemStack.writeNbt(itemStackNbt);
        nbt.put("ItemStack", itemStackNbt);

        nbt.putDouble("Price", price);
        nbt.putUuid("Owner", owner);

        // 存储所有者的名字
        nbt.putString("OwnerName", ownerName);
    }


    // 从 NBT 中读取 MarketItem
    public static MarketItem fromNbt(NbtCompound nbt) {
        UUID itemId = nbt.getUuid("ItemId");
        ItemStack itemStack = ItemStack.fromNbt(nbt.getCompound("ItemStack"));
        double price = nbt.getDouble("Price");
        UUID owner = nbt.getUuid("Owner");

        // 从 NBT 中读取所有者的名字
        String ownerName = nbt.getString("OwnerName");

        return new MarketItem(itemId, itemStack, price, owner, ownerName);
    }

}
