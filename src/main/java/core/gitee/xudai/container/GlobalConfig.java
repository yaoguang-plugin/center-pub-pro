package core.gitee.xudai.container;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 全局插件配置类：对应整个 YAML 文件
 * YAML 根节点映射
 * @author daixu
 */
@Data
@NoArgsConstructor
public class GlobalConfig {

    private PluginConfigContainer pluginConfigContainer;

    private LicenseConfigContainer licenseConfigContainer;

    private DependencyConfigContainer dependencyConfigContainer;

}
