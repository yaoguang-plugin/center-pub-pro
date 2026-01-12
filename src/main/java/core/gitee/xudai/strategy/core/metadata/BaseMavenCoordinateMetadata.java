package core.gitee.xudai.strategy.core.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Maven坐标基础类，封装公共的groupId、artifactId、version字段
 * 供插件信息、依赖信息等需要 Maven 坐标的类继承
 * @author daixu
 */
@Getter
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(force = true)
public abstract class BaseMavenCoordinateMetadata {

    /** 组ID（Maven坐标） */
    protected final String groupId;

    /**  artifact ID（Maven坐标） */
    protected final String artifactId;

    /** 版本（Maven坐标） */
    protected final String version;

}
