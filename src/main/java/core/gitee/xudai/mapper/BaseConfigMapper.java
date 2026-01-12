package core.gitee.xudai.mapper;

import core.gitee.xudai.config.loader.b.config.BaseConfig;
import core.gitee.xudai.strategy.core.metadata.BaseStrategyMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * 父类配置映射器：处理 BaseConfig → BaseStrategyMetadata 的公共属性转换
 * 子类映射器可继承此接口，复用公共映射逻辑
 */
@Mapper(componentModel = "default") // 无 Spring 依赖，生成普通 Java 类
public interface BaseConfigMapper {

    /**
     * 父类公共属性映射：BaseConfig → BaseStrategyMetadata
     * 子类映射器继承后，会自动包含这些映射规则
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "serialNumber", target = "serialNumber")
    @Mapping(source = "baseInfo", target = "baseStrategyInfo") // 内部类映射
    @Mapping(source = "id", target = "strategyId", qualifiedByName = "generateStrategyId") // 自定义策略ID
    BaseStrategyMetadata<?> toBaseMetadata(BaseConfig<?> config);

    /**
     * 内部类映射：BaseConfig.BaseInfo → BaseStrategyMetadata.BaseStrategyInfo
     * 公共内部类的转换逻辑，子类可直接复用
     */
    @Mapping(source = "order", target = "order")
    @Mapping(source = "enabled", target = "enabled")
    @Mapping(source = "required", target = "required")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "dependencies", target = "dependencies")
    BaseStrategyMetadata.BaseStrategyInfo toBaseStrategyInfo(BaseConfig.BaseInfo baseInfo);

    /**
     * 自定义策略ID生成："strategy-" + id
     */
    @Named("generateStrategyId")
    default String generateStrategyId(String id) {
        return "strategy-" + id;
    }

}
