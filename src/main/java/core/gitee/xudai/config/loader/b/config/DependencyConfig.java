package core.gitee.xudai.config.loader.b.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import core.gitee.xudai.config.ValidScope;
import core.gitee.xudai.strategy.dependency.metadata.DependencyStrategyMetadata;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author daixu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DependencyConfig extends BaseConfig<DependencyStrategyMetadata> {

    @Valid
    @JsonProperty("dependencyInfo")
    @NotNull(message = "dependencyInfo cannot be null")
    private DependencyInfo dependencyInfo;

    @Override
    public void extraValidation() {
        if (dependencyInfo == null) {
            throw new IllegalArgumentException("dependencyInfo 不能为空 (ID: " + this.id + ")");
        }
        // 核心字段非空校验
        if (dependencyInfo.getGroupId() == null || dependencyInfo.getGroupId().trim().isEmpty()) {
            throw new IllegalArgumentException("groupId cannot be null or empty for dependency: " + getId());
        }
        if (dependencyInfo.getArtifactId() == null || dependencyInfo.getArtifactId().trim().isEmpty()) {
            throw new IllegalArgumentException("artifactId cannot be null or empty for dependency: " + getId());
        }
    }

    /**
     * 依赖信息元数据类
     */

    @Data
    @NoArgsConstructor // 反序列化必需
    @AllArgsConstructor // 方便测试手动构建
    @EqualsAndHashCode(callSuper = true)
    public static class DependencyInfo extends ArtifactInfoConfig {

        private String type = "jar"; // 默认类型为 jar
        @ValidScope
        private String scope = "compile"; // 默认作用域为 compile
        private Boolean optional = false; // 默认非可选
        private String classifier;
        private List<Map<String, String>> exclusions = Collections.emptyList();
        private String systemPath;
        private String versionRange;

    }

}
