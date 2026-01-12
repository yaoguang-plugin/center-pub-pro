package core.gitee.xudai.config.loader.b.loader;

import core.gitee.xudai.container.DependencyConfigContainer;
import core.gitee.xudai.container.PluginConfigContainer;
import core.gitee.xudai.metadata.BaseConfigLoadMetadata;
import core.gitee.xudai.metadata.DependencyConfigLoadMetadata;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author daixu
 */
@Slf4j
public class DependencyConfigLoader implements ConfigLoaderContainer<DependencyConfigContainer, DependencyConfigLoadMetadata> {

    private final String yamlPath = "dependency-config.yaml";
    private DependencyConfigContainer config;
    private final DependencyConfigLoadMetadata metadata = new DependencyConfigLoadMetadata();

    @Override
    public void load() {
        metadata.setFilePath(yamlPath);
        metadata.setLoadTime(LocalDateTime.now());
        metadata.setSource(BaseConfigLoadMetadata.ConfigSource.LOCAL_FILE);
        try {
//            config = new YAMLMapper().readValue(new File(yamlPath), DependencyConfigContainer.class);
            // 复用 YamlConfigLoader2 的通用加载方法（类路径读取 + SnakeYAML 解析）
            config = YamlConfigLoader.loadYamlConfig(yamlPath, DependencyConfigContainer.class);
            metadata.setLoadStatus(BaseConfigLoadMetadata.LoadStatus.SUCCESS);
        } catch (IOException e) {
            metadata.setLoadStatus(BaseConfigLoadMetadata.LoadStatus.FAILED);
            metadata.setErrorMessage("依赖配置加载失败：" + e.getMessage());
            throw new RuntimeException(metadata.getErrorMessage(), e);
        }
    }

    public void load(int retryCount) {
        for (int i = 0; i < retryCount; i++) {
            try {
                config = YamlConfigLoader.loadYamlConfig(filePath, PluginConfigContainer.class);
                metadata.setLoadStatus(BaseConfigLoadMetadata.LoadStatus.SUCCESS);
                return;
            } catch (IOException e) {
                if (i == retryCount - 1) { // 最后一次失败则记录并抛出
                    metadata.setLoadStatus(BaseConfigLoadMetadata.LoadStatus.FAILED);
                    metadata.setErrorMessage("加载失败：" + e.getMessage());
                    throw new RuntimeException(metadata.getErrorMessage(), e);
                }
                log.warn("第{}次加载失败，重试...", i + 1);
            }
        }
    }

    @Override
    public DependencyConfigContainer getConfig() {
        return config;
    }

    @Override
    public DependencyConfigLoadMetadata getMetadata() {
        return metadata;
    }

}
