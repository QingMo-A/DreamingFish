package com.mo.economy.network.client;

import com.mo.economy.MainForServer;
import com.mo.economy.gui.home_interface.HomeInterface;
import com.mo.economy.gui.home_interface.HomeInterfaceManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class BalanceResponsePacket {

    public static final Identifier ID = new Identifier(MainForServer.MOD_ID, "balance_response");

    // 注册处理余额返回的数据包
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(ID, (client, handler, buf, responseSender) -> {
            int[] balances = buf.readIntArray();  // 读取铜币、银币、金币余额
            UUID playerUUID = client.player.getUuid();  // 获取当前玩家 UUID
            client.execute(() -> {
                // 通过 UUID 获取玩家对应的 HomeInterface 实例
                HomeInterface homeInterface = HomeInterfaceManager.getPlayerInterface(playerUUID);
                System.out.println(homeInterface == null);
                if (homeInterface != null) {
                    // 更新 HomeInterface 中的银行数据
                    homeInterface.updateBalance(balances);
                }
            });
        });
    }
}
