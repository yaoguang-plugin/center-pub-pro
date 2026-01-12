package core.gitee.xudai.container;

import core.gitee.xudai.config.loader.b.loader.PluginConfigLoader;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * 依赖配置容器（对应dependency-config.yaml根节点）
 * @author daixu
 */
@Data
public class DependencyConfigContainer {

    @JsonProperty("dependency")
    private List<PluginConfigLoader.DependencyConfig> dependencies;

}
