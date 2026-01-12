package core.gitee.xudai.strategy.plugin.support;

import core.gitee.xudai.strategy.plugin.api.PluginStrategy;
import core.gitee.xudai.entity.CentralPublishConfig;
import core.gitee.xudai.strategy.plugin.enums.PluginEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.Map;

/**
 * 插件策略抽象基类，封装插件配置的通用逻辑
 * 模板方法模式：定义插件配置的固定流程模板，子类实现可变步骤
 * @author daixu
 */
@Slf4j
public abstract class AbstractPluginStrategy implements PluginStrategy {

    /**
     * 插件枚举
     */
    private final PluginEnum pluginEnum;

    protected final PluginConfig metadata; // 从配置加载器获取的插件元数据

    protected AbstractPluginStrategy(PluginConfig metadata) {
        this.metadata = metadata;
    }

    /**
     * 构造函数
     * @param pluginEnum 插件枚举
     * @author daixu
     */
    public AbstractPluginStrategy(PluginEnum pluginEnum) {
        this.pluginEnum = pluginEnum;
    }

    /**
     * 获取插件名称（通常为 artifactId ）
     * @return artifactId
     * @author daixu
     */
    protected String getPluginName() {
        return pluginEnum.getArtifactId();
    }

    /**
     * 策略名称（用于日志和异常定位）
     * @return 策略名称
     * @author daixu
     */
    @Override
    public String getStrategyName() {
        return pluginEnum.getDescription();
    };

    /**
     * 默认启用所有插件策略（子类可重写为“根据配置决定”）
     * @param config 中央仓库发布配置
     * @return 启用返回 true，否则 false
     */
    @Override
    public boolean isEnabled(CentralPublishConfig config) {
        log.debug("[{}] 使用默认启用策略", getStrategyName());
        return true;
    }

    /**
     * 默认非必需策略（子类可重写为“必需”）
     * @param config 中央仓库发布配置
     * @return 必需返回 true，否则 false
     * @author daixu
     */
    @Override
    public boolean isRequired(CentralPublishConfig config) {
        return false; // 默认非必需
    }

    /**
     * 执行顺序（子类可重写）（最后执行，确保所有前置插件完成）
     * @return 执行顺序值
     */
    @Override
    public int getOrder() {
        return pluginEnum.getOrder();
    }

    /**
     * 通用插件配置逻辑:
     * 创建插件 → 添加到 PluginManagement → 执行插件
     * 具体插件信息由子类提供（通过getBasicPlugin方法）
     * @param config 中央仓库发布配置
     * @throws MojoExecutionException 配置过程中发生的异常
     * @author daixu
     */
    @Override
    public void configurePlugin(CentralPublishConfig config) throws MojoExecutionException {

        PluginManagement pluginManagement = config.getProject().getPluginManagement();

        // ✅ 1.获取子类的插件基础信息
        BasicPlugin basicPlugin = getBasicPlugin(config);
        if (basicPlugin == null) {
            throw wrapException("插件基础信息不能为空");
        }

        // ✅ 2.检测工程中是否已包含该插件
        boolean hasExistingPlugin = PluginDetector.hasExistingPlugin(
                config.getProject(),
                basicPlugin.getGroupId(),
                basicPlugin.getArtifactId()
        );

        if (hasExistingPlugin) {
            log.info("[{}] 工程中已包含该插件，将使用工程中的配置，跳过插件策略", getPluginName());
            return;
        }

        // ✅ 3. 调用工具类创建插件对象
        Plugin plugin = PluginConfigBuilder.createPlugin(basicPlugin);

        // ✅ 4.标记为扩展插件
        if (basicPlugin.getExpandTags() != null && basicPlugin.getExpandTags()) {
            plugin.setExtensions(true);
        }

        // ✅ 5. 添加插件到 PluginManagement
        pluginManagement.addPlugin(plugin);
        log.info("[{}] 已添加插件到 PluginManagement: {}", getPluginName(), basicPlugin.getArtifactId());

        // ✅ 6. 调用工具类执行插件
        MavenInvokerExecutor.execute(basicPlugin, getPluginName());
        log.info("[{}] 插件执行完成: {}", getPluginName(), basicPlugin.getArtifactId());
    }

    /**
     * 抽象方法：由子类提供具体的插件基础信息（强制子类实现差异化逻辑）
     */
    protected BasicPlugin getBasicPlugin(CentralPublishConfig config) throws MojoExecutionException {
        // ✅ 1.创建插件基础信息
        BasicPlugin plugin = new BasicPlugin();
        plugin.setGroupId(pluginEnum.getGroupId());
        plugin.setArtifactId(pluginEnum.getArtifactId());
        plugin.setVersion(pluginEnum.getVersion());
        plugin.setFile(config.getProject().getFile());
        setBasicPluginExpand(plugin,config);
        // ✅ 2.配置
        Map<String, String> configuration = getConfiguration(config);
        if (configuration != null && !configuration.isEmpty()) {
            plugin.setConfig(configuration);
        }
        return plugin;
    }

    protected Map<String, String> getConfiguration(CentralPublishConfig config) throws MojoExecutionException {
        return null;
    }

    protected void setBasicPluginExpand(BasicPlugin basicPlugin,CentralPublishConfig config) {
        // 空实现，留给子类扩展（原无参方法可删除或重载）
    }

    /**
     * 统一异常包装：添加策略名称前缀，便于定位问题
     */
    protected MojoExecutionException wrapException(String message) {
        return new MojoExecutionException("[" + getPluginName() + "] " + message);
    }

    /**
     * 统一异常包装：添加策略名称前缀，包含原始异常
     */
    protected MojoExecutionException wrapException(String message, Throwable cause) {
        return new MojoExecutionException("[" + getPluginName() + "] " + message, cause);
    }

}
