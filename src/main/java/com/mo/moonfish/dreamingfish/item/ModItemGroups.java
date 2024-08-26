package com.mo.moonfish.dreamingfish.item;

import com.mo.moonfish.DreamingFish;
import com.mo.moonfish.dreamingfish.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup DREAMINGFISH_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            new Identifier(DreamingFish.MOD_ID, "dreamingfish"),
            FabricItemGroup.builder().displayName(
                    Text.translatable("itemgroup.dreamingfish"))
                    .icon(() -> new ItemStack(ModItems.OCEAN_CRYSTAL))
                    .entries(((displayContext, entries) -> {
                        entries.add(ModBlocks.OCEAN_CRYSTAL_ORE);
                        entries.add(ModBlocks.DEEPSLATE_OCEAN_CRYSTAL_ORE);
                        entries.add(ModBlocks.OCEAN_CRYSTAL_BLOCK);
                        entries.add(ModBlocks.CARVED_OCEAN_CRYSTAL_BLOCK);
                        entries.add(ModItems.DREAM);
                        entries.add(ModItems.OCEAN_CRYSTAL);
                        entries.add(ModItems.BROKEN_OCEAN_CRYSTAL);
                        entries.add(ModItems.MINERAL_DETECTOR);
                        entries.add(ModItems.FISH_SOUP);
                        entries.add(ModItems.WORMHOLE_POTION);
                        entries.add(ModItems.CALL_PHONE);
                        entries.add(ModItems.CARVING_KNIFE);
                        entries.add(ModBlocks.TEST_BLOCK);
                    })).build());

    public static void registerItemGroups() {
        DreamingFish.LOGGER.info("Registering item groups");
    }
}
