package core.gitee.xudai.strategy.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StrategyType {

    DEPENDENCY("dependency"),
    LICENSE("license"),
    PLUGIN("plugin");

    private final String value;

}
