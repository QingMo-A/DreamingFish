package com.mo.economy.new_economy_system.player_market;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Market extends PersistentState {
    private final List<ListedItem> listedItems = new ArrayList<>();

    // 上架商品
    public void listItem(ListedItem item) {
        listedItems.add(0, item);
        markDirty(); // 标记数据已更改以保存
    }

    // 获取所有商品
    public List<ListedItem> getListedItems() {
        return listedItems;
    }

    // 根据商家 UUID 获取商家的商品
    public List<ListedItem> getItemsByMerchant(UUID merchantId) {
        return listedItems.stream()
                .filter(item -> item.getMerchant().equals(merchantId))
                .collect(Collectors.toList());
    }

    // 根据商品名称搜索商品
    public List<ListedItem> searchItemsByName(String itemName) {
        return listedItems.stream()
                .filter(item -> item.getItemName().equalsIgnoreCase(itemName))
                .collect(Collectors.toList());
    }

    // 分页获取商品列表
    public List<ListedItem> getItemsForPage(int page, int itemsPerPage) {
        int start = (page - 1) * itemsPerPage;
        int end = Math.min(start + itemsPerPage, listedItems.size());
        return listedItems.subList(start, end);
    }

    // 获取商品总页数
    public int getTotalPages(int itemsPerPage) {
        return (int) Math.ceil((double) listedItems.size() / itemsPerPage);
    }

    // 移除指定商家的商品（根据 UUID）
    public void removeItemsByMerchant(UUID merchantId) {
        listedItems.removeIf(item -> item.getMerchant().equals(merchantId));
        markDirty();
    }

    // 移除商品根据商品 ID
    public void removeItemById(UUID itemId) {
        listedItems.removeIf(item -> item.getItemId().equals(itemId));
        markDirty();
    }

    // 从 NBT 读取市场数据
    public static Market fromNbt(NbtCompound nbt) {
        Market market = new Market();
        NbtList nbtList = nbt.getList("MarketItems", 10); // 10 表示 NbtCompound 列表
        for (int i = 0; i < nbtList.size(); i++) {
            NbtCompound itemNbt = nbtList.getCompound(i);
            ListedItem item = ListedItem.fromNbt(itemNbt);
            market.listItem(item);
        }
        return market;
    }

    // 将市场数据写入 NBT
    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList nbtList = new NbtList();
        for (ListedItem item : listedItems) {
            NbtCompound itemNbt = new NbtCompound();
            item.writeToNbt(itemNbt);
            nbtList.add(itemNbt);
        }
        nbt.put("MarketItems", nbtList);
        return nbt;
    }

    // 获取市场实例
    public static Market getInstance(MinecraftServer server) {
        return server.getOverworld().getPersistentStateManager().getOrCreate(
                Market::fromNbt,
                Market::new,
                "market_data"
        );
    }
}
