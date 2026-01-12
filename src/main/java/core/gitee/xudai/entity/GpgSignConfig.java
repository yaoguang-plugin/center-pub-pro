package core.gitee.xudai.entity;

import lombok.Data;
import org.apache.maven.plugins.annotations.Parameter;

@Data
public class GpgSignConfig {

    /**
     * GPG密钥名称
     */
    @Parameter(property = "gpgKeyname", required = true)
    private String gpgKeyname;

    /**
     * GPG密码
     */
    @Parameter(property = "gpgPassphrase", required = true)
    private String gpgPassphrase;


}
