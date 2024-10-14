package com.mo.economy.gui.home_interface;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeInterfaceManager {
    private static final Map<UUID, HomeInterface> interfaceMap = new HashMap<>();

    public static void setPlayerInterface(UUID playerUUID, HomeInterface homeInterface) {
        System.out.println("Setting player interface for " + playerUUID);
        interfaceMap.put(playerUUID, homeInterface);
    }

    public static HomeInterface getPlayerInterface(UUID playerUUID) {
        System.out.println("Getting player interface for " + playerUUID);
        return interfaceMap.get(playerUUID);
    }

    public static void removePlayerInterface(UUID playerUUID) {
        System.out.println("Removing player interface for " + playerUUID);
        interfaceMap.remove(playerUUID);
    }
}
