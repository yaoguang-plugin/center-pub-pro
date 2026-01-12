package core.gitee.xudai.metadata;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 许可证配置元数据
 * @author daixu
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LicenseConfigLoadMetadata extends BaseConfigLoadMetadata {

    // 许可证配置专属元数据：是否需要校验许可证有效性
    private boolean needValidate = true;

}
