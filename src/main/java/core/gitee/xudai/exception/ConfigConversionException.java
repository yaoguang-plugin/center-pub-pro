package core.gitee.xudai.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 配置转换异常
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ConfigConversionException extends RuntimeException {

    private final String configId;
    private final String configType;

    public ConfigConversionException(String configId, String configType, String message, Throwable cause) {
        super(String.format("配置转换失败[%s-%s]: %s", configType, configId, message), cause);
        this.configId = configId;
        this.configType = configType;
    }

}
