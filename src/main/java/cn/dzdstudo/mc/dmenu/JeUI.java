package cn.dzdstudo.mc.dmenu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.List;

public class JeUI implements Listener {
    private static Map<String, Map<String, Object>> openPlayersUI = new HashMap<>();

    public static void open(Player pl, String path, int page) {
        // 读取配置文件
        FileConfiguration conf = Config.CONFIGS.get(path);

        // 构造箱子 UI
        Inventory inventory = Bukkit.createInventory(null, 3*9, Objects.requireNonNull(conf.getString("name")));

        // 读取按钮列表
        List<Map<?, ?>> buttons = conf.getMapList("buttons");

        // 整理按钮
        buttons.removeIf(item -> {
            String type = (String) item.get("type");
            String playerType = (String) item.get("playerType");
            if ((Objects.equals(type, "opcomm") || Objects.equals(type, "opform")) && !pl.isOp()) {
                return true;
            }
            if (Objects.equals(playerType, "be")) {
                return true;
            }
            return false;
        });

        // 添加边框
        ItemStack frameItemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta frameMeta = frameItemStack.getItemMeta();
        frameMeta.setDisplayName("这是一个边框");
        frameMeta.setLore(Arrays.asList("这是一个边框"));
        frameItemStack.setItemMeta(frameMeta);
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, frameItemStack);
        }
        for (int i = 18; i < 27; i++) {
            // 排除页数显示
            if (i == 22) {
                continue;
            }
            inventory.setItem(i, frameItemStack);
        }

        /* 处理按钮 */
        // 单行页是否可以显示
        boolean isSinglePage = buttons.size() <= 9;

        // 总行数
        int totalRow = (int) Math.ceil((double) buttons.size() / 7);

        // 添加按钮
        if (isSinglePage) {
            // 当前初始位置
            int currentPosition = 9;
            for (Map<?, ?> button : buttons) {
                // 读取要添加的按钮（物品）
                Map<?, ?> item = (Map<?, ?>) button.get("item");

                // 添加
                Material itemType = Material.getMaterial(((String) item.get("type")).toUpperCase());
                if (itemType == null) {
                    itemType = Material.GRASS_BLOCK;
                }
                ItemStack itemStack = new ItemStack(itemType);
                ItemMeta meta = itemStack.getItemMeta();
                meta.setDisplayName((String) button.get("name"));
                meta.setLore((List<String>) item.get("lore"));
                itemStack.setItemMeta(meta);
                inventory.setItem(currentPosition, itemStack);

                currentPosition ++;
            }

            // 补全空位
            inventory.setItem(22, frameItemStack);
        } else {
            // 多行页
            // 截取可显示部分
            int min = (page-1) * 7;
            if (min < 0) {
                min = 0;
            }
            int max = page * 7;
            if (max > buttons.size()) {
                max = buttons.size();
            }
            buttons = buttons.subList(min, max);

            int currentPosition = 10;
            for (Map<?, ?> button : buttons) {
                // 读取要添加的按钮（物品）
                Map<?, ?> item = (Map<?, ?>) button.get("item");

                // 添加
                Material itemType = Material.getMaterial(((String) item.get("type")).toUpperCase());
                if (itemType == null) {
                    itemType = Material.GRASS_BLOCK;
                }
                ItemStack itemStack = new ItemStack(itemType);
                ItemMeta meta = itemStack.getItemMeta();
                meta.setDisplayName((String) button.get("name"));
                meta.setLore((List<String>) item.get("lore"));
                itemStack.setItemMeta(meta);
                inventory.setItem(currentPosition, itemStack);

                currentPosition ++;
            }

            // 添加当前行数显示
            ItemStack pageItemStack = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
            ItemMeta pageMeta = frameItemStack.getItemMeta();
            pageMeta.setDisplayName(page + "/" + totalRow);
            pageMeta.setLore(Arrays.asList("当前页数：" + page, "总页数：" + totalRow));
            pageItemStack.setItemMeta(pageMeta);
            inventory.setItem(22, pageItemStack);

            // 添加换页按钮
            ItemStack upPageItemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta upPageMeta = frameItemStack.getItemMeta();
            upPageMeta.setDisplayName("上一页");
            upPageMeta.setLore(Arrays.asList("点我切换到上一页"));
            upPageItemStack.setItemMeta(upPageMeta);

            ItemStack downPageItemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta downPageMeta = frameItemStack.getItemMeta();
            downPageMeta.setDisplayName("下一页");
            downPageMeta.setLore(Arrays.asList("点我切换到下一页"));
            downPageItemStack.setItemMeta(downPageMeta);

            inventory.setItem(9, upPageItemStack);
            inventory.setItem(17, downPageItemStack);
        }

        // 更新 openPlayers
        Map<String, Object> playerUi = new HashMap<>();
        playerUi.put("path", path);
        playerUi.put("page", page);
        playerUi.put("totalRow", totalRow);
        playerUi.put("name", conf.getString("name"));
        playerUi.put("buttons", buttons);
        playerUi.put("isSinglePage", isSinglePage);
        openPlayersUI.put(String.valueOf(pl.getUniqueId()), playerUi);

        pl.openInventory(inventory);
    }

    // 菜单点击事件
    @EventHandler
    public static void onInventoryClick(InventoryClickEvent event) {
        Player pl = (Player) event.getView().getPlayer();

        // 非菜单 UI
        if (!openPlayersUI.containsKey(String.valueOf(pl.getUniqueId()))) {
            return;
        } else if (!event.getView().getTitle().equals(openPlayersUI.get(String.valueOf(pl.getUniqueId())).get("name"))) {
            return;
        }

        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();
        int rawSlot = event.getRawSlot() + 1;

        // 非按钮
        if (clickedItem == null) {
            return;
        } else if (clickedItem.getType() == Material.AIR && clickedItem.getType() == Material.GRAY_STAINED_GLASS_PANE) {
            return;
        }

        // 获取参数
        String path = (String) openPlayersUI.get(String.valueOf(pl.getUniqueId())).get("path");
        int page = (int) openPlayersUI.get(String.valueOf(pl.getUniqueId())).get("page");
        int totalRow = (int) openPlayersUI.get(String.valueOf(pl.getUniqueId())).get("totalRow");
        List<Map<?, ?>> buttons = (List<Map<?, ?>>) openPlayersUI.get(String.valueOf(pl.getUniqueId())).get("buttons");
        boolean isSinglePage = (boolean) openPlayersUI.get(String.valueOf(pl.getUniqueId())).get("isSinglePage");

        // 单行显示
        if (isSinglePage) {
            // 边框
            if (rawSlot < 10 || rawSlot > 18) {
                return;
            }
            Map<?, ?> button = buttons.get(rawSlot - 10);

            if (button.get("type").equals("comm")) {
                pl.performCommand((String) button.get("open"));
            } else if (button.get("type").equals("opcomm")) {
                pl.performCommand((String) button.get("open"));
            } else if (button.get("type").equals("form")) {
                open(pl, (String) button.get("open"), 1);
            } else if (button.get("type").equals("opform")) {
                open(pl, (String) button.get("open"), 1);
            }
        } else {
            if (rawSlot == 10) {
                // 上一页
                if (page > 1) {
                    open(pl, path, page - 1);
                }
            } else if (rawSlot == 18) {
                // 下一页
                if (page < totalRow) {
                    open(pl, path, page + 1);
                }
            } else {
                // 边框
                if (rawSlot < 10 || rawSlot > 18) {
                    return;
                }
                Map<?, ?> button = buttons.get(rawSlot - 11);

                if (button.get("type").equals("comm")) {
                    pl.performCommand((String) button.get("open"));
                } else if (button.get("type").equals("opcomm")) {
                    pl.performCommand((String) button.get("open"));
                } else if (button.get("type").equals("form")) {
                    open(pl, (String) button.get("open"), 1);
                } else if (button.get("type").equals("opform")) {
                    open(pl, (String) button.get("open"), 1);
                }
            }
        }
    }
}
