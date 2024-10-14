package com.mo.economy.network.client;

import com.mo.economy.MainForServer;
import com.mo.economy.new_economy_system.player_market.ListedItem;
import com.mo.economy.new_economy_system.player_market.Market;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class ListItemPacket {

    public static final Identifier ID = new Identifier(MainForServer.MOD_ID, "list_item");

    // 在客户端发送数据包的方法
    public static void sendListItemPacket(UUID itemId,String playerName, UUID merchantId, int copperPrice, int silverPrice, int goldPrice, ItemStack itemStack) {
        PacketByteBuf buf = PacketByteBufs.create();

        // 序列化数据
        buf.writeUuid(itemId);
        buf.writeString(playerName);
        buf.writeUuid(merchantId);
        buf.writeInt(copperPrice);
        buf.writeInt(silverPrice);
        buf.writeInt(goldPrice);
        buf.writeItemStack(itemStack);

        // 客户端发送数据包到服务端
        ClientPlayNetworking.send(ID, buf);
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(ID, (server, player, handler, buf, responseSender) -> {
            UUID itemId = buf.readUuid();
            String playerName = buf.readString();
            UUID merchantId = buf.readUuid();
            int copperPrice = buf.readInt();
            int silverPrice = buf.readInt();
            int goldPrice = buf.readInt();
            ItemStack itemStack = buf.readItemStack();

            // 在服务端执行商品上架操作
            server.execute(() -> handleListItem(player, itemId, playerName, merchantId, copperPrice, silverPrice, goldPrice, itemStack));
        });
    }

    private static void handleListItem(ServerPlayerEntity player, UUID itemId, String playerName, UUID merchantId, int copperPrice, int silverPrice, int goldPrice, ItemStack itemStack) {
        // 构建 ListedItem
        ListedItem listedItem = new ListedItem(itemId, playerName, merchantId, copperPrice, silverPrice, goldPrice, itemStack.getName().getString(), itemStack);

        // 将商品添加到市场系统
        Market market = Market.getInstance(player.getServer()); // 获取市场实例
        market.listItem(listedItem);

        // 通知玩家上架成功
        player.sendMessage(Text.translatable("gui.home_interface.list_item_successful"), false);
    }
}
