package core.gitee.xudai.facade;

import core.gitee.xudai.entity.CentralPublishConfig;
import core.gitee.xudai.strategy.plugin.enums.PluginEnum;
import core.gitee.xudai.factory.PluginStrategyFactory;
import core.gitee.xudai.registry.PluginRegistry;
import core.gitee.xudai.strategy.plugin.api.PluginStrategy;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.plugin.MojoExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * 插件配置门面
 */
public class PluginConfigurationFacade {
    private static final Logger logger = LoggerFactory.getLogger(PluginConfigurationFacade.class);
    private final CentralPublishConfig config;

    public PluginConfigurationFacade(CentralPublishConfig config) {
        this.config = config;
    }

    public void configureAllPlugins(PluginManagement pluginManagement) throws MojoExecutionException {
        Set<PluginEnum> plugins = PluginRegistry.getPluginsForPublishing();
        int configuredCount = 0;

        for (PluginEnum pluginType : plugins) {
            PluginStrategy strategy = PluginStrategyFactory.getStrategy(pluginType);

            // 检查是否跳过插件
            if (isPluginSkipped(pluginType)) {
                logger.debug("跳过插件: {}", strategy.getStrategyName());
                continue;
            }

            // 检查许可证兼容性
            if (!strategy.isCompatibleWithLicense(config)) {
                throw new MojoExecutionException(
                        "插件" + strategy.getStrategyName() + "与当前许可证不兼容: " + config.getLicenseType()
                );
            }

            // 配置插件
            strategy.configurePlugin(pluginManagement, config);
            configuredCount++;
        }

        logger.info("已完成插件配置，共配置 {} 个插件", configuredCount);
    }

    // 判断插件是否被跳过
    private boolean isPluginSkipped(PluginEnum type) {
        return switch (type) {
            case GPG -> config.isSkipGpg();
            case SOURCE -> config.isSkipSource();
            case JAVADOC -> config.isSkipJavadoc();
            case CENTRAL_PUBLISHING -> config.isSkip();
            default -> false;
        };
    }
}
