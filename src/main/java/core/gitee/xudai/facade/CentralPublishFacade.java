package core.gitee.xudai.facade;

import core.gitee.xudai.builder.PluginEventManager;
import core.gitee.xudai.builder.VersionProvider;
import core.gitee.xudai.factory.PluginStrategyFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static javax.sound.midi.ShortMessage.START;
import static jdk.internal.vm.Continuation.PreemptStatus.SUCCESS;
import static org.ietf.jgss.GSSException.FAILURE;

/**
 * 门面模式（核心：统一入口封装）
 */
@Slf4j
public class CentralPublishFacade {

    // 依赖内部子系统组件（通过构造注入，便于测试和扩展）
    private final PluginStrategyFactory strategyFactory;
    private final PluginEventManager eventManager;
    private final VersionProvider versionProvider;

    // 单例或Spring注入（根据项目环境选择）
    public CentralPublishFacade() {
        this.strategyFactory = PluginStrategyFactory.getInstance();
        this.eventManager = new PluginEventManager();
        this.versionProvider = new DefaultVersionProvider();
        // 注册默认监听器（比如日志监听器）
        registerDefaultListeners();
    }

    // 外部唯一调用方法：一键完成中央仓库发布配置
    public void configureCentralPublish(CentralPublishConfig config) throws MojoExecutionException {
        try {
            // 1. 前置校验（统一校验全局配置）
            validateGlobalConfig(config);
            // 2. 补充默认版本（如果用户未配置插件版本）
            supplementDefaultVersions(config);
            // 3. 获取所有策略并排序
            List<PluginStrategy> strategies = strategyFactory.getAllStrategies();
            // 4. 构建责任链并执行配置
            PluginConfigurationChain configurationChain = new PluginConfigurationChain(strategies, eventManager);
            configurationChain.execute(config.getProject().getPluginManagement(), config);
            // 5. 全局事件发布（配置完成）
            eventManager.publishEvent(new PluginConfigurationEvent("global", EventType.ALL_COMPLETED, "中央仓库发布配置全部完成"));
        } catch (Exception e) {
            eventManager.publishEvent(new PluginConfigurationEvent("global", EventType.ALL_FAILED, e.getMessage()));
            throw new MojoExecutionException("中央仓库发布配置失败", e);
        }
    }

    // 私有方法：封装内部细节（外部不可见）
    private void validateGlobalConfig(CentralPublishConfig config) throws MojoExecutionException {
        if (config == null) {
            throw new MojoExecutionException("全局发布配置不能为空");
        }
        if (config.getProject() == null) {
            throw new MojoExecutionException("Maven项目实例不能为空");
        }
    }

    private void supplementDefaultVersions(CentralPublishConfig config) {
        PluginVersions pluginVersions = config.getPluginVersions();
        // 为未配置的插件补充默认版本
        if (StringUtils.isEmpty(pluginVersions.getGpgPlugin())) {
            pluginVersions.setGpgPlugin(versionProvider.getVersion("gpg"));
        }
        if (StringUtils.isEmpty(pluginVersions.getSourcePlugin())) {
            pluginVersions.setSourcePlugin(versionProvider.getVersion("source"));
        }
        // 其他插件版本补充...
    }

    private void registerDefaultListeners() {
        // 注册日志监听器（默认横切逻辑）
        eventManager.addListener(event -> {
            switch (event.getType()) {
                case START:
                    log.info("插件[{}]开始配置", event.getPluginId());
                    break;
                case SUCCESS:
                    log.info("插件[{}]配置成功", event.getPluginId());
                    break;
                case FAILURE:
                    log.error("插件[{}]配置失败：{}", event.getPluginId(), event.getData());
                    break;
                case ALL_COMPLETED:
                    log.info("所有插件配置完成，可执行发布流程");
                    break;
            }
        });
    }

}
