package core.gitee.xudai.config.loader;

import core.gitee.xudai.config.loader.b.config.BaseConfig;
import core.gitee.xudai.strategy.core.metadata.BaseStrategyMetadata;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

/**
 * 配置转换工具类：统一处理 BaseConfig 子类到 BaseStrategyMetadata 子类的转换逻辑
 * 核心价值：复用公共转换流程，减少重复代码，提升可维护性
 * 泛型说明：
 * - C: 配置类类型（需继承 BaseConfig）
 * - M: 元数据类类型（需继承 BaseStrategyMetadata）
 * - CI: 配置类中的内部信息类（如 LicenseConfig.LicenseInfo）
 * - MI: 元数据类中的内部信息类（如 LicenseStrategyMetadata.LicenseStrategyInfo）
 */
@Slf4j
public final class ConfigConverterUtils2221 {

    // 日志模板常量
    private static final String WARN_CONFIG_INFO_NULL = "配置转换警告：配置ID={}, 内部信息为null，跳过特有属性转换";
    private static final String INFO_CONVERT_SUCCESS = "配置转换成功：配置ID={}, 元数据ID={}";

    /**
     * 私有构造器：工具类禁止实例化
     */
    private ConfigConverterUtils2221() {
        // 工具类不能实例化
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * 通用配置转换方法：将配置类转换为对应的元数据类
     *
     * @param config                配置类实例（如 LicenseConfig、DependencyConfig）
     * @param metadataBuilderSupplier 元数据建造者提供器（如 LicenseStrategyMetadata::builder）
     * @param configInfoExtractor   配置内部信息提取器（从配置类中获取内部信息，如 LicenseConfig::getLicenseInfo）
     * @param configInfoToMetaConverter 配置内部信息转元数据内部信息的转换器
     *                                 （如 LicenseConfig.LicenseInfo -> LicenseStrategyMetadata.LicenseStrategyInfo）
     * @param metaInfoSetter        元数据内部信息设置器（将转换后的内部信息设置到元数据建造者中）
     * @return 转换后的元数据类实例（如 LicenseStrategyMetadata）
     * @throws IllegalArgumentException 若配置或关键组件为空
     */
    /**
     * 通用配置转换方法
     *
     * @param config                        配置类实例
     * @param metadataBuilderSupplier       元数据建造者提供器
     * @param configInfoExtractor           配置内部信息提取器
     * @param configInfoToMetaConverter     配置内部信息转元数据内部信息的转换器
     * @param metaInfoSetter                元数据内部信息设置器
     * @param <C>                           配置类类型
     * @param <M>                           元数据类类型
     * @param <B>                           元数据建造者类型
     * @param <CI>                          配置内部信息类型
     * @param <MI>                          元数据内部信息类型
     * @return 转换后的元数据类实例
     */
    /**
     * 简化泛型但保留核心类型约束
     *
     * @param <C>  配置类类型
     * @param <M>  元数据类型
     * @param <CI> 配置内部信息类型
     * @param <MI> 元数据内部信息类型
     */
    public static <
            C extends BaseConfig,
            M extends BaseStrategyMetadata<M>,
            B extends BaseStrategyMetadata.BaseStrategyMetadataBuilder<M, B>,
            CI,
            MI
            > M convert(
            C config,
            Supplier<B> metadataBuilderSupplier,
            Function<C, CI> configInfoExtractor,
            Function<CI, MI> infoConverter,
            BiConsumer<B, MI> metaInfoSetter
    ) {

        // 校验入参
        validateInput(config, metadataBuilderSupplier, configInfoExtractor, infoConverter, metaInfoSetter);

        // 获取元数据建造者
        B builder = metadataBuilderSupplier.get();
        if (builder == null) {
            throw new IllegalStateException("元数据建造者获取失败（配置ID：" + config.getId() + "）");
        }

        // 1. 转换公共属性（调用工具类内部方法）
        fillBaseProperties(config, builder);

        // 2. 转换子类特有属性
        CI configInfo = configInfoExtractor.apply(config);
        if (configInfo != null) {
            MI metaInfo = infoConverter.apply(configInfo);
            if (metaInfo == null) {
                log.warn("配置内部信息转换后为null（配置ID：{}），跳过设置", config.getId());
                throw new IllegalStateException("配置内部信息转换失败（配置ID：" + config.getId() + "）");
            }
            metaInfoSetter.accept(builder, metaInfo);
        } else {
            log.warn("配置内部信息为null（配置ID：{}）", config.getId());
        }

        // 构建并返回元数据
        M metadata = builder.build();
        log.info("配置转换成功（配置ID：{} -> 元数据ID：{}）", config.getId(), metadata.getStrategyId());
        return metadata;
    }

    /**
     * 通用配置转换方法：将配置类转换为对应的元数据类
     *
     * @param config                        配置类实例
     * @param metadataBuilderSupplier       元数据建造者提供器
     * @param configInfoExtractor           配置内部信息提取器
     * @param configInfoToMetaConverter     配置内部信息转元数据内部信息的转换器
     * @param metaInfoSetter                元数据内部信息设置器
     * @param <C>                           配置类类型
     * @param <M>                           元数据类类型
     * @param <B>                           元数据建造者类型
     * @param <CI>                          配置内部信息类型
     * @param <MI>                          元数据内部信息类型
     * @return 转换后的元数据类实例
     */
//    public static <
//            C extends BaseConfig,
//            M extends BaseStrategyMetadata<M>,
//            B extends BaseStrategyMetadata.BaseStrategyMetadataBuilder<M, B>,
//            CI,
//            MI
//            > M convert(
//            C config,
//            Supplier<B> metadataBuilderSupplier,
//            Function<C, CI> configInfoExtractor,
//            Function<CI, MI> configInfoToMetaConverter,
//            BiConsumer<B, MI> metaInfoSetter
//    ) {
//        validateInput(config, metadataBuilderSupplier, configInfoExtractor, configInfoToMetaConverter, metaInfoSetter);
//
//        B metadataBuilder = metadataBuilderSupplier.get();
//        if (metadataBuilder == null) {
//            throw new IllegalStateException("Failed to get metadata builder from supplier");
//        }
//
//        // 调用父类方法填充公共属性
//        config.convertBaseMetadata(metadataBuilder);
//        log.debug("Filled common properties for config: {}", config.getId());
//
//        // 处理内部信息
//        CI configInfo = configInfoExtractor.apply(config);
//        if (configInfo != null) {
//            MI metaInfo = configInfoToMetaConverter.apply(configInfo);
//            metaInfoSetter.accept(metadataBuilder, metaInfo);
//            log.debug("Converted and set config info for config: {}", config.getId());
//        } else {
//            log.warn("Config info is null for config: {}", config.getId());
//        }
//
//        M metadata = metadataBuilder.build();
//        log.info("Successfully converted config [{}] to metadata [{}]", config.getId(), metadata.getStrategyId());
//        return metadata;
//    }

    /**
     * 填充基础属性（补全strategyId）
     */
    /**
     * 填充公共属性到元数据建造者
     * （核心公共转换逻辑：BaseConfig -> BaseStrategyMetadata的公共属性）
     */
    static <M extends BaseStrategyMetadata<M>, B extends BaseStrategyMetadata.BaseStrategyMetadataBuilder<M, B>>
    void fillBaseProperties(BaseConfig config, B builder) {
        // 校验baseInfo非空
        BaseConfig.BaseInfo baseInfo = config.getBaseInfo();
        if (baseInfo == null) {
            throw new IllegalArgumentException("baseInfo不能为空（配置ID：" + config.getId() + "）");
        }

        // 转换baseInfo到元数据的baseStrategyInfo
        BaseStrategyMetadata.BaseStrategyInfo baseStrategyInfo = BaseStrategyMetadata.BaseStrategyInfo.builder()
                .order(baseInfo.getOrder())
                .enabled(baseInfo.getEnabled())
                .required(baseInfo.getRequired())
                .description(baseInfo.getDescription())
                .dependencies(baseInfo.getDependencies())
                .build();

        // 填充公共属性到建造者
        builder.strategyId("strategy-" + config.getId()) // 策略唯一标识
                .id(config.getId()) // 配置ID
                .title(config.getTitle()) // 展示名称
                .serialNumber(config.getSerialNumber()) // 序号
                .baseStrategyInfo(baseStrategyInfo); // 基础策略信息
    }

    /**
     * 入参合法性校验
     */
    private static void validateInput(
            BaseConfig config,
            Supplier<?> builderSupplier,
            Function<?, ?> configInfoExtractor,
            Function<?, ?> infoConverter,
            BiConsumer<?, ?> infoSetter) {

        if (config == null) {
            throw new IllegalArgumentException("配置对象（config）不能为null");
        }
        if (builderSupplier == null) {
            throw new IllegalArgumentException("元数据建造者提供器（builderSupplier）不能为null");
        }
        if (configInfoExtractor == null) {
            throw new IllegalArgumentException("配置信息提取器（configInfoExtractor）不能为null");
        }
        if (infoConverter == null) {
            throw new IllegalArgumentException("信息转换器（infoConverter）不能为null");
        }
        if (infoSetter == null) {
            throw new IllegalArgumentException("元数据信息设置器（infoSetter）不能为null");
        }
    }

}
