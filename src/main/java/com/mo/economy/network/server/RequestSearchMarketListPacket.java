package com.mo.economy.network.server;

import com.mo.economy.MainForServer;
import com.mo.economy.gui.home_interface.HomeInterface;
import com.mo.economy.gui.home_interface.HomeInterfaceManager;
import com.mo.economy.network.client.MarketListResponsePacket;
import com.mo.economy.network.client.SearchMarketListResponsePacket;
import com.mo.economy.new_economy_system.player_market.ListedItem;
import com.mo.economy.new_economy_system.player_market.Market;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RequestSearchMarketListPacket {
    public static final Identifier ID = new Identifier(MainForServer.MOD_ID, "request_search_market_list");

    // 在客户端发送数据包的方法
    public static void sendSearchMarketListPacket(String itemName) {
        PacketByteBuf buf = PacketByteBufs.create();

        // 序列化数据
        buf.writeString(itemName);

        // 客户端发送数据包到服务端
        ClientPlayNetworking.send(ID, buf);
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(ID, (server, player, handler, buf, responseSender) -> {
            String itemName = buf.readString();

            // 处理市场列表请求
            server.execute(() -> handleMarketListRequest(player, itemName));
        });
    }

    private static void handleMarketListRequest(ServerPlayerEntity player, String itemName) {
        // 获取市场实例并获取商品列表
        Market market = Market.getInstance(player.getServer());
        // List<ListedItem> listedItems = market.getListedItems();
        List<ListedItem> listedItems = market.searchItemsByName(itemName);

        PacketByteBuf buf = PacketByteBufs.create();
        if (!(listedItems.isEmpty())) {
            System.out.println("items is not empty!!!");
            // 创建返回的数据包
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
            ServerPlayNetworking.send(player, SearchMarketListResponsePacket.ID, buf);
        } else {
            System.out.println("items is empty!!!");
            buf.writeInt(0);
            // 发送商品列表到客户端
            ServerPlayNetworking.send(player, SearchMarketListResponsePacket.ID, buf);
        }
    }
}
