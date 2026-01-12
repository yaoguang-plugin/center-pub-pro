package core.gitee.xudai.strategy.plugin.support;

import org.apache.maven.model.Build;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.project.MavenProject;

import java.util.List;

/**
 * 插件检测工具类，判断工程是否已引入指定插件
 * @author daixu
 */
public class PluginDetector {

    /**
     * 检测工程中是否已包含指定插件（通过groupId和artifactId匹配）
     * @param project 当前Maven项目
     * @param groupId 插件groupId
     * @param artifactId 插件artifactId
     * @return 已包含返回true，否则返回false
     * @author daixu
     */
    public static boolean hasExistingPlugin(MavenProject project, String groupId, String artifactId) {

        // ✅ 1.检查插件管理配置
        PluginManagement pluginManagement = project.getPluginManagement();
        if (pluginManagement != null && pluginManagement.getPlugins() != null) {
            for (Plugin plugin : pluginManagement.getPlugins()) {
                if (isSamePlugin(plugin, groupId, artifactId)) {
                    return true;
                }
            }
        }

        // ✅ 2.检查直接插件配置
        Build build = project.getBuild();
        if (build != null) {
            List<Plugin> plugins = build.getPlugins();
            if (plugins != null && !plugins.isEmpty()) {
                for (Plugin plugin : plugins) {
                    if (isSamePlugin(plugin, groupId, artifactId)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * 判断两个插件是否相同（groupId和artifactId一致）
     * @param plugin 待检测的插件
     * @param groupId 待检测的插件的 groupId
     * @param artifactId 待检测的插件的 artifactId
     * @return 相同返回 true，否则返回 false
     * @author daixu
     */
    private static boolean isSamePlugin(Plugin plugin, String groupId, String artifactId) {
        return groupId.equals(plugin.getGroupId()) && artifactId.equals(plugin.getArtifactId());
    }

}
