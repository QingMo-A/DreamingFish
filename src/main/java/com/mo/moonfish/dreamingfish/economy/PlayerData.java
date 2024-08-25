package com.mo.moonfish.dreamingfish.economy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;

import java.util.*;

public class PlayerData extends PersistentState {
    private final Map<UUID, List<ItemStack>> cartItems = new HashMap<>();
    private final Map<UUID, List<ItemStack>> retrievalItems = new HashMap<>();

    public List<ItemStack> getCartItems(PlayerEntity player) {
        return cartItems.getOrDefault(player.getUuid(), new ArrayList<>());
    }

    public List<ItemStack> getRetrievalItems(PlayerEntity player) {
        return retrievalItems.getOrDefault(player.getUuid(), new ArrayList<>());
    }

    public void addItemToCart(PlayerEntity player, ItemStack item) {
        cartItems.computeIfAbsent(player.getUuid(), k -> new ArrayList<>()).add(item);
        markDirty();
    }

    public void addItemToRetrieval(PlayerEntity player, ItemStack item) {
        retrievalItems.computeIfAbsent(player.getUuid(), k -> new ArrayList<>()).add(item);
        markDirty();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        // 序列化玩家数据
        return nbt;
    }

    public static PlayerData fromNbt(NbtCompound nbt) {
        PlayerData data = new PlayerData();
        // 反序列化玩家数据
        return data;
    }

    public static PlayerData get(MinecraftServer server) {
        return server.getOverworld().getPersistentStateManager().getOrCreate(
                PlayerData::fromNbt, PlayerData::new, "player_data");
    }
}
