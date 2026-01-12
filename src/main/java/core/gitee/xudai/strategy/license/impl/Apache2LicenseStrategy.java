package core.gitee.xudai.strategy.license.impl;

import core.gitee.xudai.strategy.license.enums.LicenseTypeEnum;
import core.gitee.xudai.strategy.license.support.AbstractLicenseStrategy;

/**
 * Apache 2.0 许可证策略
 * @author daixu
 */
public class Apache2LicenseStrategy extends AbstractLicenseStrategy {

    /**
     * 创建 Apache 2.0 许可证策略
     */
    public Apache2LicenseStrategy() {
        super(LicenseTypeEnum.APACHE_2_0);
    }

    @Override
    public int getOrder() {
        return -1; // 默认许可证优先级最高
    }

}
