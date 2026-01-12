package core.gitee.xudai.entity;

import core.gitee.xudai.strategy.plugin.enums.WaitUntilEnum;
import lombok.Data;
import org.apache.maven.plugins.annotations.Parameter;

@Data
public class CentralPubConfig {

    /**
     * 发布服务器ID
     */
    @Parameter(property = "publishingServerId", required = true, defaultValue = "central")
    private String publishingServerId;

    /**
     * 是否自动发布 RELEASE 版本
     */
    @Parameter(property = "autoReleasesEnabled", required = false, defaultValue = "false")
    private String autoReleasesEnabled;

    /**
     * 等待级别：uploaded/validated/published
     */
    @Parameter(property = "waitUntil", defaultValue = WaitUntilEnum.VALIDATED_CONSTANTS)
    private String waitUntil;

}
