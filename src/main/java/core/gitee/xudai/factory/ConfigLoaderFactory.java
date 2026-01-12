package core.gitee.xudai.factory;

import core.gitee.xudai.config.loader.b.loader.ConfigLoaderContainer;
import core.gitee.xudai.config.loader.b.loader.DependencyConfigLoader;
import core.gitee.xudai.config.loader.b.loader.LicenseConfigLoader;
import core.gitee.xudai.config.loader.b.loader.PluginConfigLoader;
import core.gitee.xudai.container.DependencyConfigContainer;
import core.gitee.xudai.container.LicenseConfigContainer;
import core.gitee.xudai.container.PluginConfigContainer;
import core.gitee.xudai.metadata.BaseConfigLoadMetadata;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置加载器工厂：通过工厂模式管理配置加载器，避免硬编码
 * @author daixu
 */
public class ConfigLoaderFactory {

    private static final Map<Class<?>, ConfigLoaderContainer<?, ?>> loaders = new HashMap<>();

    static {
        // 注册加载器，新增类型仅需添加注册
        loaders.put(PluginConfigContainer.class, new PluginConfigLoader());
        loaders.put(LicenseConfigContainer.class, new LicenseConfigLoader());
        loaders.put(DependencyConfigContainer.class, new DependencyConfigLoader());
    }

    @SuppressWarnings("unchecked")
    public static <T, M extends BaseConfigLoadMetadata> ConfigLoaderContainer<T, M> getLoader(Class<T> configClass) {
        return (ConfigLoaderContainer<T, M>) loaders.get(configClass);
    }

}
