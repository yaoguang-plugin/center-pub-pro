package core.gitee.xudai.strategy.core.api;

import core.gitee.xudai.entity.CentralPublishConfig;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Maven策略顶层接口，定义所有策略的通用行为
 * @author daixu
 */
public interface MavenStrategy {

    /**
     * 获取策略名称（用于日志和异常定位）
     * @return 策略名称
     * @author daixu
     */
    String getStrategyName();

    /**
     * 执行策略
     * @author daixu
     */
    void execute();

    /**
     * 配置策略核心逻辑
     * @param config 中央仓库发布配置
     * @throws MojoExecutionException 配置过程中发生的异常
     * @author daixu
     */
    void configure(CentralPublishConfig config) throws MojoExecutionException;

    /**
     * 判断当前策略是否启用（根据配置动态决定）
     * @param config 发布配置（包含开关、环境等信息）
     * @return 启用返回 true，否则 false
     */
    boolean isEnabled(CentralPublishConfig config);

    /**
     * 判断当前策略是否为必需策略（禁用会导致流程失败）
     * @param config 发布配置（可根据环境/项目类型调整必需性）
     * @return 必需返回true，否则false
     */
    boolean isRequired(CentralPublishConfig config);

    /**
     * 获取策略执行顺序（数值越小越先执行）
     * @return 执行顺序值
     * @author daixu
     */
    default int getOrder() {
        return 0;
    }

}
