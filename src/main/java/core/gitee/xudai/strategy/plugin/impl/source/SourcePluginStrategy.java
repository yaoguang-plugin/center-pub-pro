package core.gitee.xudai.strategy.plugin.impl.source;

import core.gitee.xudai.entity.CentralPublishConfig;
import core.gitee.xudai.strategy.plugin.enums.PluginEnum;
import core.gitee.xudai.strategy.plugin.support.AbstractPluginStrategy;
import lombok.extern.slf4j.Slf4j;

/**
 * 源码插件配置策略（用于生成源码Jar包）
 * @author daixu
 */
@Slf4j
public class SourcePluginStrategy extends AbstractPluginStrategy {

    public SourcePluginStrategy() {
        super(PluginEnum.SOURCE);
    }

    /**
     * 源码插件为中央仓库发布必需项，禁用则发布失败
     */
    @Override
    public boolean isRequired(CentralPublishConfig config) {
        // 无论环境如何，源码插件都是必需的（中央仓库强制要求）
        return true;
    }

    // 源码插件对GPL等许可证是强制的
//    @Override
//    public boolean isCompatibleWithLicense(CentralPublishConfig config) {
//        LicenseTypeEnum licenseType = config.getLicenseType();
//        if (licenseType == LicenseTypeEnum.GPL_V3 || licenseType == LicenseTypeEnum.LGPL_V3) {
//            return !config.isSkipSource(); // 不能跳过源码插件
//        }
//        return true; // 其他许可证允许跳过
//    }

    /**
     * 仅在生产环境强制启用，开发环境可禁用（加速开发发布）
     */
    @Override
    public boolean isEnabled(CentralPublishConfig config) {
        // 从 Maven 项目的属性中读取环境（假设 pom.xml 中定义了 <env>prod</env>）
        String env = config.getProject().getProperties().getProperty("env", "dev");
        if ("dev".equals(env)) {
            return true;
        } else {
            return true;
        }
    }

    @Override
    protected void setBasicPluginExpand(BasicPlugin sourcePlugin,CentralPublishConfig config) {
        // ✅ 设置跳过默认发布插件
        sourcePlugin.setGoal("jar-no-fork");
        sourcePlugin.setExecutionId("attach-sources");
    }

}
