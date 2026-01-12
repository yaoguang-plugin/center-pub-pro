package core.gitee.xudai.container;

import com.fasterxml.jackson.annotation.JsonProperty;
import core.gitee.xudai.config.loader.b.config.PluginConfig;
import lombok.Data;

import java.util.List;

/**
 * 插件配置容器（对应plugin-config.yaml根节点）
 * @author daixu
 */
@Data
public class PluginConfigContainer {

    @JsonProperty("plugins")
    private List<PluginConfig> plugins;

}
