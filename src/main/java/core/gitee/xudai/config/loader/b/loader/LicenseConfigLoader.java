package core.gitee.xudai.config.loader.b.loader;

import core.gitee.xudai.container.LicenseConfigContainer;
import core.gitee.xudai.metadata.LicenseConfigLoadMetadata;

/**
 * @author daixu
 */
public class LicenseConfigLoader extends AbstractConfigLoader<LicenseConfigContainer, LicenseConfigLoadMetadata> {

    // 固定配置文件路径
    private static final String FILE_PATH = "license-config.yaml";

    public LicenseConfigLoader() {
        super(FILE_PATH, LicenseConfigContainer.class, new LicenseConfigLoadMetadata());
    }

    /**
     * 许可证配置特有校验
     * @param config
     */
    @Override
    protected void customValidate(LicenseConfigContainer config) {
        // 许可证配置特有校验（示例）
//        if (config.getLicenseKey() == null || config.getLicenseKey().trim().isEmpty()) {
//            throw new IllegalArgumentException("许可证密钥(licenseKey)不能为空");
//        }
    }

}
