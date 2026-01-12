package core.gitee.xudai.strategy.license.impl;

import core.gitee.xudai.strategy.license.enums.LicenseTypeEnum;
import core.gitee.xudai.strategy.license.support.AbstractLicenseStrategy;

/**
 * MIT 许可证策略
 */
public class MitLicenseStrategy extends AbstractLicenseStrategy {

    public MitLicenseStrategy() {
        super(LicenseTypeEnum.MIT);
    }

}
