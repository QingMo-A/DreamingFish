package com.mo.moonfish.dreamingfish.world.gen;

import com.mo.moonfish.DreamingFish;
import com.mo.moonfish.dreamingfish.block.ModBlocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.List;

public class ModConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> OCEAN_CRYSTAL_ORE_KEY = registryKey("ocean_crystal_ore");
    // public static final RegistryKey<ConfiguredFeature<?, ?>> DEEPSLATE_OCEAN_CRYSTAL_ORE_KEY = registryKey("deepslate_ocean_crystal_ore");

    public static void boostrap(Registerable<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplacables = new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplacables = new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreFeatureConfig.Target> overworldOceanCrystalOre =
                List.of(OreFeatureConfig.createTarget(stoneReplacables, ModBlocks.OCEAN_CRYSTAL_ORE.getDefaultState()),
                        OreFeatureConfig.createTarget(deepslateReplacables, ModBlocks.DEEPSLATE_OCEAN_CRYSTAL_ORE.getDefaultState()));

        register(context, OCEAN_CRYSTAL_ORE_KEY, Feature.ORE, new OreFeatureConfig(overworldOceanCrystalOre, 12));
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> registryKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(DreamingFish.MOD_ID, name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context, RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
