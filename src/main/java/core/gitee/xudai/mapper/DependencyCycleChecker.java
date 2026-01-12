package core.gitee.xudai.mapper;

import core.gitee.xudai.config.loader.b.config.BaseConfig;

import java.util.*;
import java.util.stream.Collectors;

public class DependencyCycleChecker {

    /**
     * 定义节点访问状态
     */
    private enum VisitStatus {

        /**
         * 未访问
         */
        UNVISITED,

        /**
         * 访问中（递归链中）
         */
        VISITING,

        /**
         * 已访问完成
         */
        VISITED

    }

    /**
     * 检测依赖循环的核心方法
     * @param configs
     */
    public void checkDependencyCycle(List<? extends BaseConfig> configs) {
        // 步骤1：构建「配置ID → 依赖ID列表」的映射（过滤空依赖，避免NPE）
        Map<String, List<String>> dependencyMap = configs.stream()
                .collect(Collectors.toMap(
                        BaseConfig::getId,
                        c -> Optional.ofNullable(c.getBaseInfo().getDependencies())
                                .orElse(Collections.emptyList()) // 依赖为空则返回空列表
                                .stream()
                                .filter(Objects::nonNull) // 过滤null的依赖ID
                                .filter(id -> !id.trim().isEmpty()) // 过滤空字符串依赖
                                .collect(Collectors.toList())
                ));

        // 步骤2：初始化节点访问状态
        Map<String, VisitStatus> visitStatusMap = new HashMap<>();
        for (String configId : dependencyMap.keySet()) {
            visitStatusMap.put(configId, VisitStatus.UNVISITED);
        }

        // 步骤3：遍历每个节点，DFS检测循环
        for (String configId : dependencyMap.keySet()) {
            if (visitStatusMap.get(configId) == VisitStatus.UNVISITED) {
                if (hasCycle(configId, dependencyMap, visitStatusMap)) {
                    throw new IllegalStateException("检测到依赖循环：配置ID = " + configId);
                }
            }
        }
    }

    /**
     * 递归 DFS 检测循环
     * @param currentId
     * @param dependencyMap
     * @param visitStatusMap
     * @return
     */
    private boolean hasCycle(String currentId,
                             Map<String, List<String>> dependencyMap,
                             Map<String, VisitStatus> visitStatusMap) {
        // 标记当前节点为「访问中」
        visitStatusMap.put(currentId, VisitStatus.VISITING);

        // 获取当前节点的依赖列表
        List<String> dependencies = dependencyMap.getOrDefault(currentId, Collections.emptyList());
        for (String depId : dependencies) {
            // 若依赖的节点不存在于配置列表中（无效依赖），直接抛异常
            if (!dependencyMap.containsKey(depId)) {
                throw new IllegalArgumentException("配置ID = " + currentId + " 依赖的ID = " + depId + " 不存在！");
            }

            VisitStatus depStatus = visitStatusMap.get(depId);
            if (depStatus == VisitStatus.VISITING) {
                // 遇到「访问中」的节点 → 找到循环
                return true;
            } else if (depStatus == VisitStatus.UNVISITED) {
                // 递归检测依赖节点
                if (hasCycle(depId, dependencyMap, visitStatusMap)) {
                    return true;
                }
            }
            // 若depStatus是VISITED，说明该依赖链已处理完，无需递归
        }

        // 遍历完所有依赖，标记当前节点为「已访问」
        visitStatusMap.put(currentId, VisitStatus.VISITED);
        return false;
    }

    // 模拟BaseConfig（仅为演示，实际用你项目中的类）
//    static class BaseConfig {
//        private String id;
//        private BaseInfo baseInfo;
//
//        public String getId() { return id; }
//        public BaseInfo getBaseInfo() { return baseInfo; }
//
//        static class BaseInfo {
//            private List<String> dependencies;
//            public List<String> getDependencies() { return dependencies; }
//        }
//    }

}
