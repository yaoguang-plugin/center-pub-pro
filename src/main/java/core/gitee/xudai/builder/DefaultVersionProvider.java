package core.gitee.xudai.builder;

import java.util.HashMap;
import java.util.Map;

/**
 * 版本管理
 */
public class DefaultVersionProvider {
    private final Map<String, String> versions = new HashMap<>();

    public DefaultVersionProvider() {
        // 初始化默认版本
        versions.put("gpg", "3.0.1");
        versions.put("source", "3.2.1");
        versions.put("javadoc", "3.4.0");
        // 其他版本...
    }

    @Override
    public String getVersion(String pluginId) {
        return versions.getOrDefault(pluginId, "RELEASE");
    }
}
