package com.mo.economy.network.client;

import com.mo.economy.MainForServer;
import com.mo.economy.new_economy_system.player_market.Market;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class RemoveListItemPacket {
    public static final Identifier ID = new Identifier(MainForServer.MOD_ID, "remove_list_item");

    // 在客户端发送数据包的方法
    public static void sendListItemPacket(UUID itemId) {
        PacketByteBuf buf = PacketByteBufs.create();

        // 序列化数据
        buf.writeUuid(itemId);

        // 客户端发送数据包到服务端
        ClientPlayNetworking.send(ID, buf);
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(ID, (server, player, handler, buf, responseSender) -> {
            UUID itemId = buf.readUuid();

            // 在服务端执行商品上架操作
            server.execute(() -> handleRemoveListItem(player, itemId));
        });
    }

    private static void handleRemoveListItem(ServerPlayerEntity player, UUID itemId) {
        // 将商品添加到市场系统
        Market market = Market.getInstance(player.getServer()); // 获取市场实例
        market.removeItemById(itemId);

        // 通知玩家上架成功
        // player.sendMessage(Text.translatable("gui.home_interface.list_item_successful"), false);
    }
}
