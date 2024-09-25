package com.mo.economy.new_economy_system;

import com.mojang.brigadier.CommandDispatcher;
import com.mo.economy.new_economy_system.player_market.Market;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class ModCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("getMarketItems")
                .executes(context -> executeGetMarketItems(context.getSource())));  // 无需参数，获取所有物品
    }

    private static int executeGetMarketItems(ServerCommandSource source) {
        try {
            // 获取市场实例
            Market market = Market.getInstance(source.getServer());

            // 检查市场是否有物品
            var items = market.getListedItems();
            if (items.isEmpty()) {
                source.sendFeedback(() -> Text.literal("No items in the market."), false);
            } else {
                for (var item : items) {
                    source.sendFeedback(() -> Text.literal("Item: " + item.getItemName() +
                            " | Copper: " + item.getCopperPrice() +
                            " | Silver: " + item.getSilverPrice() +
                            " | Gold: " + item.getGoldPrice()), false);

                }
            }
            return 1;  // 表示成功执行
        } catch (Exception e) {
            // 打印错误堆栈信息
            e.printStackTrace();
            // 向玩家反馈错误信息
            source.sendError(Text.literal("An error occurred while executing the command."));
            return 0;  // 表示执行失败
        }
    }

}
