package core.gitee.xudai.config.loader.b.converter;

import core.gitee.xudai.config.loader.b.config.PluginConfig;
import core.gitee.xudai.mapper.PluginConfigMapper;
import core.gitee.xudai.strategy.plugin.metadata.PluginStrategyMetadata;

/**
 * 配置到元数据转换器
 * @author daixu
 */
public class PluginConfigConverter extends AbstractConfigToMetadataConverter<PluginConfig, PluginStrategyMetadata> {

    @Override
    protected PluginStrategyMetadata doConvert(PluginConfig config) {
        // 返回具体元数据类的实例（如PluginMetadata）
        return PluginConfigMapper.INSTANCE.toMetadata(config);
    }

}
