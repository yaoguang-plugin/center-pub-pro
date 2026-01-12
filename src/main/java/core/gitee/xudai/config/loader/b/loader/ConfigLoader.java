package core.gitee.xudai.config.loader.b.loader;

import core.gitee.xudai.config.loader.b.config.PluginConfig;
import core.gitee.xudai.container.DependencyConfigContainer;
import core.gitee.xudai.container.LicenseConfigContainer;
import core.gitee.xudai.container.PluginConfigContainer;
import core.gitee.xudai.metadata.DependencyConfigLoadMetadata;
import core.gitee.xudai.metadata.LicenseConfigLoadMetadata;
import core.gitee.xudai.metadata.PluginConfigLoadMetadata;
import core.gitee.xudai.strategy.plugin.enums.PluginIdEnum;
import core.gitee.xudai.strategy.plugin.metadata.PluginStrategyMetadata;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 配置加载器（枚举单例实现，整合业务配置+元数据）
 * 配置管理枚举：统一整合所有配置加载器，提供单一访问入口
 *
 * 配置加载器（内部嵌套枚举实现单例，兼顾懒加载+线程安全+封装性）
 * 统一整合所有配置加载器，提供单一访问入口
 * @author daixu
 */
@Slf4j
public enum ConfigLoader {

    // 🔴 外部枚举仅作为统一访问入口，核心逻辑委托给内部嵌套枚举
    INSTANCE;

    // 📌 单例实例（枚举单例，线程安全、防反射）
    private enum SingletonHolder {

        // 🚀 内部枚举唯一实例
        INSTANCE;

        // 聚合各配置加载器（final修饰，初始化后不可修改，保证线程安全）
        @Getter
        private final PluginConfigLoader pluginLoader = new PluginConfigLoader();
        @Getter
        private final LicenseConfigLoader licenseLoader = new LicenseConfigLoader();
        @Getter
        private final DependencyConfigLoader dependencyLoader = new DependencyConfigLoader();

        // 全局配置容器（存储整合后的所有配置）
        private GlobalConfig config;

        /**
         * 内部枚举构造器：仅初始化一次，执行配置加载
         */
        SingletonHolder() {
            this.config = loadConfig();
        }

        private GlobalConfig loadConfig() {

            GlobalConfig globalConfig = new GlobalConfig();

            // 加载插件配置并赋值
            pluginLoader.load();
            if (this.config != null) {
                this.config.setPluginConfigContainer(pluginLoader.getConfig());
            }
//            this.config.setP(pluginLoader.getMetadata());

            // 加载许可证配置并赋值
            licenseLoader.load();
            if (this.config != null) {
                this.config.setLicenseConfigContainer(licenseLoader.getConfig());
            }

            // 加载依赖配置并赋值
            dependencyLoader.load();
            if (this.config != null) {
                this.config.setDependencyConfigContainer(dependencyLoader.getConfig());
            }

            return globalConfig;
        }

        // -------------------------- 内部枚举get方法（对外暴露核心组件） --------------------------

        public GlobalConfig getGlobalConfig() {
            return config;
        }
    }

    // 初始化：统一触发所有配置加载
//    ConfigLoader() {
//        pluginLoader.load();
//        licenseLoader.load();
//        dependencyLoader.load();
//    }

    // TODO 获取所有配置
    // TODO 通过 ID 获取配置信息

    // -------------------------- 对外提供的统一访问方法 --------------------------

    // -------------------------- 外部枚举对外开放的get方法（统一访问入口） --------------------------

    // 获取单例实例
    public static ConfigLoader getInstance() {
        // 添加初始化校验：确保配置已加载
        if (INSTANCE.getGlobalConfig() == null) {
            throw new IllegalStateException("配置未初始化完成！");
        }
        // 添加日志：追踪单例访问时机
        log.info("获取ConfigLoader单例，当前时间：{}", LocalDateTime.now());
        return ConfigLoader.INSTANCE;
    }

    /**
     * 获取全局配置容器（包含所有配置和元数据）
     */
    public GlobalConfig getGlobalConfig() {
        return SingletonHolder.INSTANCE.getGlobalConfig();
    }

    /**
     * 获取插件业务配置
     */
    public PluginConfigContainer getPluginConfig() {
        return SingletonHolder.INSTANCE.getPluginLoader().getConfig();
    }

    /**
     * 获取许可证业务配置
     */
    public LicenseConfigContainer getLicenseConfig() {
        return SingletonHolder.INSTANCE.getLicenseLoader().getConfig();
    }

    /**
     * 获取依赖业务配置
     */
    public DependencyConfigContainer getDependencyConfig() {
        return SingletonHolder.INSTANCE.getDependencyLoader().getConfig();
    }

    /**
     * 获取插件配置元数据
     */
    public PluginConfigLoadMetadata getPluginConfigMetadata() {
        return SingletonHolder.INSTANCE.getPluginLoader().getMetadata();
    }

