package com.mo.economy.network.server;

import com.mo.economy.MainForServer;
import com.mo.economy.network.client.BankLevelResponsePacket;
import com.mo.economy.new_economy_system.bank.BankManager;
import net.minecraft.network.PacketByteBuf;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class RequestBankLevelPacket {

    public static final Identifier ID = new Identifier(MainForServer.MOD_ID, "request_bank_level");

    // 注册数据包处理器
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(ID, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                // 获取玩家的银行等级
                int level = BankManager.get(server).getBankAccount(player).getLevel();

                // 将银行等级发送回客户端
                sendBankLevelResponse(player, level);
            });
        });
    }

    // 发送银行等级给客户端
    private static void sendBankLevelResponse(ServerPlayerEntity player, int level) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(level);  // 写入银行等级

        ServerPlayNetworking.send(player, BankLevelResponsePacket.ID, buf);
    }
}
