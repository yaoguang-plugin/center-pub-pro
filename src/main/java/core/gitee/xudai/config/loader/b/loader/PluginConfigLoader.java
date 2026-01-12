package core.gitee.xudai.config.loader.b.loader;

import core.gitee.xudai.container.PluginConfigContainer;
import core.gitee.xudai.metadata.PluginConfigLoadMetadata;
import  core.gitee.xudai.enums.ConfigFilePathEnum;

/**
 * 插件配置加载器（加载plugin-config.yaml）
 * @author daixu
 */
public class PluginConfigLoader extends AbstractConfigLoader<PluginConfigContainer, PluginConfigLoadMetadata> {

//    private final String filePath = "plugin-config.yaml";
    private final String filePath;
    private PluginConfigContainer config;
    private final PluginConfigLoadMetadata metadata = new PluginConfigLoadMetadata();

    // 支持自定义文件路径，默认加载"plugin-config.yaml"

    /**
     * 构造器：传递差异化参数（父类通用逻辑直接复用）
     * 无需额外代码，父类已封装所有加载逻辑
     */
    public PluginConfigLoader() {
        // 传递：文件路径、配置容器Class、加载元数据实例
        super(
                ConfigFilePathEnum.PLUGIN.getFinalPath(), // 差异化：插件配置文件路径
                PluginConfigContainer.class, // 差异化：插件配置容器Class
                new PluginConfigLoadMetadata() // 差异化：插件加载元数据实例
        );
    }

    /**
     * 自定义校验：插件配置特有校验（如插件ID非空、baseInfo非空）
     */
//    @Override
//    protected void customValidate(List<PluginConfig> configs) {
//        List<PluginConfig> plugins = config.getPlugins();
//        if (plugins == null || plugins.isEmpty()) {
//            throw new IllegalArgumentException("插件配置文件中未配置任何插件");
//        }
//
//        // 校验每个插件的核心字段非空
//        for (PluginConfig plugin : plugins) {
//            if (plugin.getId() == null || plugin.getId().trim().isEmpty()) {
//                throw new IllegalArgumentException("插件ID不能为空：" + plugin.getTitle());
//            }
//            if (plugin.getBaseInfo() == null) {
//                throw new IllegalArgumentException("插件基础信息baseInfo不能为空：" + plugin.getId());
//            }
//        }
//    }

}
