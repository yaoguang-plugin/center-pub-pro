package core.gitee.xudai.strategy.dependency.support;

import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;

/**
 * 依赖检测工具类，判断工程是否已引入指定依赖
 * @author daixu
 */
public class DependencyDetector {

    /**
     * 检测工程中是否已包含指定依赖（通过groupId和artifactId匹配）
     * @param project 当前Maven项目
     * @param groupId 依赖groupId
     * @param artifactId 依赖artifactId
     * @return 已包含返回true，否则返回false
     * @author daixu
     */
    public static boolean hasExistingDependency(MavenProject project, String groupId, String artifactId) {
        if (project.getModel().getDependencies() == null) {
            return false;
        }

        for (Dependency dependency : project.getModel().getDependencies()) {
            if (isSameDependency(dependency, groupId, artifactId)) {
                return true;
            }
        }

        // 检查依赖管理中的配置
        if (project.getModel().getDependencyManagement() != null
                && project.getModel().getDependencyManagement().getDependencies() != null) {
            for (Dependency dependency : project.getModel().getDependencyManagement().getDependencies()) {
                if (isSameDependency(dependency, groupId, artifactId)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断两个依赖是否相同（groupId和artifactId一致）
     * @param dependency 依赖
     * @param groupId 待匹配的依赖 groupId
     * @param artifactId 待匹配的依赖 artifactId
     * @return 相同返回 true，否则返回 false
     */
    private static boolean isSameDependency(Dependency dependency, String groupId, String artifactId) {
        return groupId.equals(dependency.getGroupId()) && artifactId.equals(dependency.getArtifactId());
    }
}
