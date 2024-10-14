package com.mo.economy;

import com.mo.economy.gui.SingleSlotGuiDescription;
import com.mo.economy.item.ModItemGroups;
import com.mo.economy.item.ModItems;
import com.mo.economy.economy_system.EconomyCommand;
import com.mo.economy.network.client.*;
import com.mo.economy.network.server.RequestBalancePacket;
import com.mo.economy.network.server.RequestBankLevelPacket;
import com.mo.economy.network.server.RequestMarketListPacket;
import com.mo.economy.network.server.RequestSearchMarketListPacket;
import com.mo.economy.new_economy_system.ModCommands;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainForServer implements ModInitializer {

	public static final String MOD_ID = "dreamingfisheconomy";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Dreaming Fish Economy mod!");

		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();

		// 注册命令
		EconomyCommand.registerCommands();

		BankOperationPacket.register();
		RequestBalancePacket.register();  // 注册请求余额的数据包
		BalanceResponsePacket.register();  // 注册返回余额的数据包
		// 注册请求银行等级数据包
		RequestBankLevelPacket.register();
		// 注册银行等级响应数据包
		BankLevelResponsePacket.register();
		// 注册上架商品数据包
		ListItemPacket.register();
		// 注册请求市场列表数据包
		RequestMarketListPacket.register();
		// 注册市场列表响应数据包
		MarketListResponsePacket.register();
		// 注册移除商品数据包
		RemoveListedItemPacket.register();
		SearchMarketListResponsePacket.register();
		RequestSearchMarketListPacket.register();

		// 注册指令
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			ModCommands.register(dispatcher);
		});

		// 注册命令
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("openslot")
					.executes(context -> {
						// 获取执行命令的玩家
						ServerPlayerEntity player = context.getSource().getPlayer();

						if (player != null) {
							// 打开自定义 GUI
							player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
									(syncId, inv, playerEntity) -> new SingleSlotGuiDescription(syncId, inv, ScreenHandlerContext.EMPTY),
									Text.of("Single Slot")
							));
						}
						return 1;  // 成功执行命令，返回1
					})
			);
		});
	}
}
