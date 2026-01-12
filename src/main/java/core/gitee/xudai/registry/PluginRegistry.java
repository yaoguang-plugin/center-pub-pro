package core.gitee.xudai.registry;

import core.gitee.xudai.strategy.plugin.enums.PluginEnum;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 插件注册表
 */
public class PluginRegistry {

    private static final Set<PluginEnum> DEFAULT_PLUGINS = new LinkedHashSet<>();

    static {
        DEFAULT_PLUGINS.add(PluginEnum.CENTRAL_PUBLISHING);
        DEFAULT_PLUGINS.add(PluginEnum.GPG);
        DEFAULT_PLUGINS.add(PluginEnum.SOURCE);
        DEFAULT_PLUGINS.add(PluginEnum.JAVADOC);
    }

    public static Set<PluginEnum> getDefaultPlugins() {
        return Collections.unmodifiableSet(DEFAULT_PLUGINS);
    }

    public static Set<PluginEnum> getPluginsForPublishing() {
        return getDefaultPlugins();
    }
}
