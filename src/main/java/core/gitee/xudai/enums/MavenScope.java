package core.gitee.xudai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum MavenScope {

    COMPILE(true,  "默认"),
    TEST   (false, "仅测试"),
    RUNTIME(true,  "运行期"),
    PROVIDED(false,"由容器提供"),
    SYSTEM (false,"本地系统"),
    IMPORT (false,"依赖导入");

    private final boolean transitive;

    private final String desc;

    public static boolean contains(String value) {
        return Arrays.stream(values())
                .anyMatch(s -> s.name().equalsIgnoreCase(value));
    }

}
