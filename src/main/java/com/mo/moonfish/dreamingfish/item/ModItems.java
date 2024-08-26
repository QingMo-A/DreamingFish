package com.mo.moonfish.dreamingfish.item;

import com.mo.moonfish.DreamingFish;
import com.mo.moonfish.dreamingfish.item.items.food.FishSoup;
import com.mo.moonfish.dreamingfish.item.items.food.ModFoodComponents;
import com.mo.moonfish.dreamingfish.item.items.potion.WormholePotion;
import com.mo.moonfish.dreamingfish.item.items.raw_material.BrokenOceanCrystal;
import com.mo.moonfish.dreamingfish.item.items.raw_material.OceanCrystal;
import com.mo.moonfish.dreamingfish.item.items.tool.CallPhone;
import com.mo.moonfish.dreamingfish.item.items.tool.CarvingKnife;
import com.mo.moonfish.dreamingfish.item.items.tool.Dream;
import com.mo.moonfish.dreamingfish.item.items.tool.MineralDetector;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    // raw_material
    public static final Item OCEAN_CRYSTAL = registerItem(OceanCrystal.ITEM_ID, new OceanCrystal(new FabricItemSettings()));
    public static final Item BROKEN_OCEAN_CRYSTAL = registerItem(BrokenOceanCrystal.ITEM_ID, new OceanCrystal(new FabricItemSettings()));

    // tool
    public static final Item MINERAL_DETECTOR = registerItem(MineralDetector.ITEM_ID, new MineralDetector(new FabricItemSettings().maxCount(1).maxDamage(64)));
    public static final Item CALL_PHONE = registerItem(CallPhone.ITEM_ID, new CallPhone(new FabricItemSettings().maxCount(1)));
    public static final Item CARVING_KNIFE = registerItem(CarvingKnife.ITEM_ID, new CarvingKnife(new FabricItemSettings().maxCount(1).maxDamage(5)));
    public static final Item DREAM = registerItem(Dream.ITEM_ID, new Dream(new FabricItemSettings().maxCount(1).maxDamage(5)));

    // armor

    // weapon

    // food
    public static final Item FISH_SOUP = registerItem(FishSoup.ITEM_ID, new FishSoup(new FabricItemSettings().maxCount(1).food(ModFoodComponents.FISH_SOUP)));

    // potion
    public static final Item WORMHOLE_POTION = registerItem(WormholePotion.ITEM_ID, new WormholePotion(new FabricItemSettings().maxCount(1)));

    private static Item registerItem(String itemID, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(DreamingFish.MOD_ID, itemID), item);
    }

    public static void registerModItems() {
        DreamingFish.LOGGER.info("Registering Mod Items");
    }
}
