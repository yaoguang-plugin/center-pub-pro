package core.gitee.xudai.container;

import com.fasterxml.jackson.annotation.JsonProperty;
import core.gitee.xudai.config.loader.b.config.LicenseConfig;
import lombok.Data;

import java.util.List;

/**
 * 许可证配置容器（对应license-config.yaml根节点）
 * @author daixu
 */
@Data
public class LicenseConfigContainer {

    @JsonProperty("licenses")
    private List<LicenseConfig> licenses;

}
