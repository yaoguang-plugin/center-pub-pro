package core.gitee.xudai.strategy.plugin.api;

import core.gitee.xudai.entity.CentralPublishConfig;
import core.gitee.xudai.strategy.core.api.MavenStrategy;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * 插件配置策略接口，定义插件相关的策略行为
 * @author daixu
 */
public interface PluginStrategy extends MavenStrategy {

    /**
     * 获取插件策略描述
     * @return 策略描述
     * @author daixu
     */
    default String getPluginDescription() {
        return "配置" + getStrategyName() + "插件的默认策略";
    }

    // 检查插件是否与当前许可证兼容
    boolean isCompatibleWithLicense(CentralPublishConfig config);

    /**
     * 配置插件到 PluginManagement
     * @param config 中央仓库发布配置
     * @throws MojoExecutionException 配置过程中发生的异常
     * @author daixu
     */
    void configurePlugin(CentralPublishConfig config) throws MojoExecutionException;

    /**
     * 核心配置逻辑
     * @param config 中央仓库发布配置
     * @throws MojoExecutionException
     * @author daixu
     */
    @Override
    default void configure(CentralPublishConfig config) throws MojoExecutionException {
        // 插件策略的配置需依赖 PluginManagement 容器，因此默认不实现
        // 子类无需重写此方法，应通过 configurePlugin 完成配置
        configurePlugin(config);
        throw new UnsupportedOperationException("请使用 configurePlugin(PluginManagement, CentralPublishConfig)");
    }

}
