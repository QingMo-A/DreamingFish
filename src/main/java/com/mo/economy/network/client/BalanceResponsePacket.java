package com.mo.economy.network.client;

import com.mo.economy.MainForServer;
import com.mo.economy.gui.home_interface.HomeInterface;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BalanceResponsePacket {

    public static final Identifier ID = new Identifier(MainForServer.MOD_ID, "balance_response");

    // 注册处理余额返回的数据包
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(ID, (client, handler, buf, responseSender) -> {
            int[] balances = buf.readIntArray();  // 读取铜币、银币、金币余额
            client.execute(() -> {
                // 更新客户端的 GUI 或显示余额
                updateBalanceDisplay(balances);
            });
        });
    }

    // 更新客户端的余额显示
    private static void updateBalanceDisplay(int[] balances) {
        // 这里你可以更新 GUI 中的余额显示
        System.out.println("铜币余额: " + balances[0]);
        System.out.println("银币余额: " + balances[1]);
        System.out.println("金币余额: " + balances[2]);

        HomeInterface.updateMoney(balances);
    }
}
