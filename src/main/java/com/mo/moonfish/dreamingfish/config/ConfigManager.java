package com.mo.moonfish.dreamingfish.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Config config;

    public static class Config {
        public boolean requireWormholePotion = true; // 是否需要虫洞药水
        public boolean enableMobBlacklist = true; // 是否启用黑名单
        public List<String> mobBlacklist = new ArrayList<>(); // 黑名单中的生物

        public Config() {
            // 默认黑名单生物
            mobBlacklist.add("iceandfire:stonestatue");
            mobBlacklist.add("dummmmmmy:target_dummy");
        }
    }

    public static void loadConfig() {
        File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "dreamingfish_config.json");
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                config = GSON.fromJson(reader, Config.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            config = new Config();
            saveConfig();
        }
    }

    public static void saveConfig() {
        File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "dreamingfish_config.json");
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Config getConfig() {
        if (config == null) {
            loadConfig();
        }
        return config;
    }
}
