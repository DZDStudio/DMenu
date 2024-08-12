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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.geysermc.geyser.api.GeyserApi;

import java.util.*;

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

    private boolean isOpenFormItem(ItemStack item) {
        return item != null && item.getType() == Material.getMaterial(Objects.requireNonNull(Config.Config.getString("openItemType").toUpperCase())) && item.getItemMeta().getLore().contains("openMenu");
    }

    // 使用物品
    protected Map<UUID, Long> useFormPlayers = new HashMap<>();
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player pl = event.getPlayer();
        if (isOpenFormItem(event.getItem())) {
            // 1s内禁止打开
            if (useFormPlayers.containsKey(pl.getUniqueId()) && System.currentTimeMillis() - useFormPlayers.get(pl.getUniqueId()) < 1000) {
                return;
            }

            useFormPlayers.put(pl.getUniqueId(), System.currentTimeMillis());

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
        Player pl = event.getPlayer();

        ItemStack[] inventory = pl.getInventory().getContents();
        ItemStack clock = getOpenFormItem();

        boolean haveOpenFormItem = false;
        for (ItemStack item : inventory) {
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.hasLore()) {
                    List<String> lore = meta.getLore();
                    if (lore.contains("openMenu")) {
                        haveOpenFormItem = true;
                        break;
                    }
                }
            }
        }

        if (!haveOpenFormItem) {
            pl.getInventory().addItem(clock);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (isOpenFormItem(item)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        ItemStack[] drops = event.getDrops().toArray(new ItemStack[0]);
        String targetLore = "openMenu";

        for (int i = 0; i < drops.length; i++) {
            ItemStack item = drops[i];
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.hasLore()) {
                    List<String> lore = meta.getLore();
                    if (lore.contains(targetLore)) {
                        drops[i] = null;
                    }
                }
            }
        }
    }

    private ItemStack getOpenFormItem() {
        ItemStack clock = new ItemStack(Objects.requireNonNull(Material.getMaterial(Objects.requireNonNull(Config.Config.getString("openItemType").toUpperCase()))));
        ItemMeta meta = clock.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(Config.Config.getString("openItemName"));
            List<String> lore = Config.Config.getStringList("openItemLore");
            lore.add("openMenu");
            meta.setLore(lore);
            clock.setItemMeta(meta);
        }
        return clock;
    }
}
