package com.mo.economy.network.client;

import com.mo.economy.MainForServer;
import com.mo.economy.gui.home_interface.HomeInterface;
import com.mo.economy.gui.home_interface.HomeInterfaceManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class BankLevelResponsePacket {

    public static final Identifier ID = new Identifier(MainForServer.MOD_ID, "bank_level_response");

    // 注册客户端监听器，接收服务器发送的银行等级
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(ID, (client, handler, buf, responseSender) -> {
            int bankLevel = buf.readInt();  // 从数据包中读取银行等级
            UUID playerUUID = client.player.getUuid();
            // 在主线程上执行（通常用于更新UI）
            client.execute(() -> {
                // 在这里处理接收到的银行等级
                // 通过 UUID 获取玩家对应的 HomeInterface 实例
                System.out.println("Received bank level: " + bankLevel + " for player: " + playerUUID);
                HomeInterface homeInterface = HomeInterfaceManager.getPlayerInterface(playerUUID);
                homeInterface.updateBankLevel(bankLevel);
            });
        });
    }
}
