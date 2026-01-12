package core.gitee.xudai.strategy.dependency.support;

import org.apache.maven.model.Dependency;

/**
 * 依赖配置构建器
 * @author daixu
 */
public class DependencyConfigBuilder {

    /**
     * 创建依赖
     * @param basic 依赖信息
     * @param scope 作用域
     * @return Dependency
     * @author daixu
     */
    public static Dependency createDependency(BasicDependency basic, String scope) {
        Dependency dependency = new Dependency();
        dependency.setGroupId(basic.getGroupId());
        dependency.setArtifactId(basic.getArtifactId());
        dependency.setVersion(basic.getVersion());
        dependency.setScope(scope);
        return dependency;
    }

}
