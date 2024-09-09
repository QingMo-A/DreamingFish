package com.mo.economy.new_economy_system.bank;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class PlayerBankComponent {

    private final BankAccount bankAccount;

    public PlayerBankComponent() {
        this.bankAccount = new BankAccount();
    }

    // 允许访问银行账户
    public BankAccount getBankAccount() {
        return bankAccount;
    }

    // 保存玩家银行数据到 NBT
    public void writeToNbt(NbtCompound tag) {
        bankAccount.toNbt(tag);
    }

    // 从 NBT 读取玩家银行数据
    public void readFromNbt(NbtCompound tag) {
        bankAccount.fromNbt(tag);
    }
}
