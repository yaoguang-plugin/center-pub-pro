package core.gitee.xudai.config.loader.b.factory;

import core.gitee.xudai.config.loader.b.config.DependencyConfig;
import core.gitee.xudai.config.loader.b.config.LicenseConfig;
import core.gitee.xudai.config.loader.b.config.PluginConfig;
import core.gitee.xudai.config.loader.b.config.BaseConfig;
import core.gitee.xudai.config.loader.b.converter.ConfigToMetadataConverter;
import core.gitee.xudai.config.loader.b.converter.DependencyConfigConverter;
import core.gitee.xudai.config.loader.b.converter.LicenseConfigConverter;
import core.gitee.xudai.config.loader.b.converter.PluginConfigConverter;
import core.gitee.xudai.strategy.core.metadata.BaseStrategyMetadata;

import java.util.HashMap;
import java.util.Map;

public class ConverterFactory {

    // 缓存：配置类Class → 对应的转换器
    private static final Map<Class<?>, ConfigToMetadataConverter<?, ?>> CONVERTERS = new HashMap<>();

    // 静态注册转换器（修正LicenseConfig的转换器）
    static {
        CONVERTERS.put(PluginConfig.class, new PluginConfigConverter());
        CONVERTERS.put(DependencyConfig.class, new DependencyConfigConverter());
        CONVERTERS.put(LicenseConfig.class, new LicenseConfigConverter()); // 改为License自己的转换器
    }

    /**
     * 获取对应配置类的转换器（泛型约束对齐）
     * @param configClass 配置类的Class
     * @param <E> 配置类（必须是BaseConfig的子类，且BaseConfig的泛型是METADATA）
     * @param <METADATA> 元数据类（必须是BaseStrategyMetadata的子类）
     * @return 类型匹配的转换器
     */
    @SuppressWarnings("unchecked")
    public static <E extends BaseConfig<METADATA>, METADATA extends BaseStrategyMetadata<METADATA>>
    ConfigToMetadataConverter<E, METADATA> getConverter(Class<E> configClass) {
        // 强制类型转换（因为注册时已确保类型匹配，所以安全）
        return (ConfigToMetadataConverter<E, METADATA>) CONVERTERS.get(configClass);
    }

}
