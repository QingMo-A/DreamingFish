package com.mo.moonfish.dreamingfish.dimension;

import com.mo.moonfish.DreamingFish;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

import java.util.OptionalLong;

public class ModDimensions {
    public static final RegistryKey<DimensionOptions> DREAMING_KEY = RegistryKey.of(RegistryKeys.DIMENSION,
            new Identifier(DreamingFish.MOD_ID,"dreaming"));
    public static final RegistryKey<World> DREAMING_LEVEL_KEY = RegistryKey.of(RegistryKeys.WORLD,
            new Identifier(DreamingFish.MOD_ID, "dreaming"));
    public static final RegistryKey<DimensionType> DREAMING_TYPE = RegistryKey.of(RegistryKeys.DIMENSION_TYPE,
            new Identifier(DreamingFish.MOD_ID, "dreaming_type"));

    public static void bootstrapType(Registerable<DimensionType> context) {
        context.register(DREAMING_TYPE, new DimensionType(
                OptionalLong.of(12000),
                false,
                false,
                false,
                true,
                1.0,
                true,
                false,
                0,
                256,
                256,
                BlockTags.INFINIBURN_OVERWORLD,
                DimensionTypes.OVERWORLD_ID,
                1.0f,
                new DimensionType.MonsterSettings(false, false, UniformIntProvider.create(0, 0), 0)));
    }
}
