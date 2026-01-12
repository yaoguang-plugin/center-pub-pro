package core.gitee.xudai.strategy.plugin.impl.javadoc;

import core.gitee.xudai.entity.CentralPublishConfig;
import core.gitee.xudai.strategy.plugin.enums.PluginEnum;
import core.gitee.xudai.strategy.plugin.support.AbstractPluginStrategy;
import lombok.extern.slf4j.Slf4j;

/**
 * Javadoc插件配置策略（用于生成文档Jar包）
 * @author daixu
 */
@Slf4j
public class JavadocPluginStrategy extends AbstractPluginStrategy {

    /**
     * 构造函数
     */
    public JavadocPluginStrategy() {
        super(PluginEnum.JAVADOC);
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
    protected void setBasicPluginExpand(BasicPlugin javadocPlugin,CentralPublishConfig config) {
        javadocPlugin .setGoal("jar");
        javadocPlugin .setExecutionId("attach-javadocs");
    }

}
