package core.gitee.xudai.mapper;

import core.gitee.xudai.config.loader.b.config.ArtifactInfoConfig;
import core.gitee.xudai.strategy.core.metadata.BaseMavenCoordinateMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 基础映射接口
 */
@Mapper(componentModel = "default")
public interface ArtifactInfoMapper {

    @Mapping(source = "groupId", target = "groupId")
    @Mapping(source = "artifactId", target = "artifactId")
    @Mapping(source = "version", target = "version")
    BaseMavenCoordinateMetadata toArtifactStrategyInfo(ArtifactInfoConfig info);

}
