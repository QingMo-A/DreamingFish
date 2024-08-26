package com.mo.moonfish.dreamingfish.item.items.tool;

import com.mo.moonfish.dreamingfish.block.ModBlocks;
import com.mo.moonfish.dreamingfish.block.blocks.OceanCrystalBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CarvingKnife extends Item {

    public static final String ITEM_ID = "carving_knife";

    public CarvingKnife(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockHitResult hitResult = new BlockHitResult(context.getHitPos(), context.getSide(), pos, false);
        Block block = world.getBlockState(pos).getBlock();
        ItemStack itemStack = context.getStack();
        Hand hand = context.getHand();

        // 检查被点击的方块是否为 OceanCrystalBlock
        if (block instanceof OceanCrystalBlock) {
            // 将 OceanCrystalBlock 替换为 CarvedOceanCrystalBlock
            world.setBlockState(pos, ModBlocks.CARVED_OCEAN_CRYSTAL_BLOCK.getDefaultState());

            world.playSound(context.getPlayer(), pos, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

            // 减少物品耐久（或者根据需要添加其他逻辑）
            itemStack.damage(1, context.getPlayer(), (p) -> p.sendToolBreakStatus(hand));

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}
