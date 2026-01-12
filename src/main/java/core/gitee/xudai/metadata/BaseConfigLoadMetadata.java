package core.gitee.xudai.metadata;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 配置元数据基类（抽取共通属性）
 * @author daixu
 */
@Data
public class BaseConfigLoadMetadata {

    /**
     * 配置文件路径（绝对路径/相对路径）
     */
    private String filePath;

    /**
     * 加载时间（精确到秒）
     */
    private LocalDateTime loadTime;

    /**
     * 加载状态（SUCCESS/FAILED）
     */
    private LoadStatus loadStatus;

    /**
     * 配置来源（本地文件/远程配置中心/环境变量）
     */
    private ConfigSource source = ConfigSource.LOCAL_FILE;

    /**
     * 加载失败时的错误信息（状态为FAILED时非空）
     */
    private String errorMessage;

    /**
     * 加载状态枚举
     */
    public enum LoadStatus {

        /** 成功 */
        SUCCESS,

        /** 失败 */
        FAILED,

        /** 未加载 */
        UNLOADED,

        /** 加载中  */
        LOADING
    }

    /**
     * 配置来源枚举
     */
    public enum ConfigSource {

        /** 本地文件 */
        LOCAL_FILE,

        /** 远程配置中心 */
        REMOTE_CONFIG_CENTER,

        /** 环境 */
        ENVIRONMENT,

        /** 系统属性 */
        SYSTEM_PROPERTY

    }

}
