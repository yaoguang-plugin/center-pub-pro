package core.gitee.xudai.strategy.license.impl;

import core.gitee.xudai.strategy.license.enums.LicenseTypeEnum;
import core.gitee.xudai.strategy.license.support.AbstractLicenseStrategy;

/**
 * GPL 3.0 许可证策略
 */
public class Gpl3LicenseStrategy extends AbstractLicenseStrategy {

    public Gpl3LicenseStrategy() {
        super(LicenseTypeEnum.GPL_3_0);
    }

}
