package core.gitee.xudai.config.loader.b.util;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderUtils {

    /**
     * 环境变量占位符正则表达式
     * 格式：${key:defaultValue}
     * 支持默认值，如 ${key:default} 或 ${key}（无默认值时为空字符串）
     */
    private static final Pattern ENV_PLACEHOLDER = Pattern.compile("\\$\\{([^:}]+)(?::([^}]*))?}");

    /* ========================= 4. 环境变量递归解析 ========================= */

    @SuppressWarnings("unchecked")
    public static <T> T resolveEnvVariables(T obj) {

        if (obj == null) {
            return null;
        }

        Class<?> clz = obj.getClass();

        /* 基础类型短路 */
        if (obj instanceof String) {
            return (T) resolveString((String) obj);
        }

        if (clz.isPrimitive() || obj instanceof Number || obj instanceof Boolean) {
            return obj;
        }

        /* 集合 */
        if (obj instanceof Collection<?>) {
            Collection<Object> tmp = new ArrayList<>();
            for (Object o : (Collection<?>) obj) tmp.add(resolveEnvVariables(o));
            return (T) tmp;
        }
        /* Map */
        if (obj instanceof Map<?, ?>) {
            Map<Object, Object> tmp = new HashMap<>();
            ((Map<?, ?>) obj).forEach((k, v) ->
                    tmp.put(resolveEnvVariables(k), resolveEnvVariables(v)));
            return (T) tmp;
        }
        /* 自定义对象：反射字段 */
        try {
            for (Field f : getAllFields(clz)) {
                f.setAccessible(true);
                Object fv = f.get(obj);
                f.set(obj, resolveEnvVariables(fv));
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("反射解析环境变量失败", e);
        }
        return obj;
    }

    private static String resolveString(String val) {
        if (val == null) return null;
        Matcher m = ENV_PLACEHOLDER.matcher(val);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String key = m.group(1).trim();
            String def = m.group(2) != null ? m.group(2) : "";
            String real = System.getenv(key);
            m.appendReplacement(sb, Matcher.quoteReplacement(real != null ? real : def));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

}
