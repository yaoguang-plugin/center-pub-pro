package core.gitee.xudai.strategy.plugin.enums;

import java.util.Optional;

public enum PluginIdEnum {
    // 每个枚举值对应一个插件，关联插件ID、名称、描述
    GPG("gpg", "GPG签名插件", "用于中央仓库发布时的GPG签名验证"),
    SOURCE("source", "源码插件", "生成源码Jar包"),
    JAVADOC("javadoc", "文档插件", "生成JavaDoc文档Jar包"),
    LICENSE("license", "许可证插件", "生成项目许可证文件");

    // 插件唯一ID（与YAML配置的pluginId一致）
    private final String code;
    // 插件名称
    private final String name;
    // 插件描述
    private final String description;

    PluginIdEnum(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    /**
     * 根据code获取枚举（用于配置加载、策略工厂校验）
     */
    public static Optional<PluginIdEnum> getByCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }
        for (PluginIdEnum pluginId : values()) {
            if (pluginId.code.equalsIgnoreCase(code)) {
                return Optional.of(pluginId);
            }
        }
        return Optional.empty();
    }

    // Getter
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
