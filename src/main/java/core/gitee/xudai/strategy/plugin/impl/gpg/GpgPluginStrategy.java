package core.gitee.xudai.strategy.plugin.impl.gpg;

import core.gitee.xudai.config.loader.b.config.PluginConfig;
import core.gitee.xudai.entity.CentralPublishConfig;
import core.gitee.xudai.strategy.plugin.enums.PluginEnum;
import core.gitee.xudai.strategy.plugin.support.AbstractPluginStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.HashMap;
import java.util.Map;

/**
 * GPG 插件配置策略（用于签名发布文件）
 * @author daixu
 */
@Slf4j
public class GpgPluginStrategy extends AbstractPluginStrategy {

    // 当前插件ID（与YAML中pluginId一致）
    private static final String CURRENT_PLUGIN_ID = "source";

    // 构造方法（传入插件元数据，父类AbstractPluginStrategy已实现）
    public SourcePluginStrategy(PluginConfigLoader.PluginMetadata metadata) {
        super(metadata);
    }

    // 用枚举的getCode()获取插件ID，避免硬编码
    INSTANCE(PluginConfigLoader.INSTANCE.getPluginConfig(PluginIdEnum.GPG.getCode()));

    private final PluginConfig metadata;

    GpgPluginStrategy(PluginConfig metadata) {
        this.metadata = metadata;
    }

    public GpgPluginStrategy() {
        super(PluginEnum.GPG);
    }

    /**
     * 配置 GPG 插件到 PluginManagement（包含签名密钥配置）
     * 提供GPG插件的具体信息（差异化逻辑）
     * @param config 中央仓库发布配置
     * @throws MojoExecutionException 配置失败时抛出异常
     * @author daixu
     */
    @Override
    protected Map<String, String> getConfiguration(CentralPublishConfig config) throws MojoExecutionException {
        // 设置GPG签名配置（密钥名、密码）
        Map<String, String> configuration = new HashMap<>();
        if (config.getGpgKeyname() == null || config.getGpgPassphrase() == null) {
            throw wrapException("GPG密钥或密码未配置");
        }
        configuration.put("keyname", config.getGpgKeyname());
        configuration.put("passphrase", config.getGpgPassphrase());
        return configuration;
    }

}
