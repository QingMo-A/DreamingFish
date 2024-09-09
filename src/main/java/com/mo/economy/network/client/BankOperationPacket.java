package com.mo.economy.network.client;

import com.mo.economy.MainForServer;
import com.mo.economy.new_economy_system.bank.BankManager;
import net.minecraft.network.PacketByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.minecraft.server.network.ServerPlayerEntity;

public class BankOperationPacket {

    public static final Identifier ID = new Identifier(MainForServer.MOD_ID, "bank_operation");

    private final String operationType;  // deposit or withdraw
    private final String coinType;  // copper_coin, silver_coin, gold_coin
    private final int amount;  // amount of coins

    public BankOperationPacket(String operationType, String coinType, int amount) {
        this.operationType = operationType;
        this.coinType = coinType;
        this.amount = amount;
    }

    // 编写写入数据的方法，用于发送数据包
    public void write(PacketByteBuf buf) {
        buf.writeString(operationType);
        buf.writeString(coinType);
        buf.writeInt(amount);
    }

    // 解码数据包
    public static BankOperationPacket read(PacketByteBuf buf) {
        String operationType = buf.readString();
        String coinType = buf.readString();
        int amount = buf.readInt();
        return new BankOperationPacket(operationType, coinType, amount);
    }

    // 注册并处理数据包
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(ID, (server, player, handler, buf, responseSender) -> {
            BankOperationPacket packet = BankOperationPacket.read(buf);
            server.execute(() -> {
                BankManager bankManager = BankManager.get(server);  // 创建 BankManager 实例
                handleBankOperation(player, packet, bankManager);
            });
        });
    }

    // 在服务端处理银行操作
    private static void handleBankOperation(ServerPlayerEntity player, BankOperationPacket packet, BankManager bankManager) {
        System.out.println("handleBankOperation   " + packet.operationType);
        if ("deposit".equals(packet.operationType)) {
            bankManager.deposit(player, packet.coinType, packet.amount);
        } else if ("withdraw".equals(packet.operationType)) {
            bankManager.withdraw(player, packet.coinType, packet.amount);
        }
    }
}
