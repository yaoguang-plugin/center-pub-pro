package core.gitee.xudai.helper;

import core.gitee.xudai.strategy.license.enums.LicenseTypeEnum;
import org.apache.maven.model.License;
import org.apache.maven.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 许可证配置帮助类
 */
public class LicenseConfigurator {

    private static final Logger logger = LoggerFactory.getLogger(LicenseConfigurator.class);

    public static void configureApacheLicense(Model model) {
        if (model.getLicenses().isEmpty()) {
            License license = new License();
            license.setName("The Apache Software License, Version 2.0");
            license.setUrl("https://www.apache.org/licenses/LICENSE-2.0.txt");
            license.setDistribution("repo");
            model.addLicense(license);
            logger.debug("已配置 Apache 2.0 许可证");
        }
    }

    /**
     * 配置项目许可证
     */
    public static void configureLicense(Model model, CentralPublishConfig2 config) {
        if (model.getLicenses().isEmpty()) {
            License license = createLicense(config);
            model.addLicense(license);
            logger.info("已配置许可证: {}", license.getName());
        } else {
            logger.debug("项目已配置许可证，跳过自动配置");
        }
    }

    private static License createLicense(CentralPublishConfig2 config) {
        License license = new License();

        if (config.getLicenseType() != LicenseTypeEnum.UNKNOWN) {
            // 使用预定义许可证
            license.setName(config.getLicenseType().getName());
            license.setUrl(config.getLicenseType().getUrl());
            license.setDistribution(config.getLicenseType().getDistribution());
        } else if (config.getCustomLicenseName() != null && config.getCustomLicenseUrl() != null) {
            // 使用自定义许可证
            license.setName(config.getCustomLicenseName());
            license.setUrl(config.getCustomLicenseUrl());
            license.setDistribution("repo");
        } else {
            // 默认使用Apache 2.0
            license.setName(LicenseTypeEnum.APACHE_2_0.getName());
            license.setUrl(LicenseTypeEnum.APACHE_2_0.getUrl());
            license.setDistribution(LicenseTypeEnum.APACHE_2_0.getDistribution());
            logger.warn("未指定许可证类型，使用默认Apache 2.0许可证");
        }

        return license;
    }

    /**
     * 验证许可证配置
     */
    public static boolean validateLicenseConfig(CentralPublishConfig2 config) {
        if (config.getLicenseType() == LicenseTypeEnum.UNKNOWN) {
            if (config.getCustomLicenseName() == null || config.getCustomLicenseUrl() == null) {
                logger.warn("许可证配置不完整，将使用默认许可证");
                return false;
            }
        }
        return true;
    }

}
