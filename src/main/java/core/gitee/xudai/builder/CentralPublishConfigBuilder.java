package core.gitee.xudai.builder;

import core.gitee.xudai.entity.CentralPublishConfig;
import core.gitee.xudai.strategy.plugin.enums.WaitUntilEnum;
import org.apache.maven.project.MavenProject;

/**
 * 配置对象建造者
 * 名称：建造者模式（Builder Pattern）
 * 作用：简化复杂对象的创建过程，提高配置的可读性和可维护性。
 * @author daixu
 */
public class CentralPublishConfigBuilder {

    private final CentralPublishConfig config;

    public CentralPublishConfigBuilder() {
        this.config = new CentralPublishConfig();
    }

    public CentralPublishConfigBuilder publishingServerId(String serverId) {
        config.setPublishingServerId(serverId);
        return this;
    }

    public CentralPublishConfigBuilder gpgKeyname(String keyname) {
        config.setGpgKeyname(keyname);
        return this;
    }

    public CentralPublishConfigBuilder gpgPassphrase(String passphrase) {
        config.setGpgPassphrase(passphrase);
        return this;
    }

    public CentralPublishConfigBuilder waitUntil(WaitUntilEnum waitUntil) {
        config.setWaitUntil(waitUntil.getValue());
        return this;
    }

    public CentralPublishConfigBuilder project(MavenProject project) {
        config.setProject(project);
        return this;
    }

    public CentralPublishConfigBuilder skip(boolean skip) {
        config.setSkip(skip);
        return this;
    }

    public CentralPublishConfig build() {
        // 验证必要参数
        validate();
        return config;
    }

    private void validate() {
        if (config.getPublishingServerId() == null) {
            throw new IllegalStateException("publishingServerId is required");
        }
        if (config.getGpgPassphrase() == null) {
            throw new IllegalStateException("gpgPassphrase is required");
        }
    }
}
