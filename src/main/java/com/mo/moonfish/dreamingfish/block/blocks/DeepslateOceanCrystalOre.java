package com.mo.moonfish.dreamingfish.block.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.util.math.intprovider.IntProvider;

public class DeepslateOceanCrystalOre extends ExperienceDroppingBlock {
    public static final String BLOCK_ID = "deepslate_ocean_crystal_ore";


    public DeepslateOceanCrystalOre(Settings settings) {
        super(settings);
    }

    public DeepslateOceanCrystalOre(Settings settings, IntProvider experience) {
        super(settings, experience);
    }
}
