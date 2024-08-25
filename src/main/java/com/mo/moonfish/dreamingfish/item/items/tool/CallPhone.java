package com.mo.moonfish.dreamingfish.item.items.tool;

import com.mo.moonfish.dreamingfish.economy.message.EconomyMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CallPhone extends Item {

    public static final String ITEM_ID = "call_phone";

    public CallPhone(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient && player instanceof ServerPlayerEntity serverPlayer) {
            // 发送经济信息消息给玩家
            EconomyMessage.sendEconomyMessage(serverPlayer);
        }
        return TypedActionResult.success(player.getStackInHand(hand));
    }
}
