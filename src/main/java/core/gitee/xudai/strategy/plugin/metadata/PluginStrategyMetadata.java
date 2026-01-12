package core.gitee.xudai.strategy.plugin.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import core.gitee.xudai.strategy.core.enums.StrategyType;
import core.gitee.xudai.strategy.core.metadata.BaseMavenCoordinateMetadata;
import core.gitee.xudai.strategy.core.metadata.BaseStrategyMetadata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * YAML子节点映射：对应单个插件配置（如gpg、source插件）
 */
/**
 * 插件元数据：给策略类（如GpgPluginStrategy）使用的简化实体
 * 作用：隔离配置层和业务层，避免策略类依赖复杂的配置实体
 * @author daixu
 */
/**
 * 插件策略元数据实体
 * <p>
 * 作用：作为配置层与业务层的隔离实体，为插件策略类（如GpgPluginStrategy）提供简化的配置信息
 * 特性：元数据不可变，通过建造者模式构建，支持JSON反序列化和反射实例化
 * </p>
 * @author daixu
 */
@Getter
@SuperBuilder(toBuilder = true) // 支持建造者模式及对象复制修改
@NoArgsConstructor(force = true) // 兼容反序列化（如YAML/JSON解析）
public class PluginStrategyMetadata extends BaseStrategyMetadata<PluginStrategyMetadata> {

    @JsonProperty("pluginInfo")
    private final PluginStrategyInfo pluginStrategyInfo;

    /**
     * 实现父类的抽象方法：明确策略类型为"license"
     */
    @Override
    public String getStrategyType() {
        return StrategyType.PLUGIN.getValue();
    }

    // ------------------------------ 内部类：插件特有信息 ------------------------------

    /**
     * 基础信息元数据类
     */
    @Getter
    @AllArgsConstructor
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor(force = true)
    public static class PluginStrategyInfo extends BaseMavenCoordinateMetadata {
        // 扩展标签列表，用于标识插件特性
        private final List<String> expandTags;
        // 插件原始配置（JSON结构）
        private final Map<String, Object> configuration;
        // 插件执行计划配置
        private final Map<String, Object> executions;
        // 插件配置文件路径
        private final File file;
    }

    /** 构造方法：仅初始化，不提供Setter（元数据不可变）*/
//    public PluginStrategyMetadata(String id, String name, String groupId, String artifactId, String version, String description, int order, boolean required, boolean enabled, String executionId, String phase, String goal, Xpp3Dom configuration, Map<String, String> config, File file, Boolean expandTags, List<String> dependencies) {
//        super(id, name, groupId, artifactId, version, description, order, required, enabled);
//        this.executionId = executionId;
//        this.phase = phase;
//        this.goal = goal;
//        this.configuration = configuration;
//        this.config = config;
//        this.file = file;
//        this.expandTags = expandTags;
//        this.dependencies = dependencies;
//    }
}
