package cn.dzdstudo.mc.dmenu;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Config {
    private static final Plugin PLUGIN = Bukkit.getServer().getPluginManager().getPlugin("DMenu");
    public static Map<String, YamlConfiguration> CONFIGS = new HashMap<>();

    public static void reloadConfigs() {
        Logger.info("正在读取配置文件...");

        // 判断默认配置文件是否存在
        if(!new File(PLUGIN.getDataFolder(), "main.yml").exists()) {
            Logger.info("找不到配置文件，正在创建默认配置文件...");
            PLUGIN.saveResource("main.yml", false);
            PLUGIN.saveResource("cs.yml", false);
        }

        // 清空 map
        CONFIGS.clear();

        // 读取主文件
        Logger.info("加载页面配置文件: main.yml");
        File file = new File(PLUGIN.getDataFolder(), "main.yml");
        YamlConfiguration main = YamlConfiguration.loadConfiguration(file);
        CONFIGS.put("main", main);

        List<Map<?, ?>> buttons = main.getMapList("buttons");

        // 解析并读取其他页面
        for (Map<?, ?> item : buttons) {
            if(Objects.equals(item.get("type"), "form")) {
                Logger.info("加载页面配置文件: " + item.get("open") + ".yml");
                File itFile = new File(PLUGIN.getDataFolder(), item.get("open") + ".yml");
                CONFIGS.put((String) item.get("open"), YamlConfiguration.loadConfiguration(itFile));
            }
        }

        Logger.info("配置文件读取完毕。");
    }

    public static void reloadConfigs(CommandSender sender) {
        sender.sendMessage("[DMenu] 正在读取配置文件...");

        // 判断默认配置文件是否存在
        if(!new File(PLUGIN.getDataFolder(), "main.yml").exists()) {
            sender.sendMessage("[DMenu] 找不到配置文件，正在创建默认配置文件...");
            PLUGIN.saveResource("main.yml", false);
        }

        // 清空 map
        CONFIGS.clear();

        // 读取主文件
        sender.sendMessage("[DMenu] 加载页面配置文件: main.yml");
        File file = new File(PLUGIN.getDataFolder(), "main.yml");
        YamlConfiguration main = YamlConfiguration.loadConfiguration(file);
        CONFIGS.put("main", main);

        List<Map<?, ?>> buttons = main.getMapList("buttons");

        // 解析并读取其他页面
        for (Map<?, ?> item : buttons) {
            if(Objects.equals(item.get("type"), "form")) {
                sender.sendMessage("[DMenu] 加载页面配置文件: " + item.get("open") + ".yml");
                File itFile = new File(PLUGIN.getDataFolder(), item.get("open") + ".yml");
                CONFIGS.put((String) item.get("open"), YamlConfiguration.loadConfiguration(itFile));
            }
        }

        sender.sendMessage("[DMenu] 配置文件读取完毕。");
    }
}
