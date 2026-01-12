package core.gitee.xudai.mapper;

import core.gitee.xudai.config.loader.b.config.LicenseConfig;
import core.gitee.xudai.strategy.license.metadata.LicenseStrategyMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LicenseConfigMapper extends BaseConfigMapper {

    LicenseConfigMapper INSTANCE = Mappers.getMapper(LicenseConfigMapper.class);

    /**
     * 将LicenseConfig转换为LicenseStrategyMetadata
     * @param config 许可证配置类
     * @return 许可证策略元数据
     */
    @Mapping(source = "licenseInfo", target = "licenseStrategyInfo")
    LicenseStrategyMetadata toMetadata(LicenseConfig config);

    /**
     * 将LicenseConfig.LicenseInfo转换为LicenseStrategyMetadata.LicenseStrategyInfo
     * @param licenseInfo 配置中的许可证信息
     * @return 元数据中的许可证信息
     */
    @Mapping(source = "name", target = "name")
    @Mapping(source = "url", target = "url")
    @Mapping(source = "distribution", target = "distribution")
    LicenseStrategyMetadata.LicenseStrategyInfo toLicenseStrategyInfo(LicenseConfig.LicenseInfo licenseInfo);

}