    /**
     * 获取许可证配置元数据
     */
    public LicenseConfigLoadMetadata getLicenseConfigMetadata() {
        return SingletonHolder.INSTANCE.getLicenseLoader().getMetadata();
    }

    /**
     * 获取依赖配置元数据
     */
    public DependencyConfigLoadMetadata getDependencyConfigMetadata() {
        return SingletonHolder.INSTANCE.getDependencyLoader().getMetadata();
    }

    // -------------------------- 全局配置容器类（聚合业务配置+元数据） --------------------------
    /**
     * 全局配置容器：统一存储所有配置和对应元数据
     */
    public static class GlobalConfig {

        // 业务配置容器
        private PluginConfigContainer pluginConfigContainer;
        private LicenseConfigContainer licenseConfigContainer;
        private DependencyConfigContainer dependencyConfigContainer;

        // 对应元数据
        private PluginConfigLoadMetadata pluginConfigMetadata;
        private LicenseConfigLoadMetadata licenseConfigMetadata;
        private DependencyConfigLoadMetadata dependencyConfigMetadata;

        // -------------------------- GlobalConfig的get/set方法 --------------------------
        public PluginConfigContainer getPluginConfigContainer() {
            return pluginConfigContainer;
        }

        public void setPluginConfigContainer(PluginConfigContainer pluginConfigContainer) {
            this.pluginConfigContainer = pluginConfigContainer;
        }

        public LicenseConfigContainer getLicenseConfigContainer() {
            return licenseConfigContainer;
        }

        public void setLicenseConfigContainer(LicenseConfigContainer licenseConfigContainer) {
            this.licenseConfigContainer = licenseConfigContainer;
        }

        public DependencyConfigContainer getDependencyConfigContainer() {
            return dependencyConfigContainer;
        }

        public void setDependencyConfigContainer(DependencyConfigContainer dependencyConfigContainer) {
            this.dependencyConfigContainer = dependencyConfigContainer;
        }

        public PluginConfigLoadMetadata getPluginConfigMetadata() {
            return pluginConfigMetadata;
        }

        public void setPluginConfigMetadata(PluginConfigLoadMetadata pluginConfigMetadata) {
            this.pluginConfigMetadata = pluginConfigMetadata;
        }

        public LicenseConfigLoadMetadata getLicenseConfigMetadata() {
            return licenseConfigMetadata;
        }

        public void setLicenseConfigMetadata(LicenseConfigLoadMetadata licenseConfigMetadata) {
            this.licenseConfigMetadata = licenseConfigMetadata;
        }

        public DependencyConfigLoadMetadata getDependencyConfigMetadata() {
            return dependencyConfigMetadata;
        }

        public void setDependencyConfigMetadata(DependencyConfigLoadMetadata dependencyConfigMetadata) {
            this.dependencyConfigMetadata = dependencyConfigMetadata;
        }
    }

    // ------------------------------ 对外提供的获取配置方法 ------------------------------

    /**
     * 获取所有插件配置 getAllPluginConfigs
     */
    public List<PluginConfig> getAlls() {
        return globalConfig.getPlugins();
    }

    /**
     * 根据pluginId获取单个插件配置（常用）
     */
    public PluginConfig getById(String pluginId) {
        return getAllPlugins().stream()
                .filter(plugin -> pluginId.equals(plugin.getPluginId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未找到pluginId为[" + pluginId + "]的配置"));
    }

    // 获取所有插件配置
    public List<PluginConfig> getAllConfigs() {
        return PluginConfigLoader2.SingletonHolder.INSTANCE.config.getPlugins();
    }

    // 根据pluginId获取单个插件配置
    public PluginConfig getPluginConfig(String pluginId) {
        return getAllPluginConfigs().stream()
                .filter(config -> pluginId.equals(config.getPluginId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未找到插件配置：" + pluginId));
    }

    /**
     * 校验YAML中的pluginId是否都在PluginIdEnum中定义
     */
    private void validatePluginIds() {
        for (PluginConfig pluginConfig : config.getPlugins()) {
            String pluginId = pluginConfig.getPluginId();
            PluginIdEnum.getByCode(pluginId)
                    .orElseThrow(() -> new RuntimeException("无效的插件ID：" + pluginId + "（请在PluginIdEnum中定义）"));
        }
    }

    /**
     * 将PluginConfig转换为插件元数据（给策略类使用）
     */
    public PluginStrategyMetadata getPluginMetadata(String pluginId) {
        PluginConfig config = getPluginById(pluginId);
        return new PluginStrategyMetadata(
                config.getPluginId(),
                config.getGroupId(),
                config.getArtifactId(),
                config.getDefaultVersion(),
                config.getDescription(),
                config.getOrder(),
                config.getDependencies(),
                config.isRequired(),
                config.isEnabled()
        );
    }

}
