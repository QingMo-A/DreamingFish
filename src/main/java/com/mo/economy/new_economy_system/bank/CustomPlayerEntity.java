package com.mo.economy.new_economy_system.bank;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import com.mojang.authlib.GameProfile;

public class CustomPlayerEntity extends ServerPlayerEntity {

    private final PlayerBankComponent bankComponent;

    public CustomPlayerEntity(MinecraftServer server, ServerWorld world, GameProfile profile) {
        super(server, world, profile); // 正确调用父类构造函数
        this.bankComponent = new PlayerBankComponent();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        bankComponent.writeToNbt(nbt); // 将银行数据保存到 NBT
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        bankComponent.readFromNbt(nbt); // 从 NBT 中读取银行数据
    }

    public PlayerBankComponent getBankComponent() {
        return bankComponent;
    }
}

