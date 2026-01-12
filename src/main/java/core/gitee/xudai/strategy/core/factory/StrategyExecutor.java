package core.gitee.xudai.strategy.core.factory;

import core.gitee.xudai.config.loader.b.loader.ConfigLoader;
import core.gitee.xudai.container.PluginConfigContainer;
import core.gitee.xudai.strategy.plugin.metadata.PluginStrategyMetadata;
import org.codehaus.plexus.classworlds.strategy.Strategy;

import java.util.Comparator;
import java.util.List;

public class StrategyExecutor {

    public void executeAll() {
        // 1. 加载插件配置元数据
        PluginConfigContainer pluginConfig = ConfigLoader.getInstance().getPluginConfig();
        List<PluginStrategyMetadata> pluginMetadatas = pluginConfig.getPlugins();

        // 2. 按执行顺序排序（基于元数据的order）
        pluginMetadatas.sort(Comparator.comparingInt(m -> m.getBaseStrategyInfo().getOrder()));

        // 3. 执行每个策略
        for (PluginStrategyMetadata metadata : pluginMetadatas) {
            Strategy strategy = StrategyFactory.getStrategy(metadata);
            if (strategy != null) {
                strategy.execute();
            }
        }
    }

}
