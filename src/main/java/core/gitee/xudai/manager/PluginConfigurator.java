package core.gitee.xudai.manager;

import core.gitee.xudai.entity.CentralPublishConfig;
import org.apache.maven.model.*;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.shared.invoker.*;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Properties;

/**
 * 插件配置器 - 负责具体的 POM 配置逻辑
 * @author daixu
 */
public class PluginConfigurator {

    /**
     * 插件配置
     */
    private final CentralPublishConfig config;

    /**
     * 插件版本管理器
     */
    private final VersionManager versionManager;

    /**
     * 输出日志
     * 使用 SLF4J Logger 替代弃用的 getLog()
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public PluginConfigurator() {
        this.config = new CentralPublishConfig();
        this.versionManager = new VersionManager();
    }

    public PluginConfigurator(CentralPublishConfig config) {
        this.config = config;
        this.versionManager = new VersionManager();
        // 使用版本管理器初始化插件版本
        initializePluginVersions();
        logger.info("版本信息: " + versionManager.getVersionSummary());
    }

    /**
     * 使用版本管理器初始化插件版本配置
     * @author daixu
     */
    private void initializePluginVersions() {

        if (config.getPluginVersions() == null) {
            config.setPluginVersions(new PluginVersions());
        }

        PluginVersions versions = config.getPluginVersions();

        // 从版本管理器获取版本号，而不是硬编码
        versions.setGpgPlugin(versionManager.getGpgPluginVersion());
        versions.setSourcePlugin(versionManager.getSourcePluginVersion());
        versions.setJavadocPlugin(versionManager.getJavadocPluginVersion());
        versions.setCentralPublishingPlugin(versionManager.getCentralPublishingPluginVersion());

        config.setPluginVersions(versions);
    }

    /**
     * 配置许可证
     * @author daixu
     * @param model
     */
    public void configureLicense(Model model) {
        License license = new License();
        license.setName("The Apache Software License, Version 2.0");
        license.setUrl("https://www.apache.org/licenses/LICENSE-2.0.txt");
        license.setDistribution("repo");
        model.addLicense(license);
        logger.debug("已配置 Apache 2.0 许可证");
    }

    public void configureAllPlugins(PluginManagement pluginManagement) {

        try {
            // 配置
            configurePlexusUtils(pluginManagement);
//            configureDeployPlugin(pluginManagement);
            // 配置 GPG 插件
            configureGpgPlugin(pluginManagement);
            // 配置 源码 插件
            configureSourcePlugin(pluginManagement);
            // 配置 文档 插件
            configureJavadocPlugin(pluginManagement);
            // 配置 中央发布 插件
            configureCentralPublishingPlugin(pluginManagement);
        } catch (MojoExecutionException e) {
            throw new RuntimeException(e);
        }
        // 输出日志
        logger.info("已配置所有必要的插件");
    }

    public void configureDeployPlugin(PluginManagement pluginManagement) throws MojoExecutionException {
        Plugin plugin = new Plugin();
        plugin.setGroupId("org.apache.maven.plugins");
        plugin.setArtifactId("maven-deploy-plugin");
        plugin.setVersion("3.1.0");
//        plugin.setExtensions(true);

        Xpp3Dom configuration = createDeployPluginConfiguration();
        plugin.setConfiguration(configuration);

        pluginManagement.addPlugin(plugin);
        executeMavenPluginWithInvoker(
                "org.apache.maven.plugins",
                "maven-deploy-plugin",
                "3.1.0",
                "deploy",
                configuration);
    }

    public void configurePlexusUtils(PluginManagement pluginManagement) {
        Plugin plugin = new Plugin();
        plugin.setGroupId("org.codehaus.plexus");
        plugin.setArtifactId("plexus-utils");
        plugin.setVersion("3.4.2");
        pluginManagement.addPlugin(plugin);
    }

