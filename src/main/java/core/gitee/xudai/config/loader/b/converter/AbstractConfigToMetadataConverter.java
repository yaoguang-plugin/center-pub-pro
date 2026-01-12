package core.gitee.xudai.config.loader.b.converter;

import core.gitee.xudai.config.loader.b.config.BaseConfig;
import core.gitee.xudai.strategy.core.metadata.BaseStrategyMetadata;
import lombok.extern.slf4j.Slf4j;

/**
 * 配置到元数据转换器的抽象父类，封装共性转换逻辑
 * @param <C> 配置类型（继承自 BaseConfig）
 * @param <M> 元数据类型（继承自 BaseStrategyMetadata）
 * @author daixu
 */
@Slf4j
public abstract class AbstractConfigToMetadataConverter<C extends BaseConfig<M>, M extends BaseStrategyMetadata<M>> implements ConfigToMetadataConverter<C, M> {

    /**
     * 模板方法：定义转换流程
     * 1. 校验配置
     * 2. 初始化元数据
     * 3. 映射基础字段（共性）
     * 4. 映射个性化字段（子类实现）
     * 5. 返回元数据
     */
    @Override
    public final M convert(C config) {

        // 1. 校验配置（共性）
        validateConfig(config);

        // 2. 初始化元数据（由子类提供具体实例）
//        M metadata = createMetadataInstance();

        // 2. 子类实现：调用 MapStruct 映射器完成转换
        M metadata = doConvert(config);

        log.info("配置转换完成，ID: {}", config.getId());
        return metadata;
    }

    /**
     * 共性：校验配置合法性（复用BaseConfig的validate方法）
     */
    protected void validateConfig(C config) {
        log.info("开始校验配置，ID: {}", config.getId());
        config.validate();
        log.info("配置校验通过，ID: {}", config.getId());
    }


    /**
     * 抽象方法：创建元数据实例（由子类指定具体类型）
     */
//    protected abstract M createMetadataInstance();

    /**
     * 抽象方法：子类实现具体的 MapStruct 转换逻辑
     */
    protected abstract M doConvert(C config);


}
