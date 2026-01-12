package core.gitee.xudai.service;

import core.gitee.xudai.manager.PluginConfigurator;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * 显示 Maven Central Publish 插件的帮助信息
 */
@Mojo(name = "publish", requiresProject = true, requiresOnline = true,defaultPhase = LifecyclePhase.PACKAGE)
public class PublishMojo extends AbstractMojo {

    /**
     * 自动获取使用此插件的Maven项目信息
     * 关键修改：使用 ${project} 表达式自动注入
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    /**
     * 插件服务类
     */
    private PluginConfigurator pluginConfigurator;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Starting publication to Maven Central...");
        Model model = project.getModel();
        Build build = model.getBuild();
        if (build == null) {
            build = new Build();
            model.setBuild(build);
        }

        PluginManagement pluginManagement = build.getPluginManagement();
        if (pluginManagement == null) {
            pluginManagement = new PluginManagement();
            build.setPluginManagement(pluginManagement);
        }
        // 初始化插件配置器
        pluginConfigurator = new PluginConfigurator();
        pluginConfigurator.configurePlexusUtils(pluginManagement);
        pluginConfigurator.configureDeployPlugin(pluginManagement);
        // 这里实现实际的发布逻辑
        getLog().info("Publication completed successfully");
    }
}
