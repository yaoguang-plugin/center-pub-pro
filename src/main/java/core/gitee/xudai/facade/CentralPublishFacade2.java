package core.gitee.xudai.facade;

import lombok.extern.slf4j.Slf4j;

/**
 * 中央仓库发布门面类（枚举单例）
 * 作用：外部统一调用入口，封装内部所有复杂逻辑（配置加载、策略创建、责任链执行等）
 */
@Slf4j
public class CentralPublishFacade2 {
    // 单例实例
    INSTANCE;

    private static final Logger log = LoggerFactory.getLogger(CentralPublishFacade.class);

    // 依赖的内部组件（单例，初始化时注入）
    private final PluginConfigLoader configLoader;
    private final PluginStrategyFactory strategyFactory;
    private final PluginEventManager eventManager;

    // 构造方法（初始化内部组件）
    CentralPublishFacade() {
        this.configLoader = PluginConfigLoader.INSTANCE;
        this.strategyFactory = PluginStrategyFactory.INSTANCE;
        this.eventManager = PluginEventManager.INSTANCE;
        // 注册默认监听器（如日志监听器）
        registerDefaultListeners();
    }

    /**
     * 外部核心调用方法：一键完成中央仓库发布配置
     * @param config 业务配置（如GPG密钥、仓库地址等）
     */
    public void configureCentralPublish(CentralPublishBusinessConfig config) {
        try {
            log.info("开始执行中央仓库发布配置...");
            // 1. 全局配置校验
            validateBusinessConfig(config);
            // 2. 补充插件默认版本（替换环境变量）
            supplementPluginDefaultVersions(config);
            // 3. 获取所有启用的插件策略
            List<PluginStrategy> enabledStrategies = getEnabledStrategies();
            // 4. 构建责任链（处理依赖顺序）
            PluginConfigurationChain configChain = new PluginConfigurationChain(enabledStrategies, eventManager);
            // 5. 执行配置流程
            configChain.execute(config.getProject().getPluginManagement(), config);
            // 6. 配置完成通知
            eventManager.publishEvent(new PluginConfigEvent("global", PluginConfigEvent.EventType.ALL_COMPLETED, "中央仓库发布配置全部完成！"));
            log.info("中央仓库发布配置执行成功！");
        } catch (Exception e) {
            eventManager.publishEvent(new PluginConfigEvent("global", PluginConfigEvent.EventType.ALL_FAILED, "配置失败：" + e.getMessage()));
            log.error("中央仓库发布配置执行失败！", e);
            throw new RuntimeException("配置失败", e);
        }
    }

    /**
     * 校验业务配置（如项目、GPG密钥等）
     */
    private void validateBusinessConfig(CentralPublishBusinessConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("业务配置不能为空！");
        }
        if (config.getProject() == null) {
            throw new IllegalArgumentException("Maven项目实例不能为空！");
        }
        if (configLoader.getPluginConfig("gpg").isRequired()
                && (config.getGpgKey() == null || config.getGpgPassphrase() == null)) {
            throw new IllegalArgumentException("GPG密钥和密码为必需配置！");
        }
    }

    /**
     * 补充插件默认版本（替换环境变量）
     */
    private void supplementPluginDefaultVersions(CentralPublishBusinessConfig config) {
        PluginConfigLoader.PluginConfig gpgConfig = configLoader.getPluginConfig("gpg");
        // 替换环境变量（如${MAVEN_GPG_PLUGIN_VERSION}）
        String gpgVersion = resolveEnvVariable(gpgConfig.getDefaultVersion());
        config.getPluginVersions().setGpgVersion(gpgVersion);

        // 其他插件版本补充（同理）
        PluginConfigLoader.PluginConfig sourceConfig = configLoader.getPluginConfig("source");
        config.getPluginVersions().setSourceVersion(resolveEnvVariable(sourceConfig.getDefaultVersion()));
    }

    /**
     * 解析环境变量（格式：${变量名:默认值}）
     */
    private String resolveEnvVariable(String value) {
        if (value == null || !value.startsWith("${") || !value.endsWith("}")) {
            return value;
        }
        // 截取变量名和默认值（如"${VAR:DEFAULT}" -> VAR, DEFAULT）
        String content = value.substring(2, value.length() - 1);
        String[] parts = content.split(":", 2);
        String envName = parts[0];
        String defaultValue = parts.length > 1 ? parts[1] : "";
        // 从环境变量获取，获取不到则用默认值
        String envValue = System.getenv(envName);
        return envValue != null ? envValue : defaultValue;
    }

    /**
     * 获取所有启用的插件策略
     */
    private List<PluginStrategy> getEnabledStrategies() {
        return strategyFactory.getAllStrategies().stream()
                .filter(strategy -> {
                    PluginConfigLoader.PluginMetadata metadata = ((AbstractPluginStrategy) strategy).getMetadata();
                    return metadata.isEnabled();
                })
                .collect(Collectors.toList());
    }

    /**
     * 注册默认监听器（日志输出）
     */
    private void registerDefaultListeners() {
        eventManager.addListener(event -> {
            switch (event.getType()) {
                case START:
                    log.info("插件[{}]开始配置...", event.getPluginId());
                    break;
                case SUCCESS:
                    log.info("插件[{}]配置成功", event.getPluginId());
                    break;
                case SKIPPED:
                    log.warn("插件[{}]被跳过：{}", event.getPluginId(), event.getMsg());
                    break;
                case FAILURE:
                    log.error("插件[{}]配置失败：{}", event.getPluginId(), event.getMsg());
                    break;
                case ALL_COMPLETED:
                    log.info("===== 所有插件配置完成 =====");
                    break;
                case ALL_FAILED:
                    log.error("===== 配置整体失败 =====");
                    break;
            }
        });
    }
}
