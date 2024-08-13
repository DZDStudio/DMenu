package cn.dzdstudo.mc.dmenu;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.geyser.api.GeyserApi;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandKit implements CommandExecutor , TabCompleter {
    /**
     * 判断指定插件是否已加载
     *
     * @param pluginName 插件名称
     * @return 如果插件已加载，返回 true；否则返回 false
     */
    private boolean isPluginLoaded(String pluginName) {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(pluginName);
        return plugin != null && plugin.isEnabled();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!"dmenu".equalsIgnoreCase(command.getName())) {
            return false;
        }

        if (args.length == 1 && "reload".equalsIgnoreCase(args[0])) {
            if (sender instanceof Player pl) {
                if (!pl.isOp()) {
                    sender.sendMessage("§c你没有权限执行此命令。");
                    return true;
                } else {
                    Config.reloadConfigs(sender);
                }
            } else {
                Config.reloadConfigs(sender);
            }

            return true;
        }

        if (sender instanceof Player pl) {
            boolean isGeyser = isPluginLoaded("floodgate");

            if (isGeyser) {
                FloodgateApi floodgateApi = FloodgateApi.getInstance();
                boolean isBePlayer = floodgateApi.isFloodgatePlayer(pl.getUniqueId());
                if (isBePlayer) {
                    BeUI.open(pl, "main");
                } else {
                    JeUI.open(pl, "main", 1);
                }
            } else {
                JeUI.open(pl, "main", 1);
            }
        } else {
            sender.sendMessage("§c请使用客户端打开菜单。");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args) {
        if ("dmenu".equalsIgnoreCase(command.getName())) {
            List<String> completions = new ArrayList<>();

            if (sender instanceof Player pl) {
                if (!pl.isOp()) {
                    if (args.length == 1) {
                        completions.add("open");
                    }

                    return completions;
                }
            }

            if (args.length == 1) {
                completions.add("reload");
                completions.add("open");
            }
            return completions;
        }
        return null;
    }
}
