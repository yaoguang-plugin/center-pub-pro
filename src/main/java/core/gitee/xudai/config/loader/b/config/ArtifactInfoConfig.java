package core.gitee.xudai.config.loader.b.config;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共组件信息类，包含 groupId、artifactId、version，供各配置类复用
 * @author daixu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtifactInfoConfig {

    @NotBlank(message = "groupId cannot be null or empty")
    private String groupId;

    @NotBlank(message = "artifactId cannot be null or empty")
    private String artifactId;

    @NotBlank(message = "version cannot be null or empty")
    private String version;
}
