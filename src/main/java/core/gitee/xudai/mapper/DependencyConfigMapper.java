package core.gitee.xudai.mapper;

import core.gitee.xudai.config.loader.b.config.DependencyConfig;
import core.gitee.xudai.strategy.dependency.metadata.DependencyStrategyMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 依赖配置到元数据的映射器（MapStruct 自动生成实现类）
 * componentModel = "default"：生成普通 Java 类（非 Spring Bean，无容器依赖）
 */
/**
 * 依赖配置映射器：继承父类映射器，补充特有属性转换
 * @author daixu
 */
@Mapper(componentModel = "default")
public interface DependencyConfigMapper extends BaseConfigMapper,ArtifactInfoMapper {

    // 单例实例，直接调用
    DependencyConfigMapper INSTANCE = Mappers.getMapper(DependencyConfigMapper.class);

    /**
     * 子类特有属性映射：补充 dependencyInfo → dependencyStrategyInfo 的转换
     * 父类属性（id、title、baseInfo 等）的映射规则自动继承自 BaseConfigMapper
     */
    @Mapping(source = "dependencyInfo", target = "dependencyStrategyInfo")
    DependencyStrategyMetadata toMetadata(DependencyConfig config);

    /**
     * 子类特有内部类映射：DependencyConfig.DependencyInfo → DependencyStrategyMetadata.DependencyStrategyInfo
     */
//    @Mapping(source = "groupId", target = "groupId")
//    @Mapping(source = "artifactId", target = "artifactId")
//    @Mapping(source = "version", target = "version")
//    @Mapping(source = "type", target = "type")
//    @Mapping(source = "scope", target = "scope")
//    @Mapping(source = "optional", target = "optional")
//    @Mapping(source = "classifier", target = "classifier")
//    @Mapping(source = "exclusions", target = "exclusions")
//    @Mapping(source = "systemPath", target = "systemPath")
//    @Mapping(source = "versionRange", target = "versionRange")
//    DependencyStrategyMetadata.DependencyStrategyInfo toDependencyStrategyInfo(DependencyConfig.DependencyInfo info);

}
