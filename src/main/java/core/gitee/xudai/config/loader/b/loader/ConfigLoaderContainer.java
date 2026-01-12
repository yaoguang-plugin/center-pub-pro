package core.gitee.xudai.config.loader.b.loader;

import core.gitee.xudai.metadata.BaseConfigLoadMetadata;

import java.util.List;

/**
 * 配置加载器接口
 * @author daixu
 */
public interface ConfigLoaderContainer<E, M extends BaseConfigLoadMetadata> {

    /**
     * 加载配置文件
     */
    void load();

    /**
     * 获取加载后的业务配置对象
     * @return
     */
    List<E> getConfigs();

    /**
     * 获取加载过程的元数据（如加载状态、错误信息）
     * @return 加载元数据实例
     */
    M getLoadMetadata();

}
