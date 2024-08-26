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
                        MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR, OCEAN_CRYSTAL_ORE)),
                        MaterialRules.condition(MaterialRules.STONE_DEPTH_CEILING, OCEAN_CRYSTAL_BLOCK)),

                MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR, grassSurface)
        );
    }

    private static MaterialRules.MaterialRule makeStateRule(Block block) {
        return MaterialRules.block(block.getDefaultState());
    }
}
