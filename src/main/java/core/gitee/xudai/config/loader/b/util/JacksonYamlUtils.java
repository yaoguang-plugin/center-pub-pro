package core.gitee.xudai.config.loader.b.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Jackson YAML 工具类：提供线程安全的 YAML 序列化/反序列化功能
 * 统一配置 YAML 解析特性，支持 Java 8 时间类型、参数名称解析等常用功能
 */
@Slf4j
public final class JacksonYamlUtils {

    /**
     * 全局默认的 YAML ObjectMapper 实例（线程安全）
     * 采用饿汉式单例初始化，确保类加载时完成配置
     */
    private static final ObjectMapper YAML_MAPPER;

    static {
        // true=开启“打印时人类友好”
        YAML_MAPPER = configureYamlMapper();
        /* 预热：让 JVM 提前加载所有序列化器，避免第一次请求抖动 */
        try {
            YAML_MAPPER.writeValueAsString(new Object());
        } catch (IOException ignore) {}
        log.info("JacksonYamlUtils 初始化完成，Mapper hash={}", YAML_MAPPER.hashCode());
    }

    /**
     * 获取默认配置的 YAML ObjectMapper 实例
     * @return 配置完成的 ObjectMapper
     */
    public static ObjectMapper getDefaultYamlMapper() {
        return YAML_MAPPER;
    }

    // 是否美化换行（默认开启）TODO 可以作为参数暴露在外面
    private static final boolean PRETTY = true;

    /**
     * 私有构造器，防止实例化工具类
     */
    private JacksonYamlUtils() {
        throw new AssertionError("工具类不允许实例化");
    }

    /**
     * 核心配置方法：统一配置 YAML 解析特性
     * @return 配置完成的 ObjectMapper
     */
    private static ObjectMapper configureYamlMapper() {

        /* 工厂层：关闭 YAML 特有的 --- 分隔符更清爽 */
        YAMLFactory yamlFactory = YAMLFactory.builder()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .build();

        ObjectMapper mapper = new ObjectMapper(yamlFactory);

        // 1. 反序列化特性配置
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 忽略未知字段
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true); // 空数组视为 null
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true); // 未知枚举值视为 null
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true); // 单值视为数组
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false); // 基本类型允许 null

        // 2. 序列化特性配置
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false); // 允许序列化空对象
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // 日期以字符串形式输出
        mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true); // 转义非 ASCII 字符

        // 3. 通用解析特性
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true); // 允许非引号字段名
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true); // 允许单引号
        mapper.configure(MapperFeature.ALLOW_COERCION_OF_SCALARS, true); // 允许标量类型 coercion

        /* 2.2 解析层：只开必要特性，避免攻击面 */
        mapper.configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION.mappedFeature(), true); // 异常带行号
        // 就是“是否美化换行”开关；
        mapper.configure(SerializationFeature.INDENT_OUTPUT, PRETTY);

        /* 2.3 序列化层：空值、null、空集合统一不输出，减少文件体积 */
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        /* 2.5 全局命名策略：蛇形 ↔ 小驼峰自动互转（可选） */
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);

        // 4. 日期时间配置
        mapper.registerModule(new JavaTimeModule()); // 支持 Java 8 时间类型
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 设置时区为东八区
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")); // 统一日期格式
        /* 2.4 模块层：JDK8 时间 + 参数名模块（支持无参构造） */
        // 5. 支持构造函数参数名称解析（需 Java 8+ 参数名称反射支持）
        mapper.registerModule(new ParameterNamesModule());

        return mapper;
    }

    /* ========================= 3. 对外提供的方法 ========================= */

    // 基础方法：从输入流读取 YAML 为对象
    public static <T> T readValue(InputStream in, Class<T> clazz) throws IOException {
        return YAML_MAPPER.readValue(in, clazz);
    }

    // 基础方法：从输入流读取 YAML 为 List
    public static <T> List<T> readValue(InputStream in, TypeReference<List<T>> typeRef) throws IOException {
        return YAML_MAPPER.readValue(in, typeRef);
    }

    // 基础方法：从输入流读取 YAML 为 List
    public static <T> List<T> readValueList(InputStream in, Class<T> clazz) throws IOException {
        CollectionType listType = JacksonYamlUtils.YAML_MAPPER.getTypeFactory()
                .constructCollectionType(List.class, clazz);
        return YAML_MAPPER.readValue(in, listType);
    }

    /* ========================= 3. 常用模板方法 ========================= */

    /** 3.1 读取类路径 YAML → 单体对象（已解析环境变量） */
//    public static <T> T load(String classpath, Class<T> clazz) {
//        try (InputStream in = Thread.currentThread().getContextClassLoader()
//                .getResourceAsStream(classpath)) {
//            if (in == null) throw new IOException("classpath 资源不存在: " + classpath);
//            T raw = YAML_MAPPER.readValue(in, clazz);
//            return resolveEnvVariables(raw);          // 统一做 ${} 替换
//        } catch (IOException e) {
//            throw new UncheckedIOException("加载 YAML 失败: " + classpath, e);
//        }
//    }

    /** 3.2 读取类路径 YAML → List（已解析环境变量） */
//    public static <T> List<T> loadList(String classpath, TypeReference<List<T>> typeRef) {
//        try (InputStream in = Thread.currentThread().getContextClassLoader()
//                .getResourceAsStream(classpath)) {
//            if (in == null) throw new IOException("classpath 资源不存在: " + classpath);
//            List<T> raw = YAML_MAPPER.readValue(in, typeRef);
//            return resolveEnvVariables(raw);
//        } catch (IOException e) {
//            throw new UncheckedIOException("加载 YAML 列表失败: " + classpath, e);
//        }
//    }

    /** 3.3 对象 → YAML 字符串 */
    public static String toYaml(Object obj) {
        try {
            return YAML_MAPPER.writeValueAsString(obj);
        } catch (IOException e) {
            throw new UncheckedIOException("对象转 YAML 失败", e);
        }
    }

}
