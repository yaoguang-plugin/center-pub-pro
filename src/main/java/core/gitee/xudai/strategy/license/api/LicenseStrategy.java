package core.gitee.xudai.strategy.license.api;

import core.gitee.xudai.entity.CentralPublishConfig;
import core.gitee.xudai.strategy.core.api.MavenStrategy;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * 许可证策略接口
 * @author daixu
 */
public interface LicenseStrategy extends MavenStrategy {

    /**
     * 获取许可证名称
     */
    String getLicenseName();

    /**
     * 获取许可证 URL
     */
    String getLicenseUrl();

    /**
     * 获取分发方式
     */
    String getDistribution();

    /**
     * 配置许可证
     */
    void configureLicense(CentralPublishConfig config) throws MojoExecutionException;

    @Override
    default void configure(CentralPublishConfig config) throws MojoExecutionException {
        configureLicense(config);
    }

}