    private void configureCentralPublishingPlugin(PluginManagement pluginManagement) throws MojoExecutionException {
        Plugin plugin = new Plugin();
        plugin.setGroupId("org.sonatype.central");
        plugin.setArtifactId("central-publishing-maven-plugin");
        plugin.setVersion(config.getPluginVersions().getCentralPublishingPlugin());
        plugin.setExtensions(true);

        Xpp3Dom configuration = createCentralPublishingConfiguration();
        plugin.setConfiguration(configuration);

        pluginManagement.addPlugin(plugin);

        executeMavenPluginWithInvoker(
                "org.sonatype.central",
                "central-publishing-maven-plugin",
                config.getPluginVersions().getCentralPublishingPlugin(),
                "publish",
                configuration);
    }

    private Xpp3Dom createCentralPublishingConfiguration() {
        Xpp3Dom configuration = new Xpp3Dom("configuration");

        Xpp3Dom publishingServerId = new Xpp3Dom("publishingServerId");
        publishingServerId.setValue(config.getPublishingServerId());
        configuration.addChild(publishingServerId);

        Xpp3Dom autoPublish = new Xpp3Dom("autoPublish");
//        autoPublish.setValue("${auto-releases-enabled}");
        autoPublish.setValue("false");
        configuration.addChild(autoPublish);

        Xpp3Dom waitUntil = new Xpp3Dom("waitUntil");
        waitUntil.setValue("validated");
        configuration.addChild(waitUntil);

        return configuration;
    }

    private Xpp3Dom createDeployPluginConfiguration() {
        Xpp3Dom configuration = new Xpp3Dom("configuration");

        Xpp3Dom skip = new Xpp3Dom("skip");
        skip.setValue("true");
        configuration.addChild(skip);

        return configuration;
    }

    private void configureGpgPlugin(PluginManagement pluginManagement) throws MojoExecutionException {
        Plugin plugin = new Plugin();
        plugin.setGroupId("org.apache.maven.plugins");
        plugin.setArtifactId("maven-gpg-plugin");
        plugin.setVersion(config.getPluginVersions().getGpgPlugin());

        Xpp3Dom executions = new Xpp3Dom("executions");
        Xpp3Dom execution = createGpgExecution();
        executions.addChild(execution);

        plugin.setConfiguration(executions);
        pluginManagement.addPlugin(plugin);

        executeMavenPluginWithInvoker(
                "org.apache.maven.plugins",
                "maven-gpg-plugin",
                config.getPluginVersions().getGpgPlugin(),
                "sign",
                executions);
    }

    private Xpp3Dom createGpgExecution() {
        Xpp3Dom execution = new Xpp3Dom("execution");

        Xpp3Dom id = new Xpp3Dom("id");
        id.setValue("sign-artifacts");
        execution.addChild(id);

        Xpp3Dom phase = new Xpp3Dom("phase");
        phase.setValue("verify");
        execution.addChild(phase);

        Xpp3Dom goals = new Xpp3Dom("goals");
        Xpp3Dom goal = new Xpp3Dom("goal");
        goal.setValue("sign");
        goals.addChild(goal);
        execution.addChild(goals);

        Xpp3Dom configuration = new Xpp3Dom("configuration");
        if (config.getGpgKeyname() != null) {
            Xpp3Dom keyname = new Xpp3Dom("keyname");
            keyname.setValue("${gpg-keyname}");
            configuration.addChild(keyname);
        }

        if (config.getGpgPassphrase() != null) {
            Xpp3Dom passphrase = new Xpp3Dom("passphrase");
            passphrase.setValue("${gpg-passphrase}");
            configuration.addChild(passphrase);
        }
        execution.addChild(configuration);
        return execution;
    }

    private void configureSourcePlugin(PluginManagement pluginManagement) throws MojoExecutionException {
        Plugin plugin = createBasicPlugin(
                "org.apache.maven.plugins",
                "maven-source-plugin",
                config.getPluginVersions().getSourcePlugin(),
                "attach-sources",
                "jar-no-fork"
        );
        pluginManagement.addPlugin(plugin);
        executeMavenPluginWithInvoker(
                "org.apache.maven.plugins",
                "maven-source-plugin",
                config.getPluginVersions().getSourcePlugin(),
                "jar-no-fork",
                null);
    }

