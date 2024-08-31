package com.mo.moonfish.dreamingfish.economy.message;

import com.mo.moonfish.dreamingfish.economy.MarketData;
import com.mo.moonfish.dreamingfish.economy.MarketItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MarketMessage {
    // 根据物品列表构建市场消息
    public static MutableText buildMarketMessage(MinecraftServer server, int page, List<MarketItem> items, String name, boolean byPlayer) {
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
    public static MutableText buildMarketMessage(MinecraftServer server, List<MarketItem> items) {
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
    public static MutableText buildMarketMessage(MinecraftServer server, int page, MarketData marketData) {
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

    // 创建购买按钮
    private static MutableText createBuyButton(UUID itemId, double price) {
        return Text.translatable("market.buy")
                .formatted(Formatting.GREEN)
                .styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/buyitem " + itemId + " " + price)));
    }

    // 创建信息按钮
    private static MutableText createInfoButton(ItemStack itemStack) {
        MutableText infoText = Text.translatable("market.info")
                .formatted(Formatting.GREEN)
                .styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, getItemInfo(itemStack))));
        return infoText;
    }

    // 获取物品信息
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

    // 创建页面按钮
    private static MutableText createPageButton(String label, String command) {
        return Text.literal(label)
                .formatted(Formatting.GREEN)
                .styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)));
    }
}
