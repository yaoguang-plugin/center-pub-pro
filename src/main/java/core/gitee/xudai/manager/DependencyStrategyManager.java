package core.gitee.xudai.manager;


import core.gitee.xudai.entity.CentralPublishConfig;
import core.gitee.xudai.strategy.dependency.api.DependencyStrategy;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.project.MavenProject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@Component
public class DependencyStrategyManager {

    private final List<DependencyStrategy> strategies;
    private final CentralPublishConfig config;

    public DependencyStrategyManager(List<DependencyStrategy> strategies,
                                     CentralPublishConfig config) {
        this.strategies = strategies != null ? strategies : new ArrayList<>();
        this.config = config;
    }

    /**
     * 获取所有启用的依赖
     */
    public List<Dependency> getEnabledDependencies() {
        return strategies.stream()
                .filter(strategy -> strategy.isEnabled(config))
                .flatMap(strategy -> strategy.getDependencies().stream())
                .collect(Collectors.toList());
    }

    /**
     * 添加依赖到项目
     */
    public void addDependenciesToProject(MavenProject project) {
        List<Dependency> enabledDependencies = getEnabledDependencies();
        List<Dependency> projectDependencies = project.getDependencies();

        for (Dependency newDependency : enabledDependencies) {
            if (!containsDependency(projectDependencies, newDependency)) {
                projectDependencies.add(newDependency);
            }
        }
    }

    private boolean containsDependency(List<Dependency> dependencies, Dependency target) {
        return dependencies.stream().anyMatch(dep ->
                dep.getGroupId().equals(target.getGroupId()) &&
                        dep.getArtifactId().equals(target.getArtifactId()));
    }
}
