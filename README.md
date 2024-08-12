# DMenu
> Paper 间歇泉菜单插件

### 功能
- 支持自动生成 JE、BE 版菜单，一次配置多端使用
- 可以使用钟表快速打开
- 无钟表玩家入服自动给予
- 无间歇泉也可仅 JE 使用

### 配置
`plugins/DMenu/config.yml`:
```yaml
openItemType: "clock"
# 打开菜单物品类型,MC 标准类名，不要带 “minecraft:”
openItemName: "§6菜单"
# 打开菜单物品显示名称
openItemLore:
# 打开菜单物品的描述
    - "§7使用我打开菜单"
```
`plugins/DMenu/Forms/main.yml`:
```yaml
name: 主菜单
# 菜单名称

content: "§7Hi! \n欢迎回来!"
# 菜单内容

buttons:
    # 按钮
    -
        name: 前往测试菜单
        # 按钮名称

        run: cs
        # 点击按钮执行的内容
        # 如果按钮类型为 cmd 或 console_cmd 则填写需要玩家执行的命令（无需/）
        # 如果按钮类型为 form 则填写要打开页面的文件名称 (去.yml后缀)

        type: form
        # 点击按钮执行操作的类型，可选：cmd console_cmd form
        # cmd：让玩家执行命令
        # console_cmd：后台执行命令
        # form：打开一个表单

        visible: all
        # 可见客户端类型，可选：all be je
        # all：所有玩家可见
        # be：仅 BE 玩家可见
        # je：仅 JE 玩家可见

        isOp: false
        # 仅 Op 可见

        Je:
            # Je 玩家按钮
            # Java 版玩家 UI 中的按钮
            type: "grass_block"
            # 按钮物品类型,MC 标准类名，不要带 “minecraft:”
            lore:
                # 按钮的描述
                - "§7点我前往测试菜单"

        Be:
            # Be 玩家按钮
            path: textures/ui/icon_recipe_nature.png
            # 基岩版玩家 UI 中的按钮图标 （本地材质）
            url: https://avatars.githubusercontent.com/u/142202241
            # 基岩版玩家 UI 中的按钮图标 （链接），如果 path 和 url 都填写，则优先使用 path，如两个参数否不存在则为纯文本按钮
```

### 结尾
如果您觉得本项目有用，请点一个 Star ，这会对我非常有帮助！
遇到问题或请求功能请前往 Issues ，或加入插件交流QQ群：[674416045](https://qm.qq.com/q/qzy8d9nEAK)