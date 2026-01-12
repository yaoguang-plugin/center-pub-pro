package core.gitee.xudai.config.loader.b.config;

import core.gitee.xudai.strategy.license.metadata.LicenseStrategyMetadata;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 许可证配置类
 * @author daixu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LicenseConfig extends BaseConfig<LicenseStrategyMetadata> {

    @NotNull(message = "licenseInfo cannot be null")
    @JsonProperty("licenseInfo")
    private LicenseInfo licenseInfo;

    // 子类特有配置字段（与 LicenseStrategyMetadata 的 licenseType 对应）
    @JsonProperty("licenseType") // 与配置文件字段映射
    @NotNull(message = "licenseType cannot be null")
    private String licenseType;

    @Override
    public void extraValidation() {
        // 自定义校验逻辑
        if (licenseInfo != null && !licenseInfo.getUrl().startsWith("https")) {
            throw new IllegalArgumentException("许可证 URL 必须是 HTTPS 协议 (ID: " + this.id + ")");
        }
        // 校验 licenseType 非空
        if (licenseType == null || licenseType.isEmpty()) {
            throw new IllegalArgumentException("licenseType 不能为空 (ID: " + this.id + ")");
        }
    }

    /**
     * 许可证信息元数据类
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LicenseInfo {

        @NotNull(message = "licenseInfo name cannot be null")
        private String name;

        @NotNull(message = "licenseInfo url cannot be null")
        private String url;

        @NotNull(message = "licenseInfo url cannot be null")
        private String distribution;

    }

}
