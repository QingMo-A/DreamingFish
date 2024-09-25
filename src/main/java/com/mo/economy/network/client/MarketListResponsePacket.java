package com.mo.economy.network.client;

import com.mo.economy.MainForServer;
import com.mo.economy.gui.home_interface.HomeInterface;
import com.mo.economy.new_economy_system.player_market.ListedItem;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MarketListResponsePacket {

    public static final Identifier ID = new Identifier(MainForServer.MOD_ID, "market_list_response");
    public static List<ListedItem> marketItems = new ArrayList<>();

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(ID, (client, handler, buf, responseSender) -> {
            // 在客户端接收并处理市场列表响应
            int size = buf.readInt();
            List<ListedItem> receivedItems = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                UUID itemId = buf.readUuid();
                String playerName = buf.readString();
                UUID playerUUID = buf.readUuid();
                String itemName = buf.readString();
                int copperPrice = buf.readInt();
                int silverPrice = buf.readInt();
                int goldPrice = buf.readInt();
                ItemStack itemStack = buf.readItemStack();

                ListedItem listedItem = new ListedItem(itemId, playerName, playerUUID, copperPrice, silverPrice, goldPrice, itemName, itemStack);
                receivedItems.add(listedItem);
            }

            client.execute(() -> {
                marketItems.clear();
                marketItems.addAll(receivedItems); // 将收到的商品列表添加到全局列表中

                // 输出接收到的商品列表到控制台
                System.out.println("Received Market Items:");
                for (ListedItem item : marketItems) {
                    System.out.println("Item Name: " + item.getItemName() +
                            " | Copper: " + item.getCopperPrice() +
                            " | Silver: " + item.getSilverPrice() +
                            " | Gold: " + item.getGoldPrice());
                }
                HomeInterface.marketItems = marketItems;
                HomeInterface.totalItems = marketItems.size();
                HomeInterface.totalPages = Math.max(1, (int) Math.ceil((double) HomeInterface.totalItems / HomeInterface.itemsPerPage));
                HomeInterface.update(HomeInterface.page);
            });
        });
    }

    // 获取市场商品列表
    public static List<ListedItem> getMarketItems() {
        return marketItems;
    }
}
