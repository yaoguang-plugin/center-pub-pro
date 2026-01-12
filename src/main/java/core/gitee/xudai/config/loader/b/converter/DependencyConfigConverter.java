package core.gitee.xudai.config.loader.b.converter;

import core.gitee.xudai.config.loader.b.config.DependencyConfig;
import core.gitee.xudai.config.loader.b.config.PluginConfig;
import core.gitee.xudai.mapper.DependencyConfigMapper;
import core.gitee.xudai.mapper.PluginConfigMapper;
import core.gitee.xudai.strategy.dependency.metadata.DependencyStrategyMetadata;
import core.gitee.xudai.strategy.plugin.metadata.PluginStrategyMetadata;

/**
 * 配置到元数据转换器
 * @author daixu
 */
public class DependencyConfigConverter extends AbstractConfigToMetadataConverter<DependencyConfig, DependencyStrategyMetadata> {

    @Override
    protected DependencyStrategyMetadata doConvert(DependencyConfig config) {
        // 返回具体元数据类的实例（如PluginMetadata）
        return DependencyConfigMapper.INSTANCE.toMetadata(config);
    }

}
