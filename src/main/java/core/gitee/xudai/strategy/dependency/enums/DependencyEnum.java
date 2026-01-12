package core.gitee.xudai.strategy.dependency.enums;
/**
 * 插件类型枚举
 */
public enum DependencyEnum {

    PLEXUS_UTILS("org.codehaus.plexus","plexus-utils", "3.4.2", "provided","发布必用工具依赖");

    /**
     * 插件组 Id
     */
    private final String groupId;

    /**
     * 插件 Id
     */
    private final String artifactId;

    /**
     * 插件版本
     */
    private final String version;

    /**
     * 插件作用域
     * 包含阶段：编译、测试、运行、打包
     * 传递性：会传递给依赖当前项目的其他项目
     */
    private final Strin修复内部类名g scope;
    private final String description;

    DependencyEnum(String groupId, String artifactId, String version, String description, String scope) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.description = description;
        this.scope = scope;
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

    public String getScope() {
        return scope;
    }
}
