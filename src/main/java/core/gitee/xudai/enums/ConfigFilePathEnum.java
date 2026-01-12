package core.gitee.xudai.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置文件路径枚举：统一维护所有配置文件的路径（默认路径+外部可覆盖）
 * @author daixu
 */
@Getter
public enum ConfigFilePathEnum {

    // 插件配置文件（逻辑标识：PLUGIN，默认路径：plugin-config.yaml）
    PLUGIN("plugin.config.path", "plugin-config.yaml"),
    // 许可证配置文件（逻辑标识：LICENSE，默认路径：license-config.yaml）
    LICENSE("license.config.path", "license-config.yaml"),
    // 依赖配置文件（逻辑标识：DEPENDENCY，默认路径：dependency-config.yaml）
    DEPENDENCY("dependency.config.path", "dependency-config.yaml");

    // 外部配置文件中的key（用于覆盖默认路径）
    private final String configKey;
    // 默认路径（类路径/本地路径）
    private final String defaultPath;
    // 外部配置文件（存放路径覆盖配置）
    private static final Properties EXTERNAL_CONFIG = new Properties();
    // 外部配置文件名称（类路径下）
    private static final String EXTERNAL_CONFIG_FILE = "config-paths.properties";

    // 静态代码块：加载外部配置文件（仅加载一次）
    static {
        try (InputStream is = ConfigFilePathEnum.class.getClassLoader().getResourceAsStream(EXTERNAL_CONFIG_FILE)) {
            if (is != null) {
                EXTERNAL_CONFIG.load(is);
                System.out.println("成功加载外部路径配置文件：" + EXTERNAL_CONFIG_FILE);
            } else {
                System.out.println("未找到外部路径配置文件，使用默认路径：" + EXTERNAL_CONFIG_FILE);
            }
        } catch (IOException e) {
            throw new RuntimeException("加载外部路径配置文件失败：" + EXTERNAL_CONFIG_FILE, e);
        }
    }

    ConfigFilePathEnum(String configKey, String defaultPath) {
        this.configKey = configKey;
        this.defaultPath = defaultPath;
    }

    /**
     * 获取最终使用的路径（优先级：外部配置 > 默认路径）
     */
    public String getFinalPath() {
        // 从外部配置文件中读取路径（如config-paths.properties中的plugin.config.path=xxx）
        String externalPath = EXTERNAL_CONFIG.getProperty(configKey);
        // 外部配置不为空则用外部路径，否则用默认路径
        return StringUtils.isNotBlank(externalPath) ? externalPath.trim() : defaultPath;
    }

}
