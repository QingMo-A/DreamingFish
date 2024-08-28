package com.mo.moonfish.dreamingfish.world.biome.surface;

import com.mo.moonfish.dreamingfish.block.ModBlocks;
import com.mo.moonfish.dreamingfish.world.biome.ModBiomes;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

public class ModMaterialRules {
    private static final MaterialRules.MaterialRule DIRT = makeStateRule(Blocks.DIRT);
    private static final MaterialRules.MaterialRule GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
    private static final MaterialRules.MaterialRule OCEAN_CRYSTAL_ORE = makeStateRule(ModBlocks.OCEAN_CRYSTAL_ORE);
    private static final MaterialRules.MaterialRule OCEAN_CRYSTAL_BLOCK = makeStateRule(ModBlocks.OCEAN_CRYSTAL_BLOCK);

    public static MaterialRules.MaterialRule makeRules() {
        MaterialRules.MaterialCondition isAtOrAboveWaterLevel = MaterialRules.water(-1, 0);

        MaterialRules.MaterialRule grassSurface = MaterialRules.sequence(MaterialRules.condition(isAtOrAboveWaterLevel, GRASS_BLOCK), DIRT);

        return MaterialRules.sequence(
                MaterialRules.sequence(MaterialRules.condition(MaterialRules.biome(ModBiomes.TEST_BIOME),
                        MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR, GRASS_BLOCK)),
                        MaterialRules.condition(MaterialRules.STONE_DEPTH_CEILING, DIRT)),

                MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR, grassSurface)
        );
    }

    public static MaterialRules.MaterialRule makeRules2() {
        // 在 TEST_BIOME 生物群系中，将地表设置为草方块，下层为泥土
        return MaterialRules.sequence(
                MaterialRules.condition(
                        MaterialRules.biome(ModBiomes.NEW_BIOME),
                        MaterialRules.sequence(
                                // 设置地表为草方块
                                MaterialRules.condition(MaterialRules.surface(), GRASS_BLOCK),
                                // 设置草方块以下为泥土
                                DIRT
                        )
                )
        );
    }



    private static MaterialRules.MaterialRule makeStateRule(Block block) {
        return MaterialRules.block(block.getDefaultState());
    }
}
