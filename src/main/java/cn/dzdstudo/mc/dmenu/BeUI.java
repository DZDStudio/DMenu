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
        FileConfiguration conf = Config.formFiles.get(path);

        // 构件表单
        SimpleForm.Builder formBuilder = SimpleForm.builder();

        formBuilder.title((String) conf.get("name"));

        String content = (String) conf.get("content");
        if (content != null) {
            formBuilder.content(content);
        }

        // 读取按钮列表
        List<Map<?, ?>> buttons = conf.getMapList("buttons");

        // 整理按钮
        buttons.removeIf(item -> {
            Boolean isOp = (Boolean) item.get("isOp");
            String visible = (String) item.get("visible");
            if (isOp && !pl.isOp()) {
                return true;
            }

            if (Objects.equals(visible, "je")) {
                return true;
            }

            return false;
        });

        // 添加按钮
        for (Map<?, ?> item : buttons) {
            Map<String, String> BeItem = (Map<String, String>) item.get("Be");
            if(BeItem.get("path") != null) {
                // 地址图片
                formBuilder.button((String) item.get("name"), FormImage.Type.PATH, (String) BeItem.get("path"));
            } else if(BeItem.get("url") != null) {
                // URL图片
                formBuilder.button((String) item.get("name"), FormImage.Type.URL, (String) BeItem.get("url"));
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

                if(Objects.equals(item.get("type"), "cmd")) {
                    Bukkit.getServer().dispatchCommand(pl, (String) item.get("run"));
                }

                if(Objects.equals(item.get("type"), "console_cmd")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), (String) item.get("run"));
                }

                if(Objects.equals(item.get("type"), "form")) {
                    open(pl, (String) item.get("run"));
                }
            }
        });

        // 发送
        floodgateApi.sendForm(pl.getUniqueId(), formBuilder.build());
    }
}
