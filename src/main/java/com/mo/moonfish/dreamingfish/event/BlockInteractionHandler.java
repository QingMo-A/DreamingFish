package com.mo.moonfish.dreamingfish.event;

import com.mo.moonfish.dreamingfish.block.ModBlocks;
import com.mo.moonfish.dreamingfish.block.blocks.CarvedOceanCrystalBlock;
import com.mo.moonfish.dreamingfish.block.blocks.OceanCrystalBlock;
import com.mo.moonfish.dreamingfish.item.items.tool.CarvingKnife;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockInteractionHandler {

    private static final Block CARVED_OCEAN_CRYSTAL_BLOCK = ModBlocks.CARVED_OCEAN_CRYSTAL_BLOCK; // 这里初始化你的方块实例

    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> handleBlockUse((ServerPlayerEntity) player, world, hand, hitResult));
    }

    private static ActionResult handleBlockUse(ServerPlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        // 检查玩家是否使用主手
        if (hand != Hand.MAIN_HAND) return ActionResult.PASS;

        // 获取玩家手中的物品
        ItemStack itemStack = player.getStackInHand(hand);

        // 获取被点击的方块的位置和类型
        BlockPos pos = hitResult.getBlockPos();
        Block block = world.getBlockState(pos).getBlock();

        // 检查玩家手持的物品是否为 CarvingKnife 并且点击的方块是否为 OceanCrystalBlock
        if (itemStack.getItem() instanceof CarvingKnife && block instanceof OceanCrystalBlock) {
            // 将 OceanCrystalBlock 替换为 CarvedOceanCrystalBlock
            world.setBlockState(pos, CARVED_OCEAN_CRYSTAL_BLOCK.getDefaultState());

            // 减少一耐久或者任意逻辑
            itemStack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}

