package com.mo.economy.gui;

import com.mo.economy.MainForServer;
import com.mo.economy.gui.home_interface.HomeInterface;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ScreenHandlers {

    public static final Identifier SINGLE_SLOT_SCREEN_ID = new Identifier(MainForServer.MOD_ID, "single_slot");
    public static ScreenHandlerType<SingleSlotGuiDescription> SINGLE_SLOT_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(SINGLE_SLOT_SCREEN_ID, (syncId, inv) ->
            new SingleSlotGuiDescription(syncId, inv, ScreenHandlerContext.EMPTY));

    public static final Identifier HOME_INTERFACE_SCREEN_ID = new Identifier(MainForServer.MOD_ID, "home_interface");
    public static ScreenHandlerType<HomeInterface> HOME_INTERFACE_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(
            HOME_INTERFACE_SCREEN_ID,
            (syncId, inv) -> new HomeInterface(syncId, inv, ScreenHandlerContext.EMPTY));

}
