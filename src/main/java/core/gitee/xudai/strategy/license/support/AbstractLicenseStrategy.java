package core.gitee.xudai.strategy.license.support;

import core.gitee.xudai.entity.CentralPublishConfig;
import core.gitee.xudai.strategy.license.enums.LicenseTypeEnum;
import lombok.extern.slf4j.Slf4j;
import core.gitee.xudai.strategy.license.api.LicenseStrategy;
import org.apache.maven.model.License;
import org.apache.maven.model.Model;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * 许可证策略抽象基类
 * @author daixu
 */
@Slf4j
public abstract class AbstractLicenseStrategy implements LicenseStrategy {

    private final LicenseTypeEnum licenseType;

    public AbstractLicenseStrategy(LicenseTypeEnum licenseType) {
        this.licenseType = licenseType;
    }

    @Override
    public String getLicenseName() {
        return licenseType.getName();
    }

    @Override
    public String getLicenseUrl() {
        return licenseType.getUrl();
    }

    @Override
    public String getDistribution() {
        return licenseType.getDistribution();
    }

    @Override
    public String getStrategyName() {
        return "许可证策略: " + getLicenseName();
    }

    @Override
    public boolean isEnabled(CentralPublishConfig config) {
        // 默认启用
        return true;
    }

    @Override
    public boolean isRequired(CentralPublishConfig config) {
        // 许可证为必需项
        return true;
    }

    @Override
    public void configureLicense(CentralPublishConfig config) throws MojoExecutionException {

        // 检测项目中是否已存在有效的许可证配置
        boolean hasExistingLicense = LicenseDetector.hasExistingLicense(config.getProject());

        if (hasExistingLicense) {
            log.info("项目中已包含许可证配置，跳过插件的许可证策略");
            return; // 已存在许可证，不执行插件的配置
        }

        log.info("配置许可证: {}", getLicenseName());

        // 从 config 中 获取 Mode l实例
        Model model = config.getProject().getModel();
        // 实际项目中这里会将许可证信息配置到项目中
        License license = new License();
        license.setName(licenseType.getName());
        license.setUrl(licenseType.getUrl());
        license.setDistribution(licenseType.getDistribution());
        model.addLicense(license);
        log.debug("已配置 {} 许可证",licenseType.getDistribution());
    }

    @Override
    public int getOrder() {
        return 0; // 许可证配置优先于其他配置
    }

}
