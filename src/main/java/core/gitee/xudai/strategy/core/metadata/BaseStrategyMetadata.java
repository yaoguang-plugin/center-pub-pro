package core.gitee.xudai.strategy.core.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 策略元数据基类（抽取策略元数据的公共属性）
 * 所有策略元数据的基础父类
 *  @param <M> 最终要构建的元数据类型（如 LicenseStrategyMetadata）
 * @author daixu
 */
@Getter
@SuperBuilder(toBuilder = true)  // 支持建造者模式，子类可继承并扩展
@AllArgsConstructor  // 全参构造器，用于初始化 final 成员变量
@NoArgsConstructor(force = true)  // 无参构造器（Jackson反序列化用），force=true强制初始化final变量为默认值
public abstract class BaseStrategyMetadata<M extends BaseStrategyMetadata<M>> implements StrategyMetadata {

    /** 唯一标识（策略的唯一ID） */
    private final String id;

    /** 展示名称（用于直观识别策略） */
    private final String title;

    /** 序号（用于排序或标识） */
    private final Integer serialNumber;

    /** 策略基础信息（包含执行顺序、依赖等） */
    @JsonProperty("baseInfo")  // 与YAML中的baseInfo字段映射
    private final BaseStrategyInfo baseStrategyInfo;

    private final String strategyId;
    private final String strategyType;

    /**
     * 泛型建造者，明确它将用于构建类型 M 的对象
     * @param <M> 要构建的元数据类型
     * @param <B> 建造者自身的类型
     */
    @SuppressWarnings("unchecked")
    public abstract static class BaseStrategyMetadataBuilder<
            M extends BaseStrategyMetadata<M>,
            B extends BaseStrategyMetadataBuilder<M, B>
            > {
        // Lombok 会自动生成 strategyId, strategyType 等字段的设置方法

        /**
         * 关键：重写 build() 方法的返回类型为 M
         * 这让编译器在调用 build() 时能明确知道返回的是 M 类型
         * @return 构建好的元数据对象
         */
        public abstract M build();

        // 为了链式调用能正确返回子类建造者，Lombok 会处理，但这里我们声明一下
        protected abstract B self();
    }

    // ------------------------------ 实现 StrategyMetadata 接口 ------------------------------

    /**
     * 抽象方法：获取策略类型（由子类实现，返回具体类型如 "license"、"plugin"、"dependency" ）
     * 强制子类明确自身类型，避免策略工厂无法识别
     */
    @Override
    public abstract String getStrategyType();

    /**
     * 通用实现：判断策略是否可执行
     * 统一逻辑：基础信息非空 + enabled=true 时可执行
     * 子类可按需重写（如特殊策略的执行条件）
     */
//    @Override
//    public boolean isExecutable() {
//        // 基础信息为空时，默认不可执行
//        if (baseStrategyInfo == null) {
//            return false;
//        }
//        // enabled为true时可执行（处理null安全：默认false）
//        Boolean enabled = baseStrategyInfo.getEnabled();
//        return enabled != null && enabled;
//    }

    /**
     * 公共方法：判断是否可执行
     */
    @Override
    public boolean isExecutable() {
        return baseStrategyInfo != null && Boolean.TRUE.equals(baseStrategyInfo.getEnabled());
    }

    // ------------------------------ 内部类：策略基础信息 ------------------------------

    /**
     * 策略基础信息元数据类（封装策略的通用执行属性）
     */
    @Getter
    @SuperBuilder(toBuilder = true)  // 支持建造者模式
    @AllArgsConstructor  // 全参构造器
    @NoArgsConstructor(force = true)  // 无参构造器（Jackson反序列化用）
    public static class BaseStrategyInfo {

        /** 执行顺序（数字越小越先执行） */
        private final Integer order;

        /** 是否启用（false则跳过该策略） */
        private final Boolean enabled;

        /** 是否必需（true则禁用会报错） */
        private final Boolean required;

        /** 策略描述 */
        private final String description;

        /** 依赖的策略ID（被依赖策略优先执行） */
        private final List<String> dependencies;

    }

}
