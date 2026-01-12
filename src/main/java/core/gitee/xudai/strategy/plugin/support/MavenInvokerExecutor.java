package core.gitee.xudai.strategy.plugin.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.shared.invoker.*;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.io.File;
import java.util.Collections;
import java.util.Properties;

/**
 * Maven命令执行工具类，负责通过 Invoker 调用插件目标（独立于抽象类的功能组件）
 * @author daixu
 */
@Slf4j
public class MavenInvokerExecutor {

    /**
     * 通过 Maven Invoker 执行插件目标
     * @param basicPlugin 插件基础信息
     * @param strategyName 调用当前插件的策略名称（用于异常定位）
     * @throws MojoExecutionException 执行失败时抛出异常
     * @author daixu
     */
    public static void execute(BasicPlugin basicPlugin, String strategyName) throws MojoExecutionException {
        try {
            File pomFile = basicPlugin.getFile();
            if (pomFile == null || !pomFile.exists()) {
                throw new MojoExecutionException("POM文件不存在: " + (pomFile != null ? pomFile.getPath() : "null"));
            }

            // 构建调用请求
            InvocationRequest request = new DefaultInvocationRequest();
            request.setPomFile(pomFile);

            // 构建目标字符串（groupId:artifactId:version:goal）
            String goalString = String.format("%s:%s:%s:%s",
                    basicPlugin.getGroupId(),
                    basicPlugin.getArtifactId(),
                    basicPlugin.getVersion(),
                    basicPlugin.getGoal());
            request.setGoals(Collections.singletonList(goalString));

            // 添加配置参数到系统属性
            Xpp3Dom configuration = basicPlugin.getConfiguration();
            if (configuration != null) {
                Properties props = new Properties();
                addConfigurationToProperties(configuration, props, "");
                request.setProperties(props);
            }

            // 执行插件
            Invoker invoker = new DefaultInvoker();
            InvocationResult result = invoker.execute(request);

            if (result.getExitCode() != 0) {
                throw new MojoExecutionException("插件执行失败（退出码：" + result.getExitCode() + "）");
            }

        } catch (Exception e) {
            // 包装异常信息，添加策略名称便于定位
            throw new MojoExecutionException("[" + strategyName + "] Maven插件调用失败: " + basicPlugin.getArtifactId(), e);
        }
    }

    /**
     * 将 Xpp3Dom 配置转换为 Properties（用于传递给Maven Invoker）
     * @param config Xpp3Dom配置节点
     * @param props 目标Properties对象
     * @param prefix 属性名前缀（用于嵌套配置）
     * @author daixu
     */
    private static void addConfigurationToProperties(Xpp3Dom config, Properties props, String prefix) {
        for (Xpp3Dom child : config.getChildren()) {
            String key = prefix.isEmpty() ? child.getName() : prefix + "." + child.getName();
            if (child.getChildCount() > 0) {
                addConfigurationToProperties(child, props, key);
            } else {
                props.setProperty(key, child.getValue());
            }
        }
    }

}
