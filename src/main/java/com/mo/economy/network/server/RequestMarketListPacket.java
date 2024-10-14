package com.mo.economy.network.server;

import com.mo.economy.MainForServer;
import com.mo.economy.network.client.MarketListResponsePacket;
import com.mo.economy.new_economy_system.player_market.ListedItem;
import com.mo.economy.new_economy_system.player_market.Market;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class RequestMarketListPacket {
    public static final Identifier ID = new Identifier(MainForServer.MOD_ID, "request_market_list");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(ID, (server, player, handler, buf, responseSender) -> {
            // 处理市场列表请求
            server.execute(() -> handleMarketListRequest(player));
        });
    }

    private static void handleMarketListRequest(ServerPlayerEntity player) {
        // 获取市场实例并获取商品列表
        Market market = Market.getInstance(player.getServer());
        List<ListedItem> listedItems = market.getListedItems();

        // 创建返回的数据包
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(listedItems.size());

        for (ListedItem item : listedItems) {
            buf.writeUuid(item.getItemId());
            buf.writeString(item.getPlayerName());
            buf.writeUuid(item.getMerchant());
            buf.writeString(item.getItemName());
            buf.writeInt(item.getCopperPrice());
            buf.writeInt(item.getSilverPrice());
            buf.writeInt(item.getGoldPrice());
            buf.writeItemStack(item.getItemStack()); // 写入物品数据
        }

        // 发送商品列表到客户端
        ServerPlayNetworking.send(player, MarketListResponsePacket.ID, buf);
    }
}
