package com.mo.moonfish.dreamingfish.entity;

import com.mo.moonfish.DreamingFish;
import com.mo.moonfish.dreamingfish.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static void registerBlockEntities() {
        DreamingFish.LOGGER.info("Registering block entities");
    }
}
