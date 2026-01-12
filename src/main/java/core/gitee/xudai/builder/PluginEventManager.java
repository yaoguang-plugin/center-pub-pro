package core.gitee.xudai.builder;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PluginEventManager {
    private List<PluginConfigurationListener> listeners = new CopyOnWriteArrayList<>();

    public void addListener(PluginConfigurationListener listener) {
        listeners.add(listener);
    }

    public void removeListener(PluginConfigurationListener listener) {
        listeners.remove(listener);
    }

    public void publishEvent(PluginConfigurationEvent event) {
        for (PluginConfigurationListener listener : listeners) {
            listener.onEvent(event);
        }
    }

    // 在策略中使用
//    @Override
//    public void configure(PluginManagement pluginManagement, CentralPublishConfig config) {
//        eventManager.publishEvent(new PluginConfigurationEvent(getPluginId(), EventType.START, null));
//        try {
//            // 配置逻辑
//            eventManager.publishEvent(new PluginConfigurationEvent(getPluginId(), EventType.SUCCESS, null));
//        } catch (Exception e) {
//            eventManager.publishEvent(new PluginConfigurationEvent(getPluginId(), EventType.FAILURE, e));
//            throw e;
//        }
//    }
}
