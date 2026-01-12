package core.gitee.xudai.config;

import jakarta.validation.Constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义注解，校验作用域
 * @author daixu
 */
@Target({FIELD, METHOD})
@Retention(RUNTIME)
@Constraint(validatedBy = ScopeValidator.class)
public @interface ValidScope {

}
