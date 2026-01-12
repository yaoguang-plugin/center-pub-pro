package core.gitee.xudai.builder;

import jdk.jfr.EventType;
import lombok.Data;

/**
 * 观察者模式（Observer Pattern）
 * 作用：监控配置过程中的关键事件，便于扩展日志、监控等功能。
 */
@Data
public class PluginConfigurationEvent {
    private final String pluginId;
    private final EventType type;
    private final long timestamp;
    private final Object data;
}
