package com.mo.economy.item.items;

import com.mo.economy.gui.home_interface.HomeInterface;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Phone extends Item {

    public static final String ITEM_ID = "phone";

    public Phone(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user != null) {
            // 打开自定义 GUI
            user.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, inv, playerEntity) ->  new HomeInterface(syncId, inv, ScreenHandlerContext.EMPTY),
                    Text.literal(""))
            );
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
