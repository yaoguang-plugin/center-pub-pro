package core.gitee.xudai.entity;

import lombok.Data;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import java.util.Map;

/**
 * 中央仓库发布插件配置类
 * @author daixu
 */
@Data
public class CentralPublishConfig {

    /**
     * 项目对象
     */
    private MavenProject project;

    /**
     * GPG签名配置类
     */
    @Parameter(property = "gpg", required = true)
    private GpgSignConfig gpgSignConfig;

    /**
     * 中央仓库发布配置类
     */
    @Parameter(property = "central", required = true)
    private GpgSignConfig centralPubConfig;

    /**
     * 自定义属性配置
     */
    @Parameter
    private Map<String, String> properties;

    /**
     * 跳过插件执行
     */
    @Parameter(property = "skip", defaultValue = "false")
    private boolean skip;

}
