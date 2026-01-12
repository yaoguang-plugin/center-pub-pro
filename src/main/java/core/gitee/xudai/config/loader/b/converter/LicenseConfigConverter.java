package core.gitee.xudai.config.loader.b.converter;

import core.gitee.xudai.config.loader.b.config.LicenseConfig;
import core.gitee.xudai.config.loader.b.config.PluginConfig;
import core.gitee.xudai.mapper.LicenseConfigMapper;
import core.gitee.xudai.mapper.PluginConfigMapper;
import core.gitee.xudai.strategy.license.metadata.LicenseStrategyMetadata;
import core.gitee.xudai.strategy.plugin.metadata.PluginStrategyMetadata;

/**
 * 配置到元数据转换器
 * @author daixu
 */
public class LicenseConfigConverter extends AbstractConfigToMetadataConverter<LicenseConfig, LicenseStrategyMetadata> {

    @Override
    protected LicenseStrategyMetadata doConvert(LicenseConfig config) {
        // 返回具体元数据类的实例（如PluginMetadata）
        return LicenseConfigMapper.INSTANCE.toMetadata(config);
    }

}
