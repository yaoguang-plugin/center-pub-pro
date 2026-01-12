package core.gitee.xudai.strategy.core.metadata;

/**
 * 策略元数据接口，定义所有策略元数据类的通用行为规范
 * 所有具体策略元数据类（如许可证、依赖、插件）必须实现此接口
 * @author daixu
 */
public interface StrategyMetadata {

    /**
     * 获取策略类型标识（如 "license"、"plugin"、"dependency"）
     * 用于策略工厂区分不同类型的策略，实现策略的动态选择与切换
     *
     * @return 策略类型字符串（非空）
     */
    String getStrategyType();

    /**
     * 判断策略是否可执行
     * 基于策略的基础配置（如enabled、required）决定是否参与执行流程
     *
     * @return true：可执行；false：不可执行
     */
    boolean isExecutable();

}
