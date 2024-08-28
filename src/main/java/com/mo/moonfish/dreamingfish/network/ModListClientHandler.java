package com.mo.moonfish.dreamingfish.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModListClientHandler implements ClientModInitializer {

    public static final Identifier GET_MOD_LIST_PACKET_ID = new Identifier("dreamingfish", "get_mod_list");

    @Override
    public void onInitializeClient() {
        // 注册数据包监听
        ClientPlayNetworking.registerGlobalReceiver(GET_MOD_LIST_PACKET_ID, (client, handler, buf, responseSender) -> {
            // 获取mods文件夹中的文件名列表
            File modsDir = new File(FabricLoader.getInstance().getGameDir().toFile(), "mods");
            List<String> modList = Arrays.stream(modsDir.listFiles())
                    .filter(file -> file.isFile() && file.getName().endsWith(".jar"))
                    .map(File::getName)
                    .collect(Collectors.toList());

            // 发送回服务器
            PacketByteBuf responseBuf = new PacketByteBuf(Unpooled.buffer());
            responseBuf.writeInt(modList.size());
            modList.forEach(responseBuf::writeString);
            ClientPlayNetworking.send(GET_MOD_LIST_PACKET_ID, responseBuf);
        });
    }
}
