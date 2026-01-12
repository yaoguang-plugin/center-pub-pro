package core.gitee.xudai.helper;

import core.gitee.xudai.entity.CentralPublishConfig;
import core.gitee.xudai.strategy.plugin.enums.WaitUntilEnum;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * 配置验证帮助类
 */
public class ConfigurationValidator {

    public static void validatePublishConfig(CentralPublishConfig config)
            throws MojoExecutionException {

        if (config == null) {
            throw new MojoExecutionException("发布配置不能为空");
        }

        if (config.getPublishingServerId() == null || config.getPublishingServerId().trim().isEmpty()) {
            throw new MojoExecutionException("publishingServerId 是必需的参数");
        }

        if (config.getGpgPassphrase() == null || config.getGpgPassphrase().trim().isEmpty()) {
            throw new MojoExecutionException("gpgPassphrase 是必需的参数");
        }

        if (config.getGpgKeyname() == null || config.getGpgKeyname().trim().isEmpty()) {
            throw new MojoExecutionException("gpgKeyname 是必需的参数");
        }

        validateWaitUntil(config.getWaitUntil());
    }

    private static void validateWaitUntil(String waitUntil) throws MojoExecutionException {
        if (!WaitUntilEnum.UPLOADED.getValue().equals(waitUntil) &&
                !WaitUntilEnum.VALIDATED.getValue().equals(waitUntil) &&
                !WaitUntilEnum.PUBLISHED.getValue().equals(waitUntil)) {
            throw new MojoExecutionException("waitUntil 必须是: uploaded, validated, published 之一");
        }
    }

}
