package core.gitee.xudai.strategy.plugin.support;

import org.apache.maven.model.Plugin;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.util.Map;

/**
 * 插件配置构建工具类，负责插件对象创建和 XML 配置节点生成（独立于抽象类的功能组件）
 * @author daixu
 */
public class PluginConfigBuilder {

    /**
     * 根据基础插件信息创建 Maven Plugin 对象
     * @param basicPlugin 基础插件信息（groupId、artifactId等）
     * @return 构建好 的Maven Plugin 对象
     * @author daixu
     */
    public static Plugin createPlugin(BasicPlugin basicPlugin) {

        Plugin plugin = new Plugin();
        plugin.setGroupId(basicPlugin.getGroupId());
        plugin.setArtifactId(basicPlugin.getArtifactId());
        plugin.setVersion(basicPlugin.getVersion());

        // 构建执行配置（executions节点）
        Xpp3Dom executions = createExecutions(basicPlugin);
        if (executions != null) {
            plugin.setConfiguration(executions);
        }

        return plugin;
    }

    /**
     * 创建插件的 executions 配置节点
     * @param basicPlugin 插件基础信息
     * @return 构建好的 Xpp3Dom 节点，无执行配置时返回 null
     * @author daixu
     */
    public static Xpp3Dom createExecutions(BasicPlugin basicPlugin) {

        String id = basicPlugin.getExecutionId();
        String phase = basicPlugin.getPhase();
        String goal = basicPlugin.getGoal();
        Map<String, String> config = basicPlugin.getConfig();

        // 无执行配置时返回 null
        if (StringUtils.isBlank(id) && StringUtils.isBlank(phase) && StringUtils.isBlank(goal) && (config == null || config.isEmpty())) {
            return null;
        }

        Xpp3Dom executions = new Xpp3Dom("executions");
        Xpp3Dom execution = new Xpp3Dom("execution");

        // 添加执行 ID
        if (StringUtils.isNotBlank(id)) {
            Xpp3Dom executionId = new Xpp3Dom("id");
            executionId.setValue(id);
            execution.addChild(executionId);
        }

        // 添加执行阶段
        if (StringUtils.isNotBlank(phase)) {
            Xpp3Dom phaseDom = new Xpp3Dom("phase");
            phaseDom.setValue(phase);
            execution.addChild(phaseDom);
        }

        // 添加执行目标
        if (StringUtils.isNotBlank(goal)) {
            Xpp3Dom goals = new Xpp3Dom("goals");
            Xpp3Dom goalDom = new Xpp3Dom("goal");
            goalDom.setValue(goal);
            goals.addChild(goalDom);
            execution.addChild(goals);
        }

        // 添加配置参数
        if (config != null && !config.isEmpty()) {
            Xpp3Dom configuration = createConfiguration(config);
            execution.addChild(configuration);
            basicPlugin.setConfiguration(configuration);
        }

        executions.addChild(execution);

        return executions;
    }

    /**
     * 将键值对配置转换为 Xpp3Dom 节点
     * @param config 配置键值对
     * @return 转换后的 Xpp3Dom节点，空配置时返回 null
     * @author daixu
     */
    public static Xpp3Dom createConfiguration(Map<String, String> config) {

        if (config == null || config.isEmpty()) {
            return null;
        }

        Xpp3Dom configuration = new Xpp3Dom("configuration");
        for (Map.Entry<String, String> entry : config.entrySet()) {
            Xpp3Dom child = new Xpp3Dom(entry.getKey());
            child.setValue(entry.getValue());
            configuration.addChild(child);
        }

        return configuration;
    }


}
