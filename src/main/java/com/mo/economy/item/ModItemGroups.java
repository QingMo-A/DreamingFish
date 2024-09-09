package com.mo.economy.item;

import com.mo.economy.MainForServer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.Main;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup DREAMINGFISH_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            new Identifier(MainForServer.MOD_ID, "dreamingfish"),
            FabricItemGroup.builder().displayName(
                            Text.translatable("itemgroup.dreamingfish"))
                    .icon(() -> new ItemStack(ModItems.PHONE))
                    .entries(((displayContext, entries) -> {
                        entries.add(ModItems.PHONE);
                        entries.add(ModItems.COPPER_COIN);
                        entries.add(ModItems.SILVER_COIN);
                        entries.add(ModItems.GOLD_COIN);
                        entries.add(ModItems.BANK_ICON);
                        entries.add(ModItems.SYSTEM_STORE_ICON);
                        entries.add(ModItems.PLAYER_MARKET_ICON);
                    })).build());

    public static void registerItemGroups() {
        MainForServer.LOGGER.info("Registering item groups");
    }
}
