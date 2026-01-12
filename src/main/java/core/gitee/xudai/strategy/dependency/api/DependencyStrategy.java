package core.gitee.xudai.strategy.dependency.api;

import core.gitee.xudai.entity.CentralPublishConfig;
import core.gitee.xudai.strategy.core.api.MavenStrategy;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * 依赖配置策略接口，定义依赖相关的策略行为
 * @author daixu
 */
public interface DependencyStrategy extends MavenStrategy {

    /**
     * 获取依赖策略描述
     * @return 策略描述文本
     */
    default String getDependencyDescription() {
        return "配置" + getStrategyName() + "依赖的默认策略";
    }

    /**
     * 配置依赖并添加到依赖列表
     * @param config 中央仓库发布配置
     * @throws MojoExecutionException 配置过程中发生的异常
     */
    void configureDependency(CentralPublishConfig config) throws MojoExecutionException;

    @Override
    default void configure(CentralPublishConfig config) throws MojoExecutionException {
        // 空实现，避免子类必须实现，实际由带List<Dependency>参数的方法处理
        configureDependency(config);
    }

}
