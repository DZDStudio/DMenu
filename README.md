# DMenu
> Paper 间歇泉菜单插件

### 功能
- 支持自动生成 JE、BE 版菜单，一次配置多端使用
- 可以使用钟表快速打开
- 无钟表玩家入服自动给予
- 无间歇泉也可仅 JE 使用

### 配置
配置文件位于 `plugins/DMenu` 文件夹，其中 `main.yml` 为主菜单文件。

示例：
```yaml
name: 主菜单
# 菜单名称

buttons:
    -
        name: 前往测试菜单
        # 按钮名称
        open: cs
        # 点击按钮执行的操作内容
        # 如果按钮类型为 comm 或 opcomm 则填写需要玩家执行的命令（无需/）
        # 如果按钮类型为 form 或 opform 则填写要打开页面的名称 （即为文件名(去.yml后缀)）
        type: form
        # 点击按钮执行操作的类型，可选：comm opcomm form opform
        # comm：让玩家执行命令
        # opcomm：让玩家执行命令，（仅 OP 可见）
        # form：打开一个表单
        # opform：打开一个表单，（仅 OP 可见）
        item:
            # Java 版玩家 UI 中的按钮
            type: "grass_block"
            # 按钮物品类型,MC 标准类名，不要带 “minecraft:”
            lore:
            # 按钮的描述
                - "§7点我前往测试菜单"
        path: textures/ui/icon_recipe_nature.png
        # 基岩版玩家 UI 中的按钮图标 （本地材质）
        url: https://avatars.githubusercontent.com/u/142202241
        # 基岩版玩家 UI 中的按钮图标 （链接），如果 path 和 url 都填写，则优先使用 path，如两个参数否不存在则为纯文本按钮
```