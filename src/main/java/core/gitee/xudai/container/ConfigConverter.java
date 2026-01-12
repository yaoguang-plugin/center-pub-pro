package core.gitee.xudai.container;

import core.gitee.xudai.strategy.core.metadata.BaseStrategyMetadata;

/**
 * 配置转换器接口：定义配置类到元数据的转换契约
 * @param <T> 元数据类型
 * @author daixu
 */
public interface ConfigConverter <T extends BaseStrategyMetadata<?>>{

    /**
     * 将配置转换为策略元数据
     * @return 策略元数据
     */
    T convertToMetadata();

}
