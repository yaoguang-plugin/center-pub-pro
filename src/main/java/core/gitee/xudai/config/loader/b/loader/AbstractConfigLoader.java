package core.gitee.xudai.config.loader.b.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import core.gitee.xudai.config.loader.b.config.BaseConfig;
import core.gitee.xudai.config.loader.b.converter.ConfigToMetadataConverter;
import core.gitee.xudai.config.loader.b.factory.ConverterFactory;
import core.gitee.xudai.metadata.BaseConfigLoadMetadata;
import core.gitee.xudai.strategy.core.metadata.BaseStrategyMetadata;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 配置加载抽象父类（最终无报错版）
 * 泛型说明：
 * @param <E>        单个配置项类型（如 PluginConfig），必须继承 BaseConfig<METADATA>
 * @param <M>        加载元数据类型（如 PluginConfigLoadMetadata）
 * @param <METADATA> 元数据类型（如 PluginStrategyMetadata）
 * @author daixu
 */
@Slf4j
public abstract class AbstractConfigLoader<
        E extends BaseConfig<METADATA>,  // 核心：E必须是BaseConfig<METADATA>的子类
        M extends BaseConfigLoadMetadata,
        METADATA extends BaseStrategyMetadata<METADATA>  // 元数据也加约束（可选，根据实际类型）
        > implements ConfigLoaderContainer<List<E>, M> {

    // 子类的配置类型（通过泛型反射自动获取）
    protected final Class<E> elementType;

    /**
     * 配置文件路径（由子类指定）
     */
    @Getter
    private final String filePath;

    // 配置到元数据的转换器
    private final ConfigToMetadataConverter<E, METADATA> converter;

    /**
     * 加载元数据实例（记录加载状态）
     */
    @Getter
    private final M loadMetadata;

    // 加载后的配置集合（不可变，仅通过getter访问）
    @Getter
    private List<E> configs;

    // 新增：存储转换后的元数据集合（可选，根据需求决定是否保留）
    private List<METADATA> strategyMetadataList;

    /**
     * 构造器：子类传递差异化参数
     * @param filePath 配置文件路径（如 "plugin-configs.yaml"）
     * @param configTypeRef 配置集合类型引用（如 new TypeReference<List<PluginConfig>>() {}）
     * @param loadMetadata 加载元数据实例
     */
    // 构造器：要求子类传递转换器（或通过其他方式获取转换器）
// 构造器（现在泛型E的约束满足，不会报错）
    public AbstractConfigLoader(
            Class<E> elementType, String filePath,
            TypeReference<List<E>> configTypeRef,
            M loadMetadata
    ) {
        this.elementType = elementType;
        this.filePath = filePath;
        this.loadMetadata = loadMetadata;
        // 从ConverterFactory获取转换器（集成工厂模式）
        this.converter = ConverterFactory.getConverter((Class<E>) getGenericType(configTypeRef));
        // 初始化加载元数据基础信息
        initLoadMetadata();
        // 其他初始化逻辑
        this.loadMetadata.setFilePath(filePath);
        this.loadMetadata.setSource(BaseConfigLoadMetadata.ConfigSource.LOCAL_FILE);
    }

    // 抽象父类构造方法：通过泛型反射提取子类的E类型
//    @SuppressWarnings("unchecked")
//    protected AbstractConfigLoader(String filePath) {
//        this.filePath = filePath;
//        // 1. 获取当前子类的泛型父类（AbstractConfigLoader<E>）
//        Type genericSuperclass = getClass().getGenericSuperclass();
//        if (!(genericSuperclass instanceof ParameterizedType)) {
//            throw new IllegalArgumentException("子类必须声明泛型类型，例如：public class DemoConfigLoader extends AbstractConfigLoader<DemoConfig>");
//        }
//        // 2. 提取泛型参数E的Class对象
//        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
//        this.elementType = (Class<E>) parameterizedType.getActualTypeArguments()[0];
//    }

    /**
     * 初始化加载元数据（路径、来源）
     */
    private void initLoadMetadata() {
        loadMetadata.setFilePath(filePath);
        loadMetadata.setSource(BaseConfigLoadMetadata.ConfigSource.LOCAL_FILE);
        loadMetadata.setLoadStatus(BaseConfigLoadMetadata.LoadStatus.UNLOADED);
    }


    // 辅助方法：从TypeReference中提取泛型E的Class（用于工厂获取转换器）
    private Class<?> getGenericType(TypeReference<List<E>> typeRef) {
        ParameterizedType type = (ParameterizedType) typeRef.getType();
        ParameterizedType listType = (ParameterizedType) type.getActualTypeArguments()[0];
        return (Class<?>) listType.getRawType();
    }

    /**
     * 模板方法：通用加载流程（子类无需重写）
     * 流程：记录时间 → 读取解析YAML → 解析环境变量 → 配置校验 → 转换元数据 → 标记成功
     */
    @Override
    public final void load() {
        try {
            // 1. 初始化加载状态
            loadMetadata.setLoadTime(LocalDateTime.now());
            loadMetadata.setLoadStatus(BaseConfigLoadMetadata.LoadStatus.LOADING);
            log.info("开始加载配置文件：{}", filePath);

            // 2. 加载并解析YAML为配置集合
            List<E> rawConfigs = YamlConfigLoader.loadYamlConfigList(filePath, elementType);
            if (rawConfigs == null) {
                throw new IllegalArgumentException("配置文件解析后为空：" + filePath);
            }

//            // 3. 批量解析环境变量（递归处理所有字段）
//            List<E> resolvedConfigs = parseEnvVariables(rawConfigs);

            // 4. 配置校验（基础校验 + 子类自定义校验）
            doValidate(rawConfigs);

            // 5. 配置转换为元数据（通用逻辑，工厂模式获取转换器）
            List<METADATA> resolvedMetadata = convertToMetadata(rawConfigs);

            // 6. 存储结果（不可变集合，保障数据安全）
//            this.configs = Collections.unmodifiableList(rawConfigs);
            this.strategyMetadataList = Collections.unmodifiableList(resolvedMetadata);

            // 7. 标记加载成功
            loadMetadata.setLoadStatus(BaseConfigLoadMetadata.LoadStatus.SUCCESS);
//            loadMetadata.setConfigCount(resolvedConfigs.size());
            log.info("配置加载成功：{}，共{}条配置，转换元数据{}条",
                    filePath, rawConfigs.size(), resolvedMetadata.size());

        } catch (Exception e) {
            // 异常处理：标记失败 + 记录错误信息
            loadMetadata.setLoadStatus(BaseConfigLoadMetadata.LoadStatus.FAILED);
            loadMetadata.setErrorMessage("配置加载失败：" + e.getMessage());
            log.error("配置文件[{}]加载异常", filePath, e);
            throw new RuntimeException(loadMetadata.getErrorMessage(), e);
        }
    }

    /**
     * 配置校验：基础校验（非空）+ 子类自定义校验
     */
    private void doValidate(List<E> configs) {
        // 基础校验：集合非空 + 元素非空
        if (configs.isEmpty()) {
            throw new IllegalArgumentException("配置集合不能为空：" + filePath);
        }
        for (int i = 0; i < configs.size(); i++) {
            E config = configs.get(i);
            if (config == null) {
                throw new IllegalArgumentException("配置集合第" + (i + 1) + "个元素为null：" + filePath);
            }
            // 调用配置类自身的校验逻辑（BaseConfig的validate方法）
            config.validate();
        }
        // 子类自定义校验（可选扩展点）
        customValidate(configs);
    }

    private List<METADATA> convertToMetadata(List<E> configs) {
        return configs.stream()
                .map(config -> {
                    try {
                        return converter.convert(config);
                    } catch (Exception e) {
                        throw new RuntimeException("配置[" + config.getId() + "]转换失败", e);
                    }
                })
                .collect(Collectors.toList());
    }



    /**
     * 辅助方法：从TypeReference中提取配置类Class（用于工厂获取转换器）
     */
    private Class<?> extractConfigClass(TypeReference<List<E>> typeRef) {
        ParameterizedType listType = (ParameterizedType) typeRef.getType();
        ParameterizedType configType = (ParameterizedType) listType.getActualTypeArguments()[0];
        return (Class<?>) configType.getRawType();
    }

    // ------------------------------ 对外暴露的方法 ------------------------------
    /**
     * 获取加载成功的配置集合（加载失败时抛异常）
     */
//    @Override
//    public List<E> getConfigs() {
//        if (loadMetadata.getLoadStatus() != BaseConfigLoadMetadata.LoadStatus.SUCCESS) {
//            throw new IllegalStateException("配置未加载成功，无法获取：" + filePath);
//        }
//        return configs;
//    }

    /**
     * 获取加载元数据（修复：补充方法体大括号）
     */
    @Override
    public M getLoadMetadata() {
        return loadMetadata;
    }

    /**
     * 获取转换后的元数据集合（修复：LoadMetadata → loadMetadata）
     * 提供元数据集合的获取方法（可选）
     */
    public List<METADATA> getMetadataList() {
        if (loadMetadata.getLoadStatus() != BaseConfigLoadMetadata.LoadStatus.SUCCESS) {
            throw new IllegalStateException("配置未加载成功，无法获取元数据：" + filePath);
        }
        return strategyMetadataList;
    }


    // ------------------------------ 子类扩展点 ------------------------------
    /**
     * 自定义校验（子类按需重写，默认无实现）
     * 用于插件/依赖/许可证的特有校验逻辑
     */
    protected void customValidate(List<E> configs) throws IllegalArgumentException {}

}
