package core.gitee.xudai.config;

import core.gitee.xudai.metadata.BaseConfigLoadMetadata;

import java.util.List;

/**
 * 配置加载器接口
 * @param <E> 配置项类型（单个配置元素）
 * @param <M> 加载元数据类型
 * @author daixu
 */
public interface ConfigLoaderContainer<E, M extends BaseConfigLoadMetadata> {

    /**
     * 加载配置文件
     */
    void load();

    /**
     * 获取加载后的配置列表
     * @return 配置项列表
     */
    List<E> getConfig();

    /**
     * 获取加载过程的元数据
     * @return 加载元数据
     */
    M getMetadata();

}
