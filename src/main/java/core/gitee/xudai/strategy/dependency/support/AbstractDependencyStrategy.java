package core.gitee.xudai.strategy.dependency.support;

import core.gitee.xudai.strategy.dependency.api.DependencyStrategy;
import core.gitee.xudai.entity.CentralPublishConfig;
import core.gitee.xudai.strategy.dependency.enums.DependencyEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.List;

/**
 * 依赖策略抽象基类，封装依赖配置的通用逻辑
 * @author daixu
 */
@Slf4j
public abstract class AbstractDependencyStrategy implements DependencyStrategy {

    /**
     * 插件枚举
     */
    private final DependencyEnum dependencyEnum;

    /**
     * 构造函数
     * @param dependencyEnum 插件枚举
     */
    public AbstractDependencyStrategy(DependencyEnum dependencyEnum) {
        this.dependencyEnum = dependencyEnum;
    }

    /**
     * 获取插件名称（通常为 artifactId ）
     * @return 插件名称
     * @author daixu
     */
    protected String getDependencyName() {
        return dependencyEnum.getArtifactId();
    };

    /**
     * 策略名称（用于日志和异常定位）
     */
    @Override
    public String getStrategyName() {
        return dependencyEnum.getDescription();
    };

    /**
     * 默认启用所有插件策略（子类可重写为“根据配置决定”）
     */
    @Override
    public boolean isEnabled(CentralPublishConfig config) {
        log.debug("[{}] 使用默认启用策略", getStrategyName());
        // 默认启用
        return true;
    }

    /**
     * 默认非必需策略（子类可重写为“必需”）
     */
    @Override
    public boolean isRequired(CentralPublishConfig config) {
        // 默认非必需
        return false;
    }

    /**
     * 通用依赖配置逻辑：创建依赖→添加到依赖列表
     * @param config 中央仓库发布配置
     * @throws MojoExecutionException 配置过程中发生的异常
     * @author daixu
     */
    @Override
    public void configureDependency(CentralPublishConfig config) throws MojoExecutionException {

        BasicDependency basicDependency = getBasicDependency(config);
        if (basicDependency == null) {
            throw wrapException("依赖基础信息不能为空");
        }

        // 检测工程中是否已包含该依赖
        boolean hasExistingDependency = DependencyDetector.hasExistingDependency(
                config.getProject(),
                basicDependency.getGroupId(),
                basicDependency.getArtifactId()
        );

        if (hasExistingDependency) {
            log.info("[{}] 工程中已包含该依赖，将使用工程中的配置，跳过依赖策略", getStrategyName());
            return;
        }

        List<Dependency> dependencies = config.getProject().getDependencies();
        Dependency dependency = DependencyConfigBuilder.createDependency(basicDependency, getScope());
        dependencies.add(dependency);
        log.info("[{}] 已添加依赖: {}", getStrategyName(), basicDependency.getArtifactId());
    }

    /**
     * 抽象方法：由子类提供具体的依赖信息
     */
    protected BasicDependency getBasicDependency(CentralPublishConfig config) throws MojoExecutionException {
        BasicDependency dependency = new BasicDependency();
        dependency.setGroupId(dependencyEnum.getGroupId());
        dependency.setArtifactId(dependencyEnum.getArtifactId());
        dependency.setVersion(dependencyEnum.getVersion());
        // TODO 作用域，排除依赖等功能
        return dependency;
    }

    /**
     * 依赖作用域（默认空，子类可重写）
     */
    protected String getScope() {
        return "";
    }

    /**
     * 统一异常包装
     */
    protected MojoExecutionException wrapException(String message) {
        return new MojoExecutionException("[" + getStrategyName() + "] " + message);
    }

}
