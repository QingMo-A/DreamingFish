package com.mo.moonfish.dreamingfish.command.commands.economy;

import com.mo.moonfish.dreamingfish.economy.EconomyManager;
import com.mo.moonfish.dreamingfish.economy.MarketData;
import com.mo.moonfish.dreamingfish.economy.MarketItem;
import com.mo.moonfish.dreamingfish.economy.message.MarketMessage;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MarketCommand {

    private static MarketData marketData = new MarketData();

    // 注册命令
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            // 市场
            dispatcher.register(CommandManager.literal("market")
                    .then(CommandManager.argument("page", IntegerArgumentType.integer(1))
                            .executes(context -> MarketCommand.handleMarketCommand(context.getSource(), IntegerArgumentType.getInteger(context, "page")))));

            // 上架
            dispatcher.register(CommandManager.literal("listitem")
                    .then(CommandManager.argument("price", DoubleArgumentType.doubleArg())
                            .then(CommandManager.argument("quantity", IntegerArgumentType.integer(1))
                                    .executes(context -> MarketCommand.handleListItemCommand(context.getSource(), DoubleArgumentType.getDouble(context, "price"), IntegerArgumentType.getInteger(context, "quantity"))))));

            // 购买
            dispatcher.register(CommandManager.literal("buyitem")
                    .then(CommandManager.argument("itemId", UuidArgumentType.uuid())
                            .then(CommandManager.argument("price", DoubleArgumentType.doubleArg())
                                    .executes(context -> MarketCommand.handleBuyItemCommand(context.getSource(), UuidArgumentType.getUuid(context, "itemId"), DoubleArgumentType.getDouble(context, "price"))))));

            // 搜索
            dispatcher.register(CommandManager.literal("marketitem")
                    .then(CommandManager.literal("search")
                            .then(CommandManager.literal("player")
                                    .then(CommandManager.argument("playername", StringArgumentType.string())
                                            .then(CommandManager.argument("page", IntegerArgumentType.integer(1))
                                                    .executes(context -> MarketCommand.handleSearchByPlayerCommand(
                                                            context.getSource(),
                                                            StringArgumentType.getString(context, "playername"),
                                                            IntegerArgumentType.getInteger(context, "page")
                                                    )))))
                            .then(CommandManager.literal("item")
                                    .then(CommandManager.argument("itemname", StringArgumentType.string())
                                            .then(CommandManager.argument("page", IntegerArgumentType.integer(1))
                                                    .executes(context -> MarketCommand.handleSearchByItemNameCommand(
                                                            context.getSource(),
                                                            StringArgumentType.getString(context, "itemname"),
                                                            IntegerArgumentType.getInteger(context, "page")
                                                    )))))));
        });
    }

    // 市场指令处理器
    public static int handleMarketCommand(ServerCommandSource source, int page) {
        // 获取玩家实例
        ServerPlayerEntity player = source.getPlayer();
        // 获取服务器实例
        MinecraftServer server = source.getServer();
        MarketData marketData = MarketData.getInstance(server); // 获取市场数据实例
        Text marketMessage = MarketMessage.buildMarketMessage(server, page, marketData);
        player.sendMessage(marketMessage, false);
        return 1;
    }

    // 上架物品指令处理器
    public static int handleListItemCommand(ServerCommandSource source, double price, int quantity) {
        // 获取商品税
        double taxes = price * 0.05;

        // 获取玩家实例
        ServerPlayerEntity player = source.getPlayer();

        // 获取玩家余额
        double player_balance = EconomyManager.get(source.getServer()).getBalance(player);
        // 如果玩家余额 < 税款
        if (player_balance < taxes) {
            player.sendMessage(Text.translatable("market.insufficient_funds_for_taxes", taxes).formatted(Formatting.RED), false);
            return 0;
        }

        // 获取玩家名称
        String playerName = player.getName().getString();
        // 获取物品实例
        ItemStack itemStack = player.getMainHandStack();

        // 如果物品为空
        if (itemStack.isEmpty()) {
            player.sendMessage(Text.translatable("market.no_items_held").formatted(Formatting.RED), false);
            return 0;
        }

        // 如果物品数量 < 提交的数量
        if (itemStack.getCount() < quantity) {
            player.sendMessage(Text.translatable("market.insufficient_items").formatted(Formatting.RED), false);
            return 0;
        }

        // 从玩家物品栏移除上架数量的物品
        ItemStack stackForMarket = itemStack.split(quantity);

        // 检查剩余的物品堆栈，如果空了，将其设置为空堆栈
        if (itemStack.isEmpty()) {
            player.getInventory().setStack(player.getInventory().selectedSlot, ItemStack.EMPTY);
        }

        // 生成随机商品uid
        UUID itemId = UUID.randomUUID();
        // 创建新的商品实例
        MarketItem marketItem = new MarketItem(itemId, stackForMarket, price, player.getUuid(), playerName);
        MarketData marketData = MarketData.getInstance(source.getServer());
        marketData.addItem(marketItem);
        Text itemName = marketItem.getItemStack().getName();

        // 强制更新玩家物品栏
        player.currentScreenHandler.sendContentUpdates();

        player.sendMessage(Text.translatable("market.item_listed", itemName).formatted(Formatting.GREEN), false);

        // 向所有在线玩家发送消息
        for (ServerPlayerEntity onlinePlayer : source.getServer().getPlayerManager().getPlayerList()) {
            if (onlinePlayer != player) {
                onlinePlayer.sendMessage(Text.translatable("market.item_listed_for_other_players", playerName, itemName).formatted(Formatting.GREEN), false);
            }
        }
        return 1;
    }

    // 搜索物品指令处理器
    public static int handleSearchByPlayerCommand(ServerCommandSource source, String playerName, int page) {
        ServerPlayerEntity player = source.getPlayer();
        MinecraftServer server = source.getServer();
        MarketData marketData = MarketData.getInstance(server);

        List<MarketItem> items = marketData.getItemsByPlayerName(playerName);
        Text marketMessage = MarketMessage.buildMarketMessage(server, page, items, playerName, true);
        player.sendMessage(marketMessage, false);
        return 1;
    }

    public static int handleSearchByItemNameCommand(ServerCommandSource source, String itemName, int page) {
        ServerPlayerEntity player = source.getPlayer();
        MinecraftServer server = source.getServer();
        MarketData marketData = MarketData.getInstance(server);

        List<MarketItem> items = marketData.getItemsByItemName(itemName);
        Text marketMessage = MarketMessage.buildMarketMessage(server, page, items, itemName, false);
        player.sendMessage(marketMessage, false);
        return 1;
    }

    // 购买物品指令处理器
    public static int handleBuyItemCommand(ServerCommandSource source, UUID itemId, double price) {
        ServerPlayerEntity player = source.getPlayer();
        MinecraftServer server = source.getServer();

        MarketData marketData = MarketData.getInstance(server); // 获取市场数据实例
        MarketItem item = marketData.getItemById(itemId);
        if (item == null) {
            player.sendMessage(Text.translatable("market.item_sold_out_or_not_available").formatted(Formatting.RED), false);
            return 0;
        }

        double playerBalance = EconomyManager.get(server).getBalance(player);
        if (playerBalance < price) {
            player.sendMessage(Text.translatable("market.insufficient_balance").formatted(Formatting.RED), false);
            return 0;
        }

        boolean hasEmptySlot = player.getInventory().getEmptySlot() != -1;
        if (!hasEmptySlot) {
            player.sendMessage(Text.translatable("market.inventory_full").formatted(Formatting.RED), false);
            return 0;
        }

        // 扣除买家的金额
        EconomyManager.get(server).removeBalance(player, price);

        // 增加卖家的金额
        ServerPlayerEntity owner = server.getPlayerManager().getPlayer(item.getOwner());
        if (owner != null) {
            EconomyManager.get(server).addBalance(owner, price);
            owner.sendMessage(Text.translatable("market.dreamfish_coins_earned", String.format("%.2f", price)).formatted(Formatting.GREEN), false);
        } else {
            // 如果卖家不在线，可以考虑记录下来或者给卖家发送邮件等
            EconomyManager.get(server).addBalanceByUUID(item.getOwner(), price);
        }

        // 将物品添加到买家的物品栏
        player.getInventory().insertStack(item.getItemStack());
        player.sendMessage(Text.translatable("market.success_buy").formatted(Formatting.GREEN), false);

        // 从市场中移除该物品
        marketData.removeItem(itemId);

        return 1;
    }
}

