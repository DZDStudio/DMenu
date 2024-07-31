package cn.dzdstudo.mc.dmenu;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class DMenu extends JavaPlugin {

    @Override
    public void onEnable() {
        Logger.info("DMenu 正在加载...");

        // 读取配置文件
        Config.reloadConfigs();

        // 注册 Java UI 事件
        getServer().getPluginManager().registerEvents(new JeUI(), this);

        // 注册事件
        getServer().getPluginManager().registerEvents(new EventKit(), this);

        // 注册命令
        PluginCommand cmd = getCommand("DMenu");
        cmd.setExecutor(new CommandKit());
        cmd.setTabCompleter(new CommandKit());

        Logger.info("DMenu 加载完成。");
    }
}
