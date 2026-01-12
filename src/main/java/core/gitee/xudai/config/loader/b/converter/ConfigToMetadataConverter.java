package core.gitee.xudai.config.loader.b.converter;

import core.gitee.xudai.config.loader.b.config.BaseConfig;
import core.gitee.xudai.strategy.core.metadata.BaseStrategyMetadata;

/**
 * 配置到元数据的转换器接口
 * @param <E> 配置类型（如 PluginConfig、DependencyConfig）
 * @param <METADATA> 元数据类型（如 PluginStrategyMetadata）
 * @author daixu
 */
public interface ConfigToMetadataConverter<
        E extends BaseConfig<METADATA>,  // 配置类：必须是BaseConfig的子类
        METADATA extends BaseStrategyMetadata<METADATA>  // 元数据类：必须是BaseStrategyMetadata的子类
        > {
    /**
     * 将配置转换为元数据（配置 → 元数据）
     * @param config 配置
     * @return 元数据
     */
    METADATA convert(E config);

}
