package com.mo.economy.item;

import com.mo.economy.MainForServer;
import com.mo.economy.item.items.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item PHONE = registerItem(Phone.ITEM_ID, new Phone(new FabricItemSettings().maxCount(1)));

    public static final Item COPPER_COIN = registerItem(CopperCoin.ITEM_ID, new CopperCoin(new FabricItemSettings().maxCount(64)));

    public static final Item SILVER_COIN = registerItem(SilverCoin.ITEM_ID, new SilverCoin(new FabricItemSettings().maxCount(64)));

    public static final Item GOLD_COIN = registerItem(GoldCoin.ITEM_ID, new GoldCoin(new FabricItemSettings().maxCount(64)));

    public static final Item BANK_ICON = registerItem(BankIcon.ITEM_ID, new BankIcon(new FabricItemSettings().maxCount(64)));
    public static final Item SYSTEM_STORE_ICON = registerItem(SystemStoreIcon.ITEM_ID, new SystemStoreIcon(new FabricItemSettings().maxCount(64)));
    public static final Item PLAYER_MARKET_ICON = registerItem(PlayerMarketIcon.ITEM_ID, new PlayerMarketIcon(new FabricItemSettings().maxCount(64)));

    private static Item registerItem(String itemID, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(MainForServer.MOD_ID, itemID), item);
    }

    public static void registerModItems() {
        MainForServer.LOGGER.info("Registering Mod Items");
    }
}
