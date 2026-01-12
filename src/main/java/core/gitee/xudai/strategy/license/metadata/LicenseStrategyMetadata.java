package core.gitee.xudai.strategy.license.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import core.gitee.xudai.strategy.core.enums.StrategyType;
import core.gitee.xudai.strategy.core.metadata.BaseStrategyMetadata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 许可证策略元数据类，用于承载许可证策略的配置信息，符合策略模式的元数据规范
 * 职责：存储许可证策略的唯一标识、基础配置及具体许可证信息，支持策略的动态切换与适配
 * 它继承自 BaseStrategyMetadata<LicenseStrategyMetadata>，表明它的建造者会构建 LicenseStrategyMetadata 类型
 * @author daixu
 */
@Getter
@SuperBuilder(toBuilder = true) // 支持建造者模式及对象复制修改
@NoArgsConstructor(force = true) // 兼容反序列化（如YAML/JSON解析）
public class LicenseStrategyMetadata extends BaseStrategyMetadata<LicenseStrategyMetadata> {

    /**
     * 许可证类型（策略核心配置）
     * 策略执行时，根据此字段进行策略匹配
     */
    private final String licenseType;

    /**
     * 许可证具体信息（策略核心配置）
     * 包含许可证名称、官方链接、分发方式等策略执行所需的关键参数
     */
    @JsonProperty("licenseInfo")
    private final LicenseStrategyInfo licenseStrategyInfo;

    /**
     * 实现父类的抽象方法：明确策略类型为 "license"
     */
    @Override
    public String getStrategyType() {
        return StrategyType.LICENSE.getValue();
    }

    // ------------------------------ 内部类：许可证特有信息 ------------------------------

    /**
     * 许可证策略的核心配置信息类
     * 封装策略执行时依赖的具体参数，与配置文件中的licenseInfo节点一一映射
     */
    @Getter
    @AllArgsConstructor
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor(force = true)
    public static class LicenseStrategyInfo {

        /**
         * 许可证官方全称（如"The Apache Software License, Version 2.0"）
         * 用于策略执行时的合规性校验与文档生成
         */
        private final String name;

        /**
         * 许可证原文官方链接（如https://www.apache.org/licenses/LICENSE-2.0.txt）
         * 策略执行时可通过此链接获取最新许可条款，确保合规性
         */
        private final String url;

        /**
         * 许可证文件分发方式（如"repo"表示随代码仓库分发）
         * 控制策略执行时许可证文件的打包与发布逻辑
         */
        private final String distribution;

    }

}
