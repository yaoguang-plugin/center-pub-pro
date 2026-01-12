package core.gitee.xudai.config.loader.b.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import core.gitee.xudai.config.loader.b.util.JacksonYamlUtils;
import core.gitee.xudai.config.loader.b.util.PlaceholderUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;

public class YamlConfigLoader {

    /**
     * 从类路径加载 YAML 并绑定到目标类
     *
     * @param yamlPath 类路径下的 YAML 文件路径
     * @param clazz    目标类类型
     * @param <T>      目标类型泛型
     * @return 绑定后的实例
     * @throws IOException 加载或解析失败时抛出
     */
    public static <T> T loadYamlConfig(String yamlPath, Class<T> clazz) throws IOException {

        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(yamlPath)) {
            if (in == null) {
                throw new IOException("配置文件不存在：" + yamlPath);
            }
            T raw = JacksonYamlUtils.readValue(in, clazz); // 调用底层工具
            if (raw == null) {
                throw new IOException("YAML 文件内容为空：" + yamlPath);
            }
            return PlaceholderUtils.resolveEnvVariables(raw); // 配置专属的环境变量解析
        } catch (MismatchedInputException e) {
            // 类型不匹配、字段错误等解析异常
            throw new IOException("YAML 内容与目标类不匹配：" + clazz.getName() + "，错误字段：" + e.getPathReference(), e);
        } catch (IOException e) {
            throw new UncheckedIOException("加载配置失败：" + clazz, e);
        }

    }

    /**
     * 加载 YAML 文件并解析为指定元素类型的 List 集合（自动解析环境变量占位符）
     * @param yamlPath 类路径下的 YAML 文件路径（如 "config/plugins.yaml"）
     * @param elementType List 中元素的类型（如 PluginConfig.class）
     * @return 解析完成且替换完环境变量的 List 集合
     * @throws UncheckedIOException 所有异常统一包装为非检查型，简化调用方
     */
    /**
     * 读取 classpath 下的 YAML 数组，转换成 List<E>，并完成 ${} 替换
     *
     * @param yamlPath    classpath 相对路径，例如 "configs/demo.yml"
     * @param elementType 数组元素类型，例如 DemoConfig.class
     * @param <E>         元素泛型
     * @return 已解析且已替换环境变量的列表
     * @throws UncheckedIOException 任何 IO / 解析失败都转非受检异常
     */
    public static <E> List<E> loadYamlConfigList(String yamlPath, Class<E> elementType) {

        try (InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(yamlPath)) {
            if (in == null) {
                throw new IOException("配置文件不存在：" + yamlPath);
            }

            /* 2. 纯解析 */
            List<E> rawList = JacksonYamlUtils.readValueList(in, elementType);
            if (rawList == null || rawList.isEmpty()) {
                throw new IOException("YAML 文件内容为空：" + yamlPath);
            }

            /* 3. 递归替换 ${} */
            return PlaceholderUtils.resolveEnvVariables(rawList);

        } catch (MismatchedInputException e) {
            // 优先捕获子类异常：类型不匹配（如字段类型错误、结构不匹配）
            throw new UncheckedIOException(
                    String.format("YAML 内容与 List<%s> 类型不匹配，错误字段：%s，文件路径：%s",
                            elementType.getName(), e.getPathReference(), yamlPath),
                    e
            );
        } catch (IOException e) {
            // 捕获父类异常：文件不存在、流读取失败等 IO 异常
            throw new UncheckedIOException(
                    String.format("加载 List 配置失败（元素类型：%s），文件路径：%s，原因：%s",
                            elementType.getName(), yamlPath, e.getMessage()),
                    e
            );
        }
    }

}
