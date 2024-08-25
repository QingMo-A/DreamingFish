package com.mo.moonfish.dreamingfish.command.commands.economy;

import com.mo.moonfish.dreamingfish.economy.EconomyManager;
import com.mo.moonfish.dreamingfish.economy.MarketData;
import com.mo.moonfish.dreamingfish.economy.MarketItem;
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

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("market")
                    .then(CommandManager.argument("page", IntegerArgumentType.integer(1))
                            .executes(context -> MarketCommand.handleMarketCommand(context.getSource(), IntegerArgumentType.getInteger(context, "page")))));

            dispatcher.register(CommandManager.literal("listitem")
                    .then(CommandManager.argument("price", DoubleArgumentType.doubleArg())
                            .then(CommandManager.argument("quantity", IntegerArgumentType.integer(1))
                                    .executes(context -> MarketCommand.handleListItemCommand(context.getSource(), DoubleArgumentType.getDouble(context, "price"), IntegerArgumentType.getInteger(context, "quantity"))))));

            dispatcher.register(CommandManager.literal("buyitem")
                    .then(CommandManager.argument("itemId", UuidArgumentType.uuid())
                            .then(CommandManager.argument("price", DoubleArgumentType.doubleArg())
                                    .executes(context -> MarketCommand.handleBuyItemCommand(context.getSource(), UuidArgumentType.getUuid(context, "itemId"), DoubleArgumentType.getDouble(context, "price"))))));
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

    public static int handleMarketCommand(ServerCommandSource source, int page) {
        ServerPlayerEntity player = source.getPlayer();
        MinecraftServer server = source.getServer();
        MarketData marketData = MarketData.getInstance(server); // 获取市场数据实例
        Text marketMessage = buildMarketMessage(server, page, marketData);
        player.sendMessage(marketMessage, false);
        return 1;
    }

    public static int handleListItemCommand(ServerCommandSource source, double price, int quantity) {
        double taxes = price * 0.05;

        ServerPlayerEntity player = source.getPlayer();

        double player_balance = EconomyManager.get(source.getServer()).getBalance(player);
        if (player_balance < taxes) {
            player.sendMessage(Text.translatable("market.insufficient_funds_for_taxes", taxes).formatted(Formatting.RED), false);
            return 0;
        }

        String playerName = player.getName().getString();
        ItemStack itemStack = player.getMainHandStack();

        if (itemStack.isEmpty()) {
            player.sendMessage(Text.translatable("market.no_items_held").formatted(Formatting.RED), false);
            return 0;
        }

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

        UUID itemId = UUID.randomUUID();
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

    public static int handleSearchByPlayerCommand(ServerCommandSource source, String playerName, int page) {
        ServerPlayerEntity player = source.getPlayer();
        MinecraftServer server = source.getServer();
        MarketData marketData = MarketData.getInstance(server);

        List<MarketItem> items = marketData.getItemsByPlayerName(playerName);
        Text marketMessage = buildMarketMessage(server, page, items, playerName, true);
        player.sendMessage(marketMessage, false);
        return 1;
    }

    public static int handleSearchByItemNameCommand(ServerCommandSource source, String itemName, int page) {
        ServerPlayerEntity player = source.getPlayer();
        MinecraftServer server = source.getServer();
        MarketData marketData = MarketData.getInstance(server);

        List<MarketItem> items = marketData.getItemsByItemName(itemName);
        Text marketMessage = buildMarketMessage(server, page, items, itemName, false);
        player.sendMessage(marketMessage, false);
        return 1;
    }

    // 根据物品列表构建市场消息
    private static MutableText buildMarketMessage(MinecraftServer server, int page, List<MarketItem> items, String name, boolean byPlayer) {
        String command;
        if (byPlayer) {
            command = "player";
        } else {
            command = "item";
        }
        MutableText header = Text.translatable("market.title").formatted(Formatting.GOLD);

        int itemsPerPage = 8;  // 每页显示的商品数量
        int totalItems = items.size();
        int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / itemsPerPage));

        // 确保当前页数在合理范围内
        page = Math.min(page, totalPages);
        page = Math.max(page, 1);

        int startIndex = (page - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);
        List<MarketItem> itemsForPage = items.subList(startIndex, endIndex);

        MutableText itemList = Text.literal("");
        for (MarketItem item : itemsForPage) {
            itemList.append(Text.literal(item.getItemName() + " x" + item.getQuantity()).formatted(Formatting.YELLOW))
                    .append(Text.literal(" | ").formatted(Formatting.WHITE))
                    .append(Text.literal(item.getOwnerName()).formatted(Formatting.AQUA))
                    .append(Text.literal(" | ").formatted(Formatting.WHITE))
                    .append(Text.literal(String.format("%.2f", item.getPrice())).formatted(Formatting.GOLD))
                    .append(Text.literal(" "))
                    .append(createBuyButton(item.getItemId(), item.getPrice()))
                    .append(Text.literal(" "))
                    .append(createInfoButton(item.getItemStack()))
                    .append(Text.literal("\n"));
        }

        // 如果当前页没有物品，显示 <empty> 占位符
        int emptySlots = itemsPerPage - itemsForPage.size();
        for (int i = 0; i < emptySlots; i++) {
            itemList.append(Text.translatable("market.empty").formatted(Formatting.DARK_GRAY))
                    .append(Text.literal("\n"));
        }

        // 构建页码和翻页按钮
        MutableText footer = Text.literal("")
                .append(createPageButton("[<-]", "/marketitem search " + command + " \"" + name + "\" " + (page > 1 ? page - 1 : 1)))
                .append(Text.literal("====| ").formatted(Formatting.GOLD))
                .append(Text.literal(page + "/" + totalPages).formatted(Formatting.YELLOW))
                .append(Text.literal(" |====").formatted(Formatting.GOLD))
                .append(createPageButton("[->]", "/marketitem search " + command + " \"" + name + "\" " + (page < totalPages ? page + 1 : totalPages)));

        return Text.literal("")
                .append(header).append("\n")
                .append(itemList)
                .append(footer);
    }

    // 根据物品列表构建市场消息
    private static MutableText buildMarketMessage(MinecraftServer server, List<MarketItem> items) {
        MutableText header = Text.translatable("market.title").formatted(Formatting.GOLD);

        MutableText itemList = Text.literal("");
        for (MarketItem item : items) {
            itemList.append(Text.literal(item.getItemName() + " x" + item.getQuantity()).formatted(Formatting.YELLOW))
                    .append(Text.literal(" | ").formatted(Formatting.WHITE))
                    .append(Text.literal(item.getOwnerName()).formatted(Formatting.AQUA))
                    .append(Text.literal(" | ").formatted(Formatting.WHITE))
                    .append(Text.literal(String.format("%.2f", item.getPrice())).formatted(Formatting.GOLD))
                    .append(Text.literal(" "))
                    .append(createBuyButton(item.getItemId(), item.getPrice()))
                    .append(Text.literal(" "))
                    .append(createInfoButton(item.getItemStack()))
                    .append(Text.literal("\n"));
        }

        if (items.isEmpty()) {
            itemList.append(Text.translatable("market.no_item_matched").formatted(Formatting.RED));
        }

        return Text.literal("")
                .append(header).append("\n")
                .append(itemList);
    }

    // 主市场
    private static MutableText buildMarketMessage(MinecraftServer server, int page, MarketData marketData) {
        MutableText header = Text.translatable("market.title").formatted(Formatting.GOLD);

        // 获取当前页面上的8个物品
        List<MarketItem> itemsForPage = marketData.getItemsForPage(page, 8);

        MutableText itemList = Text.literal("");
        for (MarketItem item : itemsForPage) {
            itemList.append(Text.literal(item.getItemName() + " x" + item.getQuantity()).formatted(Formatting.YELLOW))
                    .append(Text.literal(" | ").formatted(Formatting.WHITE))
                    .append(Text.literal(item.getOwnerName()).formatted(Formatting.AQUA))
                    .append(Text.literal(" | ").formatted(Formatting.WHITE))
                    .append(Text.literal(String.format("%.2f", item.getPrice())).formatted(Formatting.GOLD))
                    .append(Text.literal(" "))
                    .append(createBuyButton(item.getItemId(), item.getPrice())) // 添加购买按钮
                    .append(Text.literal(" "))
                    .append(createInfoButton(item.getItemStack())) // 添加信息按钮
                    .append(Text.literal("\n"));
        }

        // 如果物品少于8个，填充 <empty> 占位符
        int emptySlots = 8 - itemsForPage.size();
        for (int i = 0; i < emptySlots; i++) {
            itemList.append(Text.translatable("market.empty").formatted(Formatting.DARK_GRAY))
                    .append(Text.literal("\n"));
        }

        // 计算总页数，确保至少有1页
        int totalPages = Math.max(1, marketData.getTotalPages(8));

        // 如果当前页大于总页数，将当前页修正为最后一页
        page = Math.min(page, totalPages);

        MutableText footer = Text.literal("")
                .append(createPageButton("[<-]", "/market " + (page > 1 ? page - 1 : 1)))  // 如果页码为1，左箭头按钮指向第一页
                .append(Text.literal("====| ").formatted(Formatting.GOLD))
                .append(Text.literal(page + "/" + totalPages).formatted(Formatting.YELLOW))
                .append(Text.literal(" |====").formatted(Formatting.GOLD))
                .append(createPageButton("[->]", "/market " + (page < totalPages ? page + 1 : totalPages)));  // 如果页码为最后一页，右箭头按钮指向最后一页

        return Text.literal("")
                .append(header).append("\n")
                .append(itemList)
                .append(footer);
    }

    private static MutableText createBuyButton(UUID itemId, double price) {
        return Text.translatable("market.buy")
                .formatted(Formatting.GREEN)
                .styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/buyitem " + itemId + " " + price)));
    }

    private static MutableText createInfoButton(ItemStack itemStack) {
        MutableText infoText = Text.translatable("market.info")
                .formatted(Formatting.GREEN)
                .styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, getItemInfo(itemStack))));
        return infoText;
    }

    private static MutableText getItemInfo(ItemStack itemStack) {
        MutableText infoText = Text.literal("");

        // 检查物品是否具有耐久度
        if (itemStack.isDamageable()) {
            infoText.append(Text.translatable("market.goods.durability").formatted(Formatting.GRAY))
                    .append("\n")
                    .append(Text.literal((itemStack.getMaxDamage() - itemStack.getDamage()) + "/" + itemStack.getMaxDamage()).formatted(Formatting.WHITE))
                    .append("\n\n");
        } else {
            infoText.append(Text.translatable("market.goods.no_durability").formatted(Formatting.RED))
                    .append("\n\n");
        }

        // 检查是否是附魔书
        if (itemStack.getItem() instanceof EnchantedBookItem) {
            NbtList enchantments = EnchantedBookItem.getEnchantmentNbt(itemStack);
            if (!enchantments.isEmpty()) {
                infoText.append(Text.translatable("market.goods.enchantment_book").formatted(Formatting.GRAY))
                        .append("\n");
                for (int i = 0; i < enchantments.size(); i++) {
                    NbtCompound enchantmentCompound = enchantments.getCompound(i);
                    Enchantment enchantment = Registries.ENCHANTMENT.get(Identifier.tryParse(enchantmentCompound.getString("id")));
                    int level = enchantmentCompound.getInt("lvl");

                    // 获取人类可读的附魔名称
                    String localizedName = enchantment.getName(level).getString();

                    infoText.append(Text.literal(localizedName).formatted(Formatting.BLUE))
                            .append("\n");
                }
                infoText.append("\n");
            } else {
                infoText.append(Text.translatable("market.goods.no_enchantment").formatted(Formatting.RED))
                        .append(Text.literal("\n\n"));
            }
        }
        // 检查物品是否具有附魔（非附魔书）
        else if (itemStack.hasEnchantments()) {
            infoText.append(Text.translatable("market.goods.enchantment").formatted(Formatting.GRAY)
                    .append("\n"));
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(itemStack);
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                Enchantment enchantment = entry.getKey();
                int level = entry.getValue();

                // 获取人类可读的附魔名称
                String localizedName = enchantment.getName(level).getString();

                infoText.append(Text.literal(localizedName).formatted(Formatting.BLUE))
                        .append("\n");
            }
            infoText.append("\n");
        } else {
            infoText.append(Text.translatable("market.goods.no_enchantment").formatted(Formatting.RED))
                    .append("\n\n");
        }

        // 检查物品是否被修改过名字
        if (itemStack.hasCustomName()) {
            infoText.append(Text.translatable("market.goods.named_item").formatted(Formatting.GRAY))
                    .append("\n")
                    .append(itemStack.getName().getString())
                    .append("\n")
                    .append(Text.translatable("market.goods.caution_discrimination").formatted(Formatting.RED));
        } else {
            infoText.append(Text.translatable("market.goods.no_custom_name").formatted(Formatting.RED))
                    .append("\n");
        }

        return infoText;
    }



    private static MutableText createPageButton(String label, String command) {
        return Text.literal(label)
                .formatted(Formatting.GREEN)
                .styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)));
    }

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