    private void configureJavadocPlugin(PluginManagement pluginManagement) throws MojoExecutionException {
        Plugin plugin = createBasicPlugin(
                "org.apache.maven.plugins",
                "maven-javadoc-plugin",
                config.getPluginVersions().getJavadocPlugin(),
                "attach-javadocs",
                "jar"
        );
        pluginManagement.addPlugin(plugin);
        executeMavenPluginWithInvoker(
                "org.apache.maven.plugins",
                "maven-javadoc-plugin",
                config.getPluginVersions().getJavadocPlugin(),
                "jar",
                null);
    }

    private Plugin createBasicPlugin(String groupId, String artifactId,
                                     String version, String executionId, String goal) {
        Plugin plugin = new Plugin();
        plugin.setGroupId(groupId);
        plugin.setArtifactId(artifactId);
        plugin.setVersion(version);

        Xpp3Dom executions = new Xpp3Dom("executions");
        Xpp3Dom execution = new Xpp3Dom("execution");

        Xpp3Dom id = new Xpp3Dom("id");
        id.setValue(executionId);
        execution.addChild(id);

        Xpp3Dom goals = new Xpp3Dom("goals");
        Xpp3Dom goalDom = new Xpp3Dom("goal");
        goalDom.setValue(goal);
        goals.addChild(goalDom);
        execution.addChild(goals);

        executions.addChild(execution);
        plugin.setConfiguration(executions);

        return plugin;
    }

    private void executeMavenPluginWithInvoker(String groupId, String artifactId, String version,
                                               String goal, Xpp3Dom configuration)
            throws MojoExecutionException {

        try {
            logger.info("通过Maven Invoker执行: " + artifactId + ":" + goal+":"+config.getProject());

            InvocationRequest request = new DefaultInvocationRequest();
            request.setPomFile(config.getProject().getFile());

            // 构建目标字符串：groupId:artifactId:version:goal
            String goalString = groupId + ":" + artifactId + ":" + version + ":" + goal;
            request.setGoals(Collections.singletonList(goalString));

            // 如果有配置，转换为系统属性
            if (configuration != null) {
                Properties props = new Properties();
                addConfigurationToProperties(configuration, props, "");
                request.setProperties(props);
            }

            Invoker invoker = new DefaultInvoker();
            InvocationResult result = invoker.execute(request);

            if (result.getExitCode() != 0) {
                throw new MojoExecutionException("插件执行失败: " + artifactId);
            }

        } catch (Exception e) {
            throw new MojoExecutionException("Maven调用失败", e);
        }
    }

    private void addConfigurationToProperties(Xpp3Dom config, Properties props, String prefix) {
        for (Xpp3Dom child : config.getChildren()) {
            String key = prefix.isEmpty() ? child.getName() : prefix + "." + child.getName();
            if (child.getChildCount() > 0) {
                addConfigurationToProperties(child, props, key);
            } else {
                props.setProperty(key, child.getValue());
            }
        }
    }

//    public void ensurePluginsInBuildSection(Build build) {
//        // 实现确保插件在 build/plugins 部分的逻辑
//        logger.debug("已确保插件在 build 部分配置");
//    }

//    public void configurePublishProfiles(Model model) {
//        // 自动发布 Profile
//        Profile autoProfile = createAutoProfile();
//        model.addProfile(autoProfile);
//
//        // 快照版 Profile
//        Profile snapshotProfile = createSnapshotProfile();
//        model.addProfile(snapshotProfile);
//
//        // 稳定版 Profile
//        Profile releaseProfile = createReleaseProfile();
//        model.addProfile(releaseProfile);
//
//        logger.debug("已配置发布配置文件");
//    }

//    private Profile createAutoProfile() {
//        Profile profile = new Profile();
//        profile.setId("auto");
//        profile.getProperties().setProperty("auto-releases-enabled", "true");
//        profile.getProperties().setProperty("wait-until", "published");
//        return profile;
//    }

//    private Profile createSnapshotProfile() {
//        Profile profile = new Profile();
//        profile.setId("snapshot");
//        profile.setActivation(new Activation());
//        profile.getActivation().setActiveByDefault(true);
//        return profile;
//    }

//    private Profile createReleaseProfile() {
//        Profile profile = new Profile();
//        profile.setId("release");
//        return profile;
//    }

}
