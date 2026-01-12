package core.gitee.xudai.strategy.plugin.enums;

/**
 * 等待发布状态枚举
 * @author daixu
 */
public enum WaitUntilEnum {

    /**
     * 仅等待文件上传完成,快速上传，后续手动处理，只报告上传失败。
     */
    UPLOADED("uploaded"),

    /**
     * 等待上传+服务器验证,确保文件格式正确，报告上传和验证失败。
     */
    VALIDATED("validated"),

    /**
     * 等待上传+验证+发布完成，完全自动化发布，报告所有阶段失败。
     */
    PUBLISHED("published");

    /**
     * 仅等待文件上传完成,快速上传，后续手动处理，只报告上传失败。
     */
    public static final String UPLOADED_CONSTANTS = "uploaded";

    /**
     * 等待上传+服务器验证,确保文件格式正确，报告上传和验证失败。
     */
    public static final String VALIDATED_CONSTANTS = "validated";

    /**
     * 等待上传+验证+发布完成，完全自动化发布，报告所有阶段失败。
     */
    public static final String PUBLISHED_CONSTANTS = "published";

    private final String value;

    WaitUntilEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
