package core.gitee.xudai.strategy.dependency.impl.plexus;

import core.gitee.xudai.entity.CentralPublishConfig;
import core.gitee.xudai.strategy.dependency.enums.DependencyEnum;
import core.gitee.xudai.strategy.dependency.support.AbstractDependencyStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * TODO 检测如果子工程有引入，就不使用插件的版本了
 */
/**
 * Plexus Utils依赖配置策略（用于添加Plexus工具类依赖）
 * @author daixu
 */
@Slf4j
public class PlexusUtilsDependencyStrategy extends AbstractDependencyStrategy {

    /**
     * 构造函数
     */
    public PlexusUtilsDependencyStrategy() {
        super(DependencyEnum.PLEXUS_UTILS);
    }

    /**
     * 根据配置决定是否启用（示例：默认不启用，可通过配置开启）
     * @param config 中央仓库发布配置
     * @return 启用返回true，否则返回false
     */
    @Override
    public boolean isEnabled(CentralPublishConfig config) {
        // 实际场景可根据config中的开关决定是否启用
        return false;
    }

    /**
     * 配置Plexus Utils依赖并添加到依赖列表
     * @param config 中央仓库发布配置
     * @throws MojoExecutionException 配置失败时抛出异常
     */
    @Override
    public BasicDependency getBasicDependency(CentralPublishConfig config) throws MojoExecutionException {

        BasicDependency dependency = new BasicDependency();
        dependency.setGroupId(DependencyEnum.PLEXUS_UTILS.getGroupId());
        dependency.setArtifactId(DependencyEnum.PLEXUS_UTILS.getArtifactId());
        dependency.setVersion(DependencyEnum.PLEXUS_UTILS.getVersion());

        return dependency;
    }

    /**
     * 依赖作用域：compile（编译时依赖）
     * @return 作用域字符串
     */
    @Override
    protected String getScope() {
        return DependencyEnum.PLEXUS_UTILS.getScope();
    }

}
