package com.mo.moonfish.dreamingfish.block.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.util.math.intprovider.IntProvider;

public class OceanCrystalOre extends ExperienceDroppingBlock {
    public static final String BLOCK_ID = "ocean_crystal_ore";

    public OceanCrystalOre(Settings settings) {
        super(settings);
    }

    public OceanCrystalOre(Settings settings, IntProvider experience) {
        super(settings, experience);
    }
}
