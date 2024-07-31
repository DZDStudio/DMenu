package cn.dzdstudo.mc.dmenu;

import org.bukkit.Bukkit;

public class Logger {
    private static final java.util.logging.Logger logger = Bukkit.getServer().getPluginManager().getPlugin("DMenu").getLogger();

    /**
     * 输出普通消息
     * @param msg 消息
     */
    public static void info(String msg) {
        logger.info(msg);
    }

    /**
     * 输出警告消息
     * @param msg 消息
     */
    public static void warn(String msg) {
        logger.warning(msg);
    }

    /**
     * 输出错误消息
     * @param msg 消息
     */
    public static void error(String msg) {
        logger.severe(msg);
    }
}
