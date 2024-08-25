package com.mo.moonfish.dreamingfish.loot;

import com.mo.moonfish.dreamingfish.item.ModItems;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.Set;

public class LootTableModifiers {

    private static final Set<String> CHEST_LOOT_TABLES = Set.of(
            "minecraft:chests/"
            );

    public static void registerLootTables() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, table, source) -> {
            // 检查Loot Table ID是否以我们关心的前缀开头
            for (String prefix : CHEST_LOOT_TABLES) {
                if (id.toString().startsWith(prefix)) {
                    // 创建一个新的LootPool
                    LootPool pool = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1)) // 每次生成时掉落一次
                            .with(ItemEntry.builder(ModItems.WORMHOLE_POTION))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build()) // 随机数量
                            .conditionally(RandomChanceLootCondition.builder(0.20f)) // 20%的概率生成
                            .build();

                    // 将LootPool添加到LootTable中
                    table.pool(pool);
                    break; // 一旦匹配就可以跳出循环
                }
            }
        });
    }
}
