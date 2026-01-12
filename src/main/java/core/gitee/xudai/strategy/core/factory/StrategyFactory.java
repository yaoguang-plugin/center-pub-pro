package core.gitee.xudai.strategy.core.factory;

import core.gitee.xudai.strategy.core.metadata.StrategyMetadata;
import core.gitee.xudai.strategy.license.api.LicenseStrategy;
import core.gitee.xudai.strategy.license.metadata.LicenseStrategyMetadata;
import core.gitee.xudai.strategy.plugin.impl.gpg.GpgPluginStrategy;
import core.gitee.xudai.strategy.plugin.metadata.PluginStrategyMetadata;
import org.codehaus.plexus.classworlds.strategy.Strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * 策略工厂
 */
public interface StrategyFactory {

//    private static final Map<String, StrategyCreator> STRATEGY_MAP = new HashMap<>();
//
//    // 静态注册策略创建器
//    static {
//        STRATEGY_MAP.put("plugin", metadata -> {
//            if (metadata.getId().equals("gpg")) {
//                return new GpgPluginStrategy((PluginStrategyMetadata) metadata);
//            }
//            // 其他插件策略...
//            return null;
//        });
//        STRATEGY_MAP.put("license", metadata -> new LicenseStrategy((LicenseStrategyMetadata) metadata));
//    }
//
//    // 函数式接口：用于创建策略
//    @FunctionalInterface
//    private interface StrategyCreator {
//        Strategy create(StrategyMetadata metadata);
//    }
//
//    // 根据元数据获取策略
//    static Strategy getStrategy(StrategyMetadata metadata) {
//        StrategyCreator creator = STRATEGY_MAP.get(metadata.getStrategyType());
//        if (creator == null) {
//            throw new IllegalArgumentException("未知策略类型：" + metadata.getStrategyType());
//        }
//        return creator.create(metadata);
//    }
}
