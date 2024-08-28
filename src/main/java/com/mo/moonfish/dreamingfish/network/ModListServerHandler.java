package com.mo.moonfish.dreamingfish.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModListServerHandler {

    private static final Identifier GET_MOD_LIST_PACKET_ID = new Identifier("dreamingfish", "get_mod_list");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(GET_MOD_LIST_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            String modList = buf.readString(32767);
            server.execute(() -> {
                player.sendMessage(Text.literal("玩家 " + player.getEntityName() + " 的模组列表: " + modList), false);
            });
        });
    }
}
