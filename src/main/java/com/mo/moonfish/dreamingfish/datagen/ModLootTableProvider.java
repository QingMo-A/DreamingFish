package com.mo.moonfish.dreamingfish.datagen;

import com.mo.moonfish.dreamingfish.block.ModBlocks;
import com.mo.moonfish.dreamingfish.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    protected ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.OCEAN_CRYSTAL_BLOCK);

        addDrop(ModBlocks.OCEAN_CRYSTAL_ORE, oreDrops(ModBlocks.OCEAN_CRYSTAL_ORE, ModItems.BROKEN_OCEAN_CRYSTAL));
        addDrop(ModBlocks.DEEPSLATE_OCEAN_CRYSTAL_ORE, oreDrops(ModBlocks.DEEPSLATE_OCEAN_CRYSTAL_ORE, ModItems.BROKEN_OCEAN_CRYSTAL));
    }
}
