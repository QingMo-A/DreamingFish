package com.mo.moonfish.dreamingfish.item.items.tool;

import com.mo.moonfish.dreamingfish.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

public class MineralDetector extends Item {
    public static final String ITEM_ID = "mineral_detector";

    public MineralDetector(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!(context.getWorld().isClient)) {
            BlockPos posClicked = context.getBlockPos();
            PlayerEntity player = context.getPlayer();

            boolean flag = false;

            for (int i = 0; i < posClicked.getY() + 64; i++) {
                BlockState state = context.getWorld().getBlockState(posClicked.down(i));

                if(isValuableBlock(state)){
                    output(posClicked.down(i), player, state.getBlock());
                    flag = true;
                }
            }

            if(!flag){
                player.sendMessage(Text.translatable("mineral_detector_output_not_found"), false);
            }

        }

        context.getStack().damage(1, context.getPlayer(), playerEntity -> playerEntity.sendToolBreakStatus(playerEntity.getActiveHand()));

        return ActionResult.SUCCESS;
    }

    private void output(BlockPos down, PlayerEntity player, Block block) {
        player.sendMessage(Text.translatable("mineral_detector_output_found", block.asItem().getName().getString()), false);
    }

    private boolean isValuableBlock(BlockState state) {
        return state.isOf(Blocks.COAL_ORE) ||
                state.isOf(Blocks.IRON_ORE) ||
                state.isOf(Blocks.GOLD_ORE) ||
                state.isOf(Blocks.DIAMOND_ORE) ||
                state.isOf(Blocks.EMERALD_ORE) ||
                state.isOf(Blocks.LAPIS_ORE) ||
                state.isOf(Blocks.REDSTONE_ORE) ||
                state.isOf(Blocks.NETHER_QUARTZ_ORE) ||
                state.isOf(Blocks.NETHER_GOLD_ORE) ||
                state.isOf(ModBlocks.OCEAN_CRYSTAL_ORE) ||
                state.isOf(ModBlocks.DEEPSLATE_OCEAN_CRYSTAL_ORE);
    }
}
