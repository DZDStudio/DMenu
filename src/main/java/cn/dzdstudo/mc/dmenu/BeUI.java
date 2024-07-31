package cn.dzdstudo.mc.dmenu;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BeUI {
    public static void open(Player pl, String path) {
        FloodgateApi floodgateApi = FloodgateApi.getInstance();

        // 读取配置文件
        FileConfiguration conf = Config.CONFIGS.get(path);

        // 构件表单
        SimpleForm.Builder formBuilder = SimpleForm.builder();

        formBuilder.title((String) conf.get("name"));

        // 读取按钮列表
        List<Map<?, ?>> buttons = conf.getMapList("buttons");

        // 整理按钮
        buttons.removeIf(item -> {
            String type = (String) item.get("type");
            return (Objects.equals(type, "opcomm") || Objects.equals(type, "opform")) && !pl.isOp();
        });

        // 添加按钮
        for (Map<?, ?> item : buttons) {
            if(item.get("path") != null) {
                // 地址图片
                formBuilder.button((String) item.get("name"), FormImage.Type.PATH, (String) item.get("path"));
            } else if(item.get("url") != null) {
                // URL图片
                formBuilder.button((String) item.get("name"), FormImage.Type.URL, (String) item.get("url"));
            } else {
                // 文字表单
                formBuilder.button((String) item.get("name"));
            }
        }

        // 回调
        formBuilder.validResultHandler((form, res) -> {
            if (res != null) {
                int index = res.clickedButtonId();
                Map<?, ?> item = buttons.get(index);

                if(Objects.equals(item.get("type"), "comm")) {
                    Bukkit.getServer().dispatchCommand(pl, (String) item.get("open"));
                }

                if(Objects.equals(item.get("type"), "opcomm")) {
                    Bukkit.getServer().dispatchCommand(pl, (String) item.get("open"));
                }

                if(Objects.equals(item.get("type"), "form")) {
                    open(pl, (String) item.get("open"));
                }

                if(Objects.equals(item.get("type"), "opform")) {
                    open(pl, (String) item.get("open"));
                }
            }
        });

        // 发送
        floodgateApi.sendForm(pl.getUniqueId(), formBuilder.build());
    }
}
