package com.mo.moonfish;

import com.mo.moonfish.dreamingfish.block.ModBlocks;
import com.mo.moonfish.dreamingfish.command.commands.economy.EconomyCommand;
import com.mo.moonfish.dreamingfish.command.commands.economy.MarketCommand;
import com.mo.moonfish.dreamingfish.command.commands.economy.WelfareCommand;
import com.mo.moonfish.dreamingfish.command.commands.tpa.TPACommand;
import com.mo.moonfish.dreamingfish.command.commands.tpa.TPAHandler;
import com.mo.moonfish.dreamingfish.economy.BankManager;
import com.mo.moonfish.dreamingfish.entity.ModBlockEntities;
import com.mo.moonfish.dreamingfish.item.ModItemGroups;
import com.mo.moonfish.dreamingfish.item.ModItems;
import com.mo.moonfish.dreamingfish.item.items.tool.Dream;
import com.mo.moonfish.dreamingfish.loot.LootTableModifiers;
import com.mo.moonfish.dreamingfish.mixin.ModTags;
import com.mo.moonfish.dreamingfish.world.gen.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DreamingFish implements ModInitializer {
	// 模组ID
	public static final String MOD_ID = "dreamingfish";
	// 日志记录器
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private long lastDayChecked = -1;

	@Override
	public void onInitialize() {
		// 初始化
		TPAHandler.init();
		// 注册模组物品
		ModItems.registerModItems();
		// 注册模组物品组
		ModItemGroups.registerItemGroups();
		// 注册模组方块
		ModBlocks.registerModBlocks();

        // 注册TPA命令
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			TPACommand.register(dispatcher);
		});
		TPAHandler.init();

		// 注册经济命令
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			EconomyCommand.register(dispatcher);
		});

		// 注册经济命令
		MarketCommand.registerCommands();

		// 注册福利命令
		WelfareCommand.registerCommands();

        // 注册LootTable
        LootTableModifiers.registerLootTables();

		// 注册世界生成
		ModWorldGeneration.generateModWorldGen();

		// 注册方块实体
		ModBlockEntities.registerBlockEntities();

		LOGGER.info("DreamingFish has been initialized");

		// 每个服务器刻（tick）检查一次是否需要应用利息
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			long currentDay = server.getOverworld().getTimeOfDay() / 240000;  // Minecraft 一天是24000刻
			if (currentDay != lastDayChecked) {
				lastDayChecked = currentDay;
				applyDailyInterest(server);
			}
		});

		CustomPortalBuilder.beginPortal()
				.frameBlock(ModBlocks.CARVED_OCEAN_CRYSTAL_BLOCK)
				.lightWithItem(ModItems.DREAM)
				.destDimID(new Identifier(DreamingFish.MOD_ID, "dreaming"))
				.tintColor(0x76efa)
				.registerPortal();
	}

	private void applyDailyInterest(MinecraftServer server) {
		BankManager bankManager = BankManager.get(server);
		bankManager.applyInterest();
		server.getPlayerManager().broadcast(Text.translatable("receive_daily_interest"), false);
	}
}