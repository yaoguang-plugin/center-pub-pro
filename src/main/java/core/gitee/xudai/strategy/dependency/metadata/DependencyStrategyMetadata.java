package core.gitee.xudai.strategy.dependency.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import core.gitee.xudai.strategy.core.enums.StrategyType;
import core.gitee.xudai.strategy.core.metadata.BaseMavenCoordinateMetadata;
import core.gitee.xudai.strategy.core.metadata.BaseStrategyMetadata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

/**
 * 依赖元数据
 * @author daixu
 */
@Getter
@SuperBuilder(toBuilder = true) // 支持建造者模式及对象复制修改
@NoArgsConstructor(force = true) // 兼容反序列化（如YAML/JSON解析）
public class DependencyStrategyMetadata extends BaseStrategyMetadata<DependencyStrategyMetadata> {

    @JsonProperty("dependencyInfo")
    private final DependencyStrategyInfo dependencyStrategyInfo;

    /**
     * 实现父类的抽象方法：明确策略类型为"license"
     */
    @Override
    public String getStrategyType() {
        return StrategyType.DEPENDENCY.getValue();
    }

    // ------------------------------ 内部类：依赖详细信息 ------------------------------

    /** 依赖详细信息元数据类（对应配置中的dependencyInfo节点） */
    @Getter
    @AllArgsConstructor
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor(force = true)
    public static class DependencyStrategyInfo extends BaseMavenCoordinateMetadata {
        /** 依赖类型（默认jar，可指定war、ear、pom等） */
        private final String type;
        /** 依赖作用域（默认compile） */
        private final String scope;
        /** 是否为可选依赖（true则不传递） */
        private final Boolean optional;
        /** 分类器（区分同一artifact的不同构建产物） */
        private final String classifier;
        /** 排除的传递依赖列表 */
        private final List<Map<String, String>> exclusions;
        /** 本地jar路径（仅scope=system时使用） */
        private final String systemPath;
        /** 版本范围（高级特性，如[5.0,6.0)） */
        private final String versionRange;
    }

}
