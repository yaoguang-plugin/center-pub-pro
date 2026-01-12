package core.gitee.xudai.config.loader.b.config;

import core.gitee.xudai.strategy.plugin.metadata.PluginStrategyMetadata;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 插件配置映射类：对应 YAML 中的单个插件配置
 * @author daixu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PluginConfig extends BaseConfig<PluginStrategyMetadata> {

    @JsonProperty("pluginInfo")
    @NotNull(message = "pluginInfo 不能为空")
    private PluginInfo pluginInfo;

    @Override
    public void extraValidation() {
        if (pluginInfo == null) {
            throw new IllegalArgumentException("pluginInfo 不能为空 (ID: " + this.id + ")");
        }
    }

    /**
     * 插件信息元数据类
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class PluginInfo extends ArtifactInfoConfig {

        private List<String> expandTags =  Collections.emptyList();

        private Map<String, Object> configuration = Map.of();

        private Map<String, Object> executions = Map.of();

        // 父类 ArtifactInfo 的 groupId/artifactId 强制非空
        @Override
        @NotNull(message = "pluginInfo.groupId 不能为空")
        public String getGroupId() {
            return super.getGroupId();
        }

        @Override
        @NotNull(message = "pluginInfo.artifactId 不能为空")
        public String getArtifactId() {
            return super.getArtifactId();
        }
    }

}
