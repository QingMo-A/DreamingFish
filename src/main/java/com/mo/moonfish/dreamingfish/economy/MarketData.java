package com.mo.moonfish.dreamingfish.economy;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MarketData extends PersistentState {
    private final List<MarketItem> marketItems = new ArrayList<>();

    // 添加商品
    public void addItem(MarketItem item) {
        marketItems.add(item);
        markDirty(); // 标记为脏数据，以便 Minecraft 知道需要保存
    }

    // 移除商品
    public void removeItem(UUID itemId) {
        marketItems.removeIf(item -> item.getItemId().equals(itemId));
        markDirty(); // 标记为脏数据，以便 Minecraft 知道需要保存
    }

    public List<MarketItem> getItemsForPage(int page, int itemsPerPage) {
        int start = (page - 1) * itemsPerPage;
        int end = Math.min(start + itemsPerPage, marketItems.size());
        return marketItems.subList(start, end);
    }

    public int getTotalPages(int itemsPerPage) {
        return (int) Math.ceil((double) marketItems.size() / itemsPerPage);
    }


    public MarketItem getItemById(UUID itemId) {
        return marketItems.stream().filter(item -> item.getItemId().equals(itemId)).findFirst().orElse(null);
    }

    // 将市场数据写入 NBT
    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList nbtList = new NbtList();
        for (MarketItem item : marketItems) {
            NbtCompound itemNbt = new NbtCompound();
            item.writeNbt(itemNbt);
            nbtList.add(itemNbt);
        }
        nbt.put("MarketItems", nbtList);
        return nbt;
    }

    // 从 NBT 中读取市场数据
    public static MarketData fromNbt(NbtCompound nbt) {
        MarketData marketData = new MarketData();
        NbtList nbtList = nbt.getList("MarketItems", 10); // 10表示这是一个 NbtCompound 的列表
        for (int i = 0; i < nbtList.size(); i++) {
            NbtCompound itemNbt = nbtList.getCompound(i);
            MarketItem item = MarketItem.fromNbt(itemNbt);
            marketData.addItem(item);
        }
        return marketData;
    }

    // 获取市场数据实例
    public static MarketData getInstance(MinecraftServer server) {
        return server.getOverworld().getPersistentStateManager().getOrCreate(
                MarketData::fromNbt,
                MarketData::new,
                "market_data"
        );
    }

    // MarketData 新增的方法
    public List<MarketItem> getItemsByPlayerName(String playerName) {
        return marketItems.stream()
                .filter(item -> item.getOwnerName().equalsIgnoreCase(playerName))
                .collect(Collectors.toList());
    }

    public List<MarketItem> getItemsByItemName(String itemName) {
        return marketItems.stream()
                .filter(item -> item.getItemName().equalsIgnoreCase(itemName))
                .collect(Collectors.toList());
    }
}
