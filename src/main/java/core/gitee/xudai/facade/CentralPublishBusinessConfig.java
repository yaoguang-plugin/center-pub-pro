package core.gitee.xudai.facade;

import lombok.Data;
import org.apache.maven.project.MavenProject;

/**
 * 中央仓库发布业务配置（外部调用时传入）
 */
@Data
public class CentralPublishBusinessConfig {
    // Maven项目实例
    private MavenProject project;
    // GPG配置
    private String gpgKey;
    private String gpgPassphrase;
    // 插件版本配置（用户可自定义，覆盖默认版本）
    private PluginVersionConfig pluginVersions = new PluginVersionConfig();
}
