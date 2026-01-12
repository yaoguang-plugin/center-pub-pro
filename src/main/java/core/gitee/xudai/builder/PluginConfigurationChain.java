package core.gitee.xudai.builder;

import core.gitee.xudai.entity.CentralPublishConfig;
import core.gitee.xudai.strategy.plugin.api.PluginStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.List;

/**
 * 责任链模式（Chain of Responsibility）
 * 作用：处理插件配置之间的依赖关系，按顺序执行配置流程。
 * @author daixu
 *
 * // 使用方式
 * List<PluginStrategy> strategies = PluginStrategyFactory.getAllStrategies();
 * new PluginConfigurationChain(strategies).execute(pluginManagement, config);
 */
@Slf4j
public class PluginConfigurationChain {

    private List<PluginStrategy> strategies;

    private int currentIndex = 0;

    public PluginConfigurationChain(List<PluginStrategy> strategies) {
        this.strategies = strategies;
    }

    public void execute(CentralPublishConfig config) throws MojoExecutionException {
        if (currentIndex < strategies.size()) {
            PluginStrategy strategy = strategies.get(currentIndex++);
            try {
                strategy.configure(config);
                // 执行下一个策略
                execute(pluginManagement, config);
            } catch (SkipExecutionException e) {
                log.info("跳过插件[{}]配置: {}", strategy.getStrategyName(), e.getMessage());
                execute(pluginManagement, config);
            } catch (CriticalConfigurationException e) {
                log.error("插件[{}]配置失败，终止流程: {}", strategy.getStrategyName(), e.getMessage());
                throw e;
            }
        }
    }

}
