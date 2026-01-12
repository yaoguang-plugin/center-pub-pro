package core.gitee.xudai.service;
import core.gitee.xudai.entity.CentralPublishConfig;
import core.gitee.xudai.strategy.plugin.enums.WaitUntilEnum;
import core.gitee.xudai.manager.PluginConfigurator;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.maven.model.Model;
import org.apache.maven.model.Build;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.project.MavenProject;
/**
 * Maven 中央仓库发布插件
 * @author daixu
 */
@Mojo(name = "central-publish", defaultPhase = LifecyclePhase.DEPLOY)
public class CentralPublishMojo extends AbstractMojo {

    /**
     * 自动获取使用此插件的Maven项目信息
     * 关键修改：使用 ${project} 表达式自动注入
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    /**
     * Maven会话信息，可用于获取执行环境
     */
    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession session;

    /**
     * 配置对象
     */
    @Parameter(property = "config", required = false, readonly = false)
    private CentralPublishConfig config;

    /**
     * 插件服务类
     */
    private PluginConfigurator pluginConfigurator;

    /**
     * 输出日志
     * 使用 SLF4J Logger 替代弃用的 getLog()
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        logger.info("Starting Maven Central Publishing...");
//        skipDefaultDeployPlugin();
        // 如果项目中有deploy-plugin配置，动态修改它，如果没有，测试是否可以正常发布，不能就创建一个
        dynamicallyConfigureDeployPlugin();
        // 初始化默认配置
        initializeConfig();


        logger.info("Project: " + project.getGroupId() + ":" + project.getArtifactId() + ":" + project.getVersion());

        try {

            // 验证必要参数
            validateParameters();

            // 验证配置
//            if (!config.isValid()) {
//                throw new MojoExecutionException("发布配置无效，请检查 publishingServerId 等必要参数");
//            }
            config.setProject(project);
            // 初始化插件配置器
            pluginConfigurator = new PluginConfigurator(config);

            // 配置项目属性
//            configureProjectProperties();

            // 配置所有插件
            configureBuildPlugins(config);

            // 设置跳过默认部署
//            System.setProperty("maven.deploy.skip", "true");

            // 直接调用Maven命令
//            ProcessBuilder pb = new ProcessBuilder(
//                    "mvn",
//                    "clean",
//                    "deploy",
//                    "-DskipTests=true",
//                    "-Dmaven.deploy.skip=false"
//            );

            logger.info("Central publishing configuration completed successfully");
            logger.info("Run 'mvn clean deploy' to publish to Maven Central");

        } catch (Exception e) {
            logger.error("Failed to configure central publishing", e);
            throw new MojoExecutionException("Central publishing configuration failed", e);
        }
    }

    private void dynamicallyConfigureDeployPlugin() {
        if (project.getBuild() != null) {
            for (Plugin plugin : project.getBuild().getPlugins()) {
                if ("org.apache.maven.plugins".equals(plugin.getGroupId())
                        && "maven-deploy-plugin".equals(plugin.getArtifactId())) {

                    Xpp3Dom config = (Xpp3Dom) plugin.getConfiguration();
                    if (config == null) {
                        config = new Xpp3Dom("configuration");
                        plugin.setConfiguration(config);
                    }

                    Xpp3Dom skip = config.getChild("skip");
                    if (skip == null) {
                        skip = new Xpp3Dom("skip");
                        config.addChild(skip);
                    }
                    skip.setValue("true");

                    getLog().debug("Dynamically configured deploy-plugin skip=true");
                    break;
                }
            }
        }
    }

    /**
     * 初始化配置对象
     * @author daixu
     */
    private void initializeConfig() {

        // 如果配置为空，创建默认配置
        if (config == null) {
            config = new CentralPublishConfig();
        }

        // 开关：跳过中央仓库发布插件执行
        if (config.isSkip()) {
            logger.info("Skipping central publish plugin execution");
        }

    }

    /**
     * 必传参数校验
     * @author daixu
     */
    private void validateParameters() throws MojoExecutionException {
        if (config.getPublishingServerId() == null || config.getPublishingServerId().trim().isEmpty()) {
            throw new MojoExecutionException("publishingServerId is required for central publishing");
        }
        if (config.getGpgPassphrase() == null || config.getGpgPassphrase().trim().isEmpty()) {
            throw new MojoExecutionException("gpgPassphrase is required for signing artifacts");
        }
        if (config.getGpgKeyname() == null || config.getGpgKeyname().trim().isEmpty()) {
            throw new MojoExecutionException("gpgKeyname is required for signing artifacts");
        }
        if (!WaitUntilEnum.UPLOADED.getValue().equals(config.getWaitUntil()) && !WaitUntilEnum.VALIDATED.getValue().equals(config.getWaitUntil()) && !WaitUntilEnum.PUBLISHED.getValue().equals(config.getWaitUntil())) {
            throw new MojoExecutionException("waitUntil must be one of: uploaded, validated, published");
        }
    }

    private void configureProjectProperties() {

        Model model = project.getModel();

        // 设置项目基本信息
        if (model.getUrl() == null) {
            model.setUrl("https://github.com/your-username/your-project");
        }

        if (model.getLicenses().isEmpty()) {
            pluginConfigurator.configureLicense(model);
        }

//        if (model.getScm() == null) {
//            pluginConfigurator.configureScm(model);
//        }

        // 设置发布相关属性
//        pluginConfigurator.configureProjectProperties(model);
    }

    private void configureBuildPlugins(CentralPublishConfig config) {
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
        logger.info("开始配置插件");
        // 配置所有必要的插件
        pluginConfigurator.configureAllPlugins(pluginManagement);
        logger.info("已配置所有必要的插件");

        // 确保插件也在 build/plugins 中配置
//        pluginConfigurator.ensurePluginsInBuildSection(build);
    }

