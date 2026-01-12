package core.gitee.xudai.config;

import core.gitee.xudai.enums.MavenScope;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collections;
import java.util.List;

/**
 * 校验器：maven 作用域范围是否合规校验
 * @author daixu
 */
public class ScopeValidator implements ConstraintValidator<ValidScope, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || MavenScope.contains(value);
    }

}
