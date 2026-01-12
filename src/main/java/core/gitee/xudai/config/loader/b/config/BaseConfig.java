package core.gitee.xudai.config.loader.b.config;

import core.gitee.xudai.strategy.core.metadata.BaseStrategyMetadata;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

/**
 * YAML 配置项基类（抽取共通属性）
 * @author daixu
 */
@Data
@Slf4j
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseConfig <T extends BaseStrategyMetadata<T>> {

    /** 唯一标识 */
    @NotNull(message = "id cannot be null")
    protected String id;

    /** 展示名称 */
    @NotNull(message = "title cannot be null")
    protected String title;

    /** 序号 */
    @NotNull(message = "serialNumber cannot be null")
    protected Integer serialNumber;

    @JsonProperty("baseInfo")
    @NotNull(message = "baseInfo cannot be null")
    protected BaseInfo baseInfo;

    /**
     * 加载配置时（如YAML解析后），使用 Validator 校验，
     */
    private static final Validator validator;

    /**
     * 静态 Validator 实例，避免重复创建
     */
    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * 校验模板方法（final 不让子类绕过去）
     * 统一校验逻辑，校验配置的合法性：校验当前配置的所有约束注解
     * 父类负责校验公共属性，子类可重写此方法添加特有属性的校验。
     * 子类可重写此方法添加特有校验。
     * 1. 先执行 JSR 303 注解校验（公共属性）
     * 2. 调用钩子方法执行子类特有校验
     */
    public final void validate() {

        // 1. 基础注解校验（公共属性）
        // 校验当前对象（即子类实例）的所有约束注解
        Set<ConstraintViolation<BaseConfig>> violations = validator.validate(this);

        if (!violations.isEmpty()) {
            StringBuilder errorMsg = new StringBuilder("配置校验失败 (ID: " + this.id + "): ");
            violations.forEach(v -> errorMsg
                    .append(" ")
                    .append("; ")
                    .append(v.getMessage())
                    .append(v.getPropertyPath())
            );
            throw new IllegalArgumentException(errorMsg.toString().trim());
        }

        // 2. 执行子类额外校验（钩子方法）子类扩展点（默认空，可不重写）
        extraValidation();

    }

    /** 校验钩子：子类扩展点 */
    protected void extraValidation() {}

    /**
     * 基础信息类：封装所有配置的公共基础属性
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BaseInfo {

        /** 默认顺序为 0 */
        protected Integer order = 0;

        /** 默认启用 */
        protected Boolean enabled = true;

        /** 默认非必需 */
        protected Boolean required = false;

        /** 描述 */
        protected String description;

        /** 依赖项 */
        protected List<String> dependencies = Collections.emptyList();

    }

}
