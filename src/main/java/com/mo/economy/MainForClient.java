package com.mo.economy;

import com.mo.economy.gui.ScreenHandlers;
import com.mo.economy.gui.SingleSlotScreen;
import com.mo.economy.gui.home_interface.HomeInterfaceScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class MainForClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // 正确注册屏幕
        ScreenRegistry.register(ScreenHandlers.SINGLE_SLOT_SCREEN_HANDLER, SingleSlotScreen::new);

        // 正确注册 Home Interface 屏幕
        ScreenRegistry.register(ScreenHandlers.HOME_INTERFACE_SCREEN_HANDLER, HomeInterfaceScreen::new);
    }
}
