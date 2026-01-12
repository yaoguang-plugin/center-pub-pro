package core.gitee.xudai.metadata;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 依赖配置元数据
 * @author daixu
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DependencyConfigLoadMetadata extends BaseConfigLoadMetadata {

    // 依赖配置专属元数据：是否自动解析传递依赖
    private boolean autoResolveTransitive = true;

}
