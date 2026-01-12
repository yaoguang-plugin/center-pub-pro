package core.gitee.xudai.strategy.plugin.support;

import core.gitee.xudai.builder.PluginEventManager;
import core.gitee.xudai.entity.CentralPublishConfig;
import core.gitee.xudai.strategy.plugin.metadata.PluginStrategyMetadata;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractPluginStrategy2 {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final PluginStrategyMetadata metadata;
    protected PluginEventManager eventManager;

    // 注入事件管理器（由门面类或责任链传递）
    public AbstractPluginStrategy(PluginStrategyMetadata metadata) {
        this.metadata = metadata;
    }

    // 注入事件管理器（用于发布步骤事件）
    public void setEventManager(PluginEventManager eventManager) {
        this.eventManager = eventManager;
    }

    // 【模板方法】定义固定流程（不可修改，确保所有子类遵循统一规范）
    @Override
    public final void configure(PluginManagement pluginManagement, CentralPublishConfig config) throws MojoExecutionException {
        try {
            // 步骤1：前置检查（固定流程）
            preCheck(config);
            // 步骤2：发布「开始」事件（固定流程）
            publishStepEvent(EventType.START, null);
            // 步骤3：校验配置（可变流程：子类必须实现）
            validateConfig(config);
            // 步骤4：检查是否已存在插件（固定流程）
            if (hasExistingPlugin(config)) {
                log.info("工程中已存在插件[{}]，跳过配置", getPluginId());
                publishStepEvent(EventType.SKIPPED, "工程已存在该插件");
                return;
            }
            // 步骤5：创建插件实例（可变流程：子类必须实现）
            Plugin plugin = createPlugin(config);
            // 步骤6：配置插件详情（可变流程：子类必须实现）
            configurePluginDetails(plugin, config);
            // 步骤7：自定义扩展（可变流程：子类可选重写，钩子方法）
            customExtension(plugin, config);
            // 步骤8：添加到PluginManagement（固定流程）
            pluginManagement.addPlugin(plugin);
            log.info("插件[{}]配置完成", getPluginId());
            // 步骤9：发布「成功」事件（固定流程）
            publishStepEvent(EventType.SUCCESS, null);
            // 步骤10：后置处理（可变流程：子类可选重写，钩子方法）
            postProcess(plugin, config);
        } catch (SkipExecutionException e) {
            publishStepEvent(EventType.SKIPPED, e.getMessage());
            throw e;
        } catch (MojoExecutionException e) {
            publishStepEvent(EventType.FAILURE, e.getMessage());
            throw e;
        }
    }

    // -------------------------- 固定流程方法（子类不可修改）--------------------------
    /**
     * 前置检查：固定校验启用状态和必填标识
     */
    private void preCheck(CentralPublishConfig config) throws MojoExecutionException {
        if (!isEnabled(config)) {
            throw new SkipExecutionException("插件[{}]已禁用", getPluginId());
        }
        if (isRequired(config) && !config.isRequiredPluginsEnabled()) {
            throw new MojoExecutionException("插件[{}]为必需插件，无法禁用", getPluginId());
        }
    }

    /**
     * 检查工程是否已存在该插件（固定逻辑）
     */
    private boolean hasExistingPlugin(CentralPublishConfig config) {
        return PluginDetector.hasExistingPlugin(
                config.getProject(),
                metadata.getGroupId(),
                metadata.getArtifactId()
        );
    }

    /**
     * 发布步骤事件（固定逻辑，与事件管理器协同）
     */
    private void publishStepEvent(EventType type, String data) {
        if (eventManager != null) {
            eventManager.publishEvent(new PluginConfigurationEvent(getPluginId(), type, data));
        }
    }

    // -------------------------- 抽象方法（子类必须实现：核心可变逻辑）--------------------------
    /**
     * 校验配置：子类必须实现（确保关键配置不缺失）
     */
    protected abstract void validateConfig(CentralPublishConfig config) throws MojoExecutionException;

    /**
     * 创建插件实例：子类必须实现（插件GroupId/ArtifactId/Version可能不同）
     */
    protected abstract Plugin createPlugin(CentralPublishConfig config) throws MojoExecutionException;

    /**
     * 配置插件详情：子类必须实现（执行目标、配置参数等）
     */
    protected abstract void configurePluginDetails(Plugin plugin, CentralPublishConfig config) throws MojoExecutionException;

    // -------------------------- 钩子方法（子类可选重写：扩展逻辑）--------------------------
    /**
     * 自定义扩展：子类可选重写（比如额外添加依赖、修改执行顺序等）
     */
    protected void customExtension(Plugin plugin, CentralPublishConfig config) throws MojoExecutionException {
        // 默认无实现，留给子类扩展
    }

    /**
     * 后置处理：子类可选重写（比如记录自定义日志、触发其他操作）
     */
    protected void postProcess(Plugin plugin, CentralPublishConfig config) throws MojoExecutionException {
        // 默认无实现，留给子类扩展
    }

    // -------------------------- 继承自PluginStrategy的方法（默认实现）--------------------------
    @Override
    public String getPluginId() {
        return metadata.getPluginId();
    }

    @Override
    public String getStrategyName() {
        return metadata.getDescription();
    }

    @Override
    public boolean isEnabled(CentralPublishConfig config) {
        return true; // 默认启用
    }

    @Override
    public boolean isRequired(CentralPublishConfig config) {
        return false; // 默认非必需
    }

    @Override
    public int getOrder() {
        return metadata.getOrder(); // 从元数据获取执行顺序
    }
}