    private void skipDefaultDeployPlugin() {
        // 方法1：设置系统属性（最有效）
        System.setProperty("maven.deploy.skip", "true");

        // 方法2：设置会话属性
        session.getUserProperties().setProperty("maven.deploy.skip", "true");

        // 方法3：设置项目属性
        project.getProperties().setProperty("maven.deploy.skip", "true");

        getLog().info("Skipped default maven-deploy-plugin execution");
    }

//    private void configurePublishProfiles() {
//        Model model = project.getModel();
//        pluginConfigurator.configurePublishProfiles(model);
//    }
//
//    private void saveModifiedPom() throws IOException {
//        File pomFile = project.getFile();
//        MavenXpp3Writer writer = new MavenXpp3Writer();
//
//        try (FileWriter fileWriter = new FileWriter(pomFile)) {
//            writer.write(fileWriter, project.getModel());
//        }
//
//        getLog().info("POM 文件已更新: " + pomFile.getAbsolutePath());
//    }

//    private void printUsageInstructions() {
//        logger.info("请执行以下命令进行发布：");
//        logger.info("发布快照版本: mvn clean deploy -P snapshot");
//        logger.info("发布稳定版本: mvn clean deploy -P release");
//
//        if (config.isAutoReleasesEnabled()) {
//            getLog().info("自动发布版本: mvn clean deploy -P auto,release");
//        }
//    }

    /**
     * 配置项目属性
     * @author daixu
     */
//    private void configureProjectProperties() {
//
//        // 设置项目属性
//        config.getProject().getProperties().setProperty("publishing-server-id", config.getPublishingServerId());
//        config.getProject().getProperties().setProperty("auto-releases-enabled", String.valueOf(config.isGenerateJavadoc()));
//        config.getProject().getProperties().setProperty("wait-until", config.getWaitUntil());
//
//        if (config.getGpgKeyname() != null) {
//            config.getProject().getProperties().setProperty("gpg.keyname", config.getGpgKeyname());
//        }
//
//        if (config.getGpgPassphrase() != null) {
//            config.getProject().getProperties().setProperty("gpg.passphrase", config.getGpgPassphrase());
//        }
//
//        // 设置自定义属性
//        if (config.getProperties() != null && !config.getProperties().isEmpty()) {
//            for (Map.Entry<String, String> entry : config.getProperties().entrySet()) {
//                config.getProject().getProperties().setProperty(entry.getKey(), entry.getValue());
//            }
//        }
//
//    }

    /**
     * 配置发布插件
     * @author daixu
     */
//    private void configurePublishingPlugin() {
//        Plugin publishingPlugin = new Plugin();
//        publishingPlugin.setGroupId("org.sonatype.central");
//        publishingPlugin.setArtifactId("central-publishing-maven-plugin");
//        publishingPlugin.setVersion("0.9.0");
//
//        Xpp3Dom configuration = new Xpp3Dom("configuration");
//
//        Xpp3Dom publishingServerIdDom = new Xpp3Dom("publishingServerId");
//        publishingServerIdDom.setValue(config.getPublishingServerId());
//        configuration.addChild(publishingServerIdDom);
//
//        Xpp3Dom autoPublishDom = new Xpp3Dom("autoPublish");
//        autoPublishDom.setValue(String.valueOf(config.isAutoReleasesEnabled()));
//        configuration.addChild(autoPublishDom);
//
//        Xpp3Dom waitUntilDom = new Xpp3Dom("waitUntil");
//        waitUntilDom.setValue(config.getWaitUntil());
//        configuration.addChild(waitUntilDom);
//
//        publishingPlugin.setConfiguration(configuration);
//
//        // 添加到项目构建插件中
//        config.getProject().getBuild().addPlugin(publishingPlugin);
//    }

    /**
     * 配置签名插件
     * @author daixu
     */
//    private void configureGpgPlugin() {
//        Plugin gpgPlugin = new Plugin();
//        gpgPlugin.setGroupId("org.apache.maven.plugins");
//        gpgPlugin.setArtifactId("maven-gpg-plugin");
//        gpgPlugin.setVersion("3.2.8");
//
//        Xpp3Dom configuration = new Xpp3Dom("configuration");
//
//        if (config.getGpgKeyname() != null) {
//            Xpp3Dom keynameDom = new Xpp3Dom("keyname");
//            keynameDom.setValue(config.getGpgKeyname());
//            configuration.addChild(keynameDom);
//        }
//
//        Xpp3Dom passphraseDom = new Xpp3Dom("passphrase");
//        passphraseDom.setValue(config.getGpgPassphrase());
//        configuration.addChild(passphraseDom);
//
//        gpgPlugin.setConfiguration(configuration);
//
//        config.getProject().getBuild().addPlugin(gpgPlugin);
//    }

    /**
     * 配置源码和文档插件
     * @author daixu
     */
//    private void configureAuxiliaryPlugins() {
//        if (config.isGenerateSources()) {
//            Plugin sourcePlugin = new Plugin();
//            sourcePlugin.setGroupId("org.apache.maven.plugins");
//            sourcePlugin.setArtifactId("maven-source-plugin");
//            sourcePlugin.setVersion("3.3.1");
//            config.getProject().getBuild().addPlugin(sourcePlugin);
//        }
//        if (config.isGenerateJavadoc()) {
//            Plugin javadocPlugin = new Plugin();
//            javadocPlugin.setGroupId("org.apache.maven.plugins");
//            javadocPlugin.setArtifactId("maven-javadoc-plugin");
//            javadocPlugin.setVersion("3.12.0");
//            config.getProject().getBuild().addPlugin(javadocPlugin);
//        }
//    }

}
