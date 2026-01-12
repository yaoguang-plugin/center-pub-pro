package core.gitee.xudai.mapper;

import core.gitee.xudai.config.loader.b.config.PluginConfig;
import core.gitee.xudai.strategy.plugin.metadata.PluginStrategyMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PluginConfigMapper extends BaseConfigMapper  {

    PluginConfigMapper INSTANCE = Mappers.getMapper(PluginConfigMapper.class);

    /**
     * 将PluginConfig转换为PluginStrategyMetadata
     * @param config 插件配置类
     * @return 插件策略元数据
     */
    @Mapping(source = "pluginInfo", target = "pluginStrategyInfo")
    PluginStrategyMetadata toMetadata(PluginConfig config);

    /**
     * 将PluginConfig.PluginInfo转换为PluginStrategyMetadata.PluginStrategyInfo
     * @param pluginInfo 配置中的插件信息
     * @return 元数据中的插件信息
     */
    @Mapping(source = "groupId", target = "groupId")
    @Mapping(source = "artifactId", target = "artifactId")
    @Mapping(source = "version", target = "version")
    @Mapping(source = "expandTags", target = "expandTags")
    @Mapping(source = "configuration", target = "configuration")
    @Mapping(source = "executions", target = "executions")
    PluginStrategyMetadata.PluginStrategyInfo toPluginStrategyInfo(PluginConfig.PluginInfo pluginInfo);

}
