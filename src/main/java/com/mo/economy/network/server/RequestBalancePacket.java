package com.mo.economy.network.server;

import com.mo.economy.MainForServer;
import com.mo.economy.new_economy_system.bank.BankManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;

public class RequestBalancePacket {

    public static final Identifier ID = new Identifier(MainForServer.MOD_ID, "request_balance");

    // 编写请求余额的数据包
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(ID, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                // 获取玩家的余额
                BankManager bankManager = BankManager.get(server); // 获取BankManager实例
                int[] balances = bankManager.getBalance(player);  // 使用BankManager实例调用getBalance
                // 发送余额数据回到客户端
                sendBalanceResponse(player, balances);
            });
        });
    }

    // 发送余额数据给客户端
    private static void sendBalanceResponse(ServerPlayerEntity player, int[] balances) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeIntArray(balances);  // 发送铜币、银币、金币的余额

        ServerPlayNetworking.send(player, new Identifier(MainForServer.MOD_ID, "balance_response"), buf);
    }
}
