package core.gitee.xudai.strategy.plugin.enums;
/**
 * 插件类型枚举
 */
public enum PluginEnum {

    GPG("org.apache.maven.plugins","maven-gpg-plugin", "3.2.8","GPG签名插件", 0),
    SOURCE("org.apache.maven.plugins","maven-source-plugin", "3.3.1","源码打包插件", 1),
    JAVADOC("org.apache.maven.plugins","maven-javadoc-plugin", "3.12.0","文档生成插件", 2),
    CENTRAL_PUBLISHING("org.sonatype.central","central-publishing-maven-plugin","0.9.0", "中央仓库发布插件", 3),
    DEPLOY("org.apache.maven.plugins","maven-deploy-plugin", "3.1.0","部署插件", 4);

    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String description;
    private final int order;

    PluginEnum(String groupId, String artifactId, String version, String description, int order) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.description = description;
        this.order = order;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public int getOrder() {
        return order;
    }
}
