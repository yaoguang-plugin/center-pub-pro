package core.gitee.xudai.strategy.plugin.impl.central;

import core.gitee.xudai.entity.CentralPublishConfig;
import core.gitee.xudai.strategy.plugin.enums.PluginEnum;
import core.gitee.xudai.strategy.plugin.support.AbstractPluginStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.HashMap;
import java.util.Map;

/**
 * 中央仓库发布插件配置策略（用于将构件发布到中央仓库）
 * @author daixu
 */
@Slf4j
public class CentralPublishingPluginStrategy extends AbstractPluginStrategy {

    /**
     * 构造函数
     */
    public CentralPublishingPluginStrategy() {
        super(PluginEnum.CENTRAL_PUBLISHING);
    }

    /**
     * 源码插件为中央仓库发布必需项，禁用则发布失败
     */
    @Override
    public boolean isRequired(CentralPublishConfig config) {
        // 无论环境如何，源码插件都是必需的（中央仓库强制要求）
        return true;
    }

    @Override
    protected void setBasicPluginExpand(BasicPlugin basicPlugin,CentralPublishConfig config) {
        // ✅ 标记为扩展插件
        basicPlugin.setExpandTags(true);
    }

    /**
     * 获取中央仓库发布插件的基础信息
     * @param config 中央仓库发布配置
     * @return 插件基础信息
     * @throws MojoExecutionException 配置过程中发生的异常
     * @author daixu
     */
    @Override
    protected Map<String, String> getConfiguration(CentralPublishConfig config) throws MojoExecutionException {

        // ✅ 设置发布配置（服务器ID、自动发布开关等）
        Map<String,String> configuration = new HashMap<>();
        // TODO key 是否可以放在其他地方进行维护
        configuration.put("publishingServerId",config.getPublishingServerId());
        configuration.put("autoPublish",config.getAutoReleasesEnabled());
        configuration.put("waitUntil",config.getWaitUntil());
        log.info("已配置中央发布插件");

        return configuration;
    }

}
