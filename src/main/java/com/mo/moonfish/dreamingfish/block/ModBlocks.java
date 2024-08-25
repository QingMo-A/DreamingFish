package com.mo.moonfish.dreamingfish.block;

import com.mo.moonfish.DreamingFish;
import com.mo.moonfish.dreamingfish.block.blocks.DeepslateOceanCrystalOre;
import com.mo.moonfish.dreamingfish.block.blocks.OceanCrystalBlock;
import com.mo.moonfish.dreamingfish.block.blocks.OceanCrystalOre;
import com.mo.moonfish.dreamingfish.block.blocks.TestBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class ModBlocks {
    public static final Block OCEAN_CRYSTAL_BLOCK = registerBlock(
            OceanCrystalBlock.BLOCK_ID,
            new OceanCrystalBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)));

    public static final Block OCEAN_CRYSTAL_ORE = registerBlock(
            OceanCrystalOre.BLOCK_ID,
            new OceanCrystalOre(FabricBlockSettings.copyOf(Blocks.IRON_ORE), UniformIntProvider.create(2, 5)));

    public static final Block DEEPSLATE_OCEAN_CRYSTAL_ORE = registerBlock(
            DeepslateOceanCrystalOre.BLOCK_ID,
            new DeepslateOceanCrystalOre(FabricBlockSettings.copyOf(Blocks.IRON_ORE), UniformIntProvider.create(2, 5)));

    public static final Block TEST_BLOCK = registerBlock(
            TestBlock.BLOCK_ID,
            new TestBlock(FabricBlockSettings.copyOf(Blocks.GRASS_BLOCK)));

    private static Block registerBlock(String blockID, Block block) {
        registerBlockItem(blockID, block);
        return Registry.register(Registries.BLOCK, new Identifier(DreamingFish.MOD_ID, blockID), block);
    }

    private static Item registerBlockItem(String blockID, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(DreamingFish.MOD_ID, blockID), new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        DreamingFish.LOGGER.info("Registering Mod Blocks");
    }
}
