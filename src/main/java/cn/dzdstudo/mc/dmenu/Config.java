package cn.dzdstudo.mc.dmenu;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private static final Plugin PLUGIN = Bukkit.getServer().getPluginManager().getPlugin("DMenu");
    public static Map<String, YamlConfiguration> formFiles = new HashMap<>();
    public static YamlConfiguration Config;

    public static void reloadConfigs() {
        Logger.info("正在读取配置文件...");

        // 判断默认配置文件是否存在
        if(!new File(PLUGIN.getDataFolder(), "Forms/main.yml").exists() || !new File(PLUGIN.getDataFolder(), "config.yml").exists()) {
            Logger.info("找不到配置文件，正在创建默认配置文件...");
            PLUGIN.saveResource("config.yml", false);
            PLUGIN.saveResource("Forms/main.yml", false);
            PLUGIN.saveResource("Forms/cs.yml", false);
        }

        // 清空 map
        formFiles.clear();

        // 读取配置文件
        Config = YamlConfiguration.loadConfiguration(new File(PLUGIN.getDataFolder(), "config.yml"));

        // 读取菜单文件
        File formFolder = new File(PLUGIN.getDataFolder(), "Forms");

        File[] files = formFolder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
                    Logger.info("加载页面配置文件: " + fileName + "[" + file.getName() + "]");
                    formFiles.put(fileName, YamlConfiguration.loadConfiguration(file));
                }
            }
        }

        Logger.info("配置文件读取完毕。");
    }

    public static void reloadConfigs(CommandSender sender) {
        sender.sendMessage("[DMenu] 正在读取配置文件...");

        // 判断默认配置文件是否存在
        if(!new File(PLUGIN.getDataFolder(), "Forms/main.yml").exists() || !new File(PLUGIN.getDataFolder(), "config.yml").exists()) {
            sender.sendMessage("[DMenu] 找不到配置文件，正在创建默认配置文件...");
            PLUGIN.saveResource("config.yml", false);
            PLUGIN.saveResource("Forms/main.yml", false);
            PLUGIN.saveResource("Forms/cs.yml", false);
        }

        // 清空 map
        formFiles.clear();

        // 读取配置文件
        Config = YamlConfiguration.loadConfiguration(new File(PLUGIN.getDataFolder(), "config.yml"));

        // 读取菜单文件
        File formFolder = new File(PLUGIN.getDataFolder(), "Forms");

        File[] files = formFolder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
                    sender.sendMessage("[DMenu] 加载页面配置文件: " + fileName + "[" + file.getName() + "]");
                    formFiles.put(fileName, YamlConfiguration.loadConfiguration(file));
                }
            }
        }

        sender.sendMessage("[DMenu] 配置文件读取完毕。");
    }
}
