package com.mo.moonfish.dreamingfish.economy.message;

import com.mo.moonfish.dreamingfish.economy.BankManager;
import com.mo.moonfish.dreamingfish.economy.EconomyManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class EconomyMessage {
    public static void sendEconomyMessage(ServerPlayerEntity serverPlayer) {

        Text transferText = Text.translatable("economy.callphone.transfer").styled(style -> style.withColor(Formatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/transfer ")));
        Text depositText = Text.translatable("economy.callphone.deposit").styled(style -> style.withColor(Formatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/deposit ")));
        Text withdrawText = Text.translatable("economy.callphone.withdrew").styled(style -> style.withColor(Formatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/withdraw ")));
        Text myItemText = Text.translatable("economy.callphone.myitem").styled(style -> style.withColor(Formatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/marketitem search player " + serverPlayer.getName().getString() + " 1")));
        Text marketText = Text.translatable("economy.callphone.market").styled(style -> style.withColor(Formatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/market 1")));
        Text listItemText = Text.translatable("economy.callphone.list_item").styled(style -> style.withColor(Formatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/listitem")));

        // 获取玩家余额
        double balance = EconomyManager.get(serverPlayer.getServer()).getBalance(serverPlayer);
        double bankBalance = BankManager.get(serverPlayer.getServer()).getBankBalance(serverPlayer);

        // 生成整个消息文本
        Text message = Text.translatable("economy.callphone.title").formatted(Formatting.GOLD)
                .append("\n\n")
                .append(Text.translatable("economy.callphone.player_balance").formatted(Formatting.YELLOW))
                .append(Text.literal(String.format("%.2f", balance)).formatted(Formatting.GOLD))
                .append(" ")
                .append(Text.translatable("economy.callphone.unit").formatted(Formatting.GOLD))
                .append(" ")
                // .append(Text.literal("钱包余额: ").formatted(Formatting.YELLOW))
                // .append(Text.literal(String.format("%.2f", balance) + " 枚梦鱼币 ").formatted(Formatting.GOLD))
                .append(transferText)
                .append("\n")
                .append(Text.translatable("economy.callphone.player_bank_balance").formatted(Formatting.YELLOW))
                .append(Text.literal(String.format("%.2f", bankBalance)).formatted(Formatting.GOLD))
                .append(" ")
                .append(Text.translatable("economy.callphone.unit").formatted(Formatting.GOLD))
                // .append(Text.literal("银行存款: ").formatted(Formatting.YELLOW))
                // .append(Text.literal(String.format("%.2f", bankBalance) + " 枚梦鱼币 ").formatted(Formatting.GOLD))
                .append(" ")
                .append(depositText)
                .append(Text.literal(" "))
                .append(withdrawText)
                .append("\n\n")
                .append(myItemText)
                .append(" ")
                .append(marketText)
                .append(" ")
                .append(listItemText)
                .append("\n\n")
                .append(Text.translatable("economy.callphone.title").formatted(Formatting.GOLD));

        // 发送消息
        serverPlayer.sendMessage(message, false);
    }
}
