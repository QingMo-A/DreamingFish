package com.mo.moonfish.dreamingfish.datagen;

import com.mo.moonfish.dreamingfish.block.ModBlocks;
import com.mo.moonfish.dreamingfish.mixin.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(ModTags.Blocks.VALUABLE_BLOCKS)
                .add(ModBlocks.OCEAN_CRYSTAL_ORE)
                .add(ModBlocks.DEEPSLATE_OCEAN_CRYSTAL_ORE)
                .forceAddTag(BlockTags.GOLD_ORES)
                .forceAddTag(BlockTags.IRON_ORES)
                .forceAddTag(BlockTags.EMERALD_ORES)
                .forceAddTag(BlockTags.REDSTONE_ORES)
                .forceAddTag(BlockTags.DIAMOND_ORES)
                .forceAddTag(BlockTags.COAL_ORES)
                .forceAddTag(BlockTags.LAPIS_ORES);

        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(ModBlocks.OCEAN_CRYSTAL_ORE)
                .add(ModBlocks.DEEPSLATE_OCEAN_CRYSTAL_ORE)
                .add(ModBlocks.OCEAN_CRYSTAL_BLOCK);

        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.OCEAN_CRYSTAL_ORE)
                .add(ModBlocks.DEEPSLATE_OCEAN_CRYSTAL_ORE)
                .add(ModBlocks.OCEAN_CRYSTAL_BLOCK);
    }
}
