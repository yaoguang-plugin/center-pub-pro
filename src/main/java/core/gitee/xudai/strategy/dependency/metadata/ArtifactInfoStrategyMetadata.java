package core.gitee.xudai.strategy.dependency.metadata;

import core.gitee.xudai.strategy.core.metadata.BaseStrategyMetadata;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 依赖元数据
 * @author daixu
 */
@Getter
@SuperBuilder(toBuilder = true) // 支持建造者模式及对象复制修改
@NoArgsConstructor(force = true) // 兼容反序列化（如YAML/JSON解析）
public class ArtifactInfoStrategyMetadata extends BaseStrategyMetadata<ArtifactInfoStrategyMetadata> {



}
