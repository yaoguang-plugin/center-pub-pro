package core.gitee.xudai.factory;

import core.gitee.xudai.strategy.plugin.api.PluginStrategy;
import core.gitee.xudai.strategy.plugin.enums.PluginEnum;
import core.gitee.xudai.strategy.plugin.impl.central.DeployPluginStrategy;
import core.gitee.xudai.strategy.plugin.impl.javadoc.JavadocPluginStrategy;
import core.gitee.xudai.strategy.plugin.impl.source.SourcePluginStrategy;
import core.gitee.xudai.strategy.plugin.impl.central.CentralPublishingPluginStrategy;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * 插件策略工厂
 * 名称：工厂模式（Factory Pattern）
 * 作用：集中管理策略对象的创建，减少对象创建与使用的耦合。
 * @author daixu
 */
public class PluginStrategyFactory {

    // 策略缓存容器
    private static final Map<PluginEnum, PluginStrategy> STRATEGY_CACHE = new HashMap<>();

    static {
        // 1. 内置策略注册
        registerBuiltInStrategies();
        // 2. SPI加载外部策略（支持第三方扩展）
        loadExternalStrategies();
    }

    /**
     * 注册内置策略
     */
    private static void registerBuiltInStrategies() {
        register(PluginEnum.CENTRAL_PUBLISHING, new CentralPublishingPluginStrategy());
        register(PluginEnum.DEPLOY, new DeployPluginStrategy());
        register(PluginEnum.SOURCE, new SourcePluginStrategy());
        register(PluginEnum.JAVADOC, new JavadocPluginStrategy());
    }

    /**
     * 通过 SPI 加载外部策略
     */
    private static void loadExternalStrategies() {
        ServiceLoader<PluginStrategy> loader = ServiceLoader.load(PluginStrategy.class);
        for (PluginStrategy strategy : loader) {
            // 策略需实现获取类型的方法
            PluginEnum pluginEnum = strategy.getPluginType();
            if (pluginEnum != null) {
                register(pluginEnum, strategy);
            }
        }
    }

    /**
     * 注册策略
     */
    public static void register(PluginEnum type, PluginStrategy strategy) {
        if (STRATEGY_CACHE.containsKey(type)) {
            throw new IllegalArgumentException("策略已存在: " + type);
        }
        STRATEGY_CACHE.put(type, strategy);
    }

    /**
     * 获取策略实例
     */
    public static PluginStrategy getStrategy(PluginEnum type) throws MojoExecutionException {
        PluginStrategy strategy = STRATEGY_CACHE.get(type);
        if (strategy == null) {
            throw new MojoExecutionException("未找到插件策略: " + type);
        }
        return strategy;
    }

}
