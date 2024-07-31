package cn.dzdstudo.mc.dmenu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.geysermc.geyser.api.GeyserApi;

import java.util.Arrays;

public class EventKit implements Listener {
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

    // 使用物品
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player pl = event.getPlayer();
        if (event.getItem() != null && event.getItem().getType() == Material.CLOCK) {
            // 打开菜单
            boolean isGeyser = isPluginLoaded("Geyser-Spigot") && isPluginLoaded("floodgate");

            if (isGeyser) {
                boolean isBePlayer = GeyserApi.api().isBedrockPlayer(pl.getUniqueId());
                if (isBePlayer) {
                    BeUI.open(pl, "main");
                } else {
                    JeUI.open(pl, "main", 1);
                }
            } else {
                JeUI.open(pl, "main", 1);
            }

            // 拦截
            event.setCancelled(true);
        }
    }

    /* 给钟 */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ItemStack clock = getCustomClock();

        // Check if the player already has the clock
        if (!player.getInventory().contains(clock)) {
            player.getInventory().addItem(clock);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (isCustomClock(item)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().removeIf(this::isCustomClock);
    }

    private ItemStack getCustomClock() {
        ItemStack clock = new ItemStack(Material.CLOCK);
        ItemMeta meta = clock.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6菜单");
            meta.setLore(Arrays.asList("§7使用即可打开菜单"));
            clock.setItemMeta(meta);
        }
        return clock;
    }

    private boolean isCustomClock(ItemStack item) {
        if (item == null || item.getType() != Material.CLOCK) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta != null && "§6菜单".equals(meta.getDisplayName());
    }
}
