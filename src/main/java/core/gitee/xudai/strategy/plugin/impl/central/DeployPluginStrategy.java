package core.gitee.xudai.strategy.plugin.impl.central;

import core.gitee.xudai.entity.CentralPublishConfig;
import core.gitee.xudai.strategy.plugin.enums.PluginEnum;
import core.gitee.xudai.strategy.plugin.support.AbstractPluginStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.HashMap;
import java.util.Map;

/**
 * 部署插件策略（用于将构件部署到中央仓库）
 * 特定作用：覆盖默认的发布插件，防止默认插件去找 <distributionManagement> 标签下的发布路径，配置跳过发布插件
 *     <distributionManagement>
 *
 *         <!--SNAPSHOTS：快照版本 -->
 *         <snapshotRepository>
 *             <id>5c03dada-e993-4d92-98da-a2850cd54ba4</id>
 *             <name>Sonatype Nexus Snapshots</name>
 *             <url>https://central.sonatype.com/content/repositories/snapshots</url>
 *         </snapshotRepository>
 *
 *         <!--RELEASES：稳定版本 -->
 *         <repository>
 *             <id>5c03dada-e993-4d92-98da-a2850cd54ba4</id>
 *             <name>Nexus Release Repository</name>
 *             <url>https://central.sonatype.com/service/local/staging/deploy/maven2</url>
 *         </repository>
 *
 *     </distributionManagement>
 * @author daixu
 */
@Slf4j
public class DeployPluginStrategy extends AbstractPluginStrategy  {

    /**
     * 构造函数
     */
    public DeployPluginStrategy() {
        super(PluginEnum.DEPLOY);
    }

    @Override
    protected Map<String, String> getConfiguration(CentralPublishConfig config) throws MojoExecutionException {
        // ✅ 2.设置跳过默认发布插件
        Map<String,String> configuration = new HashMap<>();
        configuration.put("skip", "true");
        return configuration;
    }

}
