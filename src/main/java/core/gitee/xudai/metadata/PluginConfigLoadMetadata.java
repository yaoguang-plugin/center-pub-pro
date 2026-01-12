package core.gitee.xudai.metadata;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 插件配置元数据（扩展插件专属元数据）
 */
@Data
@EqualsAndHashCode(callSuper = true) // 继承父类的equals和hashCode
public class PluginConfigLoadMetadata extends BaseConfigLoadMetadata {

    // 插件配置专属元数据：配置文件的schema版本（如后续需兼容不同版本的YAML结构）
    private String schemaVersion = "1.0";

}
