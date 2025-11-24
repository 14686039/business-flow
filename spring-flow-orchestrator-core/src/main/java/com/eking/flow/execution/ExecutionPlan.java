package com.eking.flow.execution;

import java.util.*;

/**
 * 一个流程的执行计划
 */
public class ExecutionPlan {

    private List<String> sequentialComponents;
    private Map<String, ConditionalBranch> conditionalBranches;
    private Map<String, List<String>> parallelBranches; // fork node -> list of branch components

    public ExecutionPlan() {
        this.sequentialComponents = new ArrayList<>();
        this.conditionalBranches = new HashMap<>();
        this.parallelBranches = new HashMap<>();
    }
    /**
     * 添加一个顺序组件到执行计划中。
     * @param componentId 组件ID
     */
    public void addSequentialComponent(String componentId) {
        sequentialComponents.add(componentId);
    }
    /**
     * 添加一个条件分支到执行计划中。
     * @param routerId 路由组件ID
     * @param condition 条件表达式
     * @param targetComponentId 目标组件ID
     */
    public void addConditionalBranch(String routerId, String condition, String targetComponentId) {
        conditionalBranches.computeIfAbsent(routerId, k -> new ConditionalBranch())
                           .addBranch(condition, targetComponentId);
    }
    /**
     * 添加一个并行分支到执行计划中。
     * @param forkNodeId 分岔节点ID
     * @param branchComponentIds 并行分支组件ID列表
     */
    public void addParallelBranches(String forkNodeId, List<String> branchComponentIds) {
        parallelBranches.put(forkNodeId, branchComponentIds);
    }
    /**
     * 获取顺序组件列表。
     * @return 顺序组件列表
     */
    public List<String> getSequentialComponents() {
        return sequentialComponents;
    }
    /**
     * 获取路由组件的条件分支。
     * @return 路由组件的条件分支
     */
    public Map<String, ConditionalBranch> getConditionalBranches() {
        return conditionalBranches;
    }

    /**
     * 获取并行分支映射。
     * @return 并行分支映射
     */
    public Map<String, List<String>> getParallelBranches() {
        return parallelBranches;
    }

    /**
     * 获取一个分岔节点的并行分支。
     * @param forkNodeId 分岔节点ID
     * @return 分岔节点的并行分支
     */
    public List<String> getParallelBranches(String forkNodeId) {
        return parallelBranches.get(forkNodeId);
    }

    /**
     * 获取路由组件的条件分支。
     * @param routerId 路由组件ID
     * @return 路由组件的条件分支
     */
    public ConditionalBranch getConditionalBranches(String routerId) {
        return conditionalBranches.get(routerId);
    }

    /**
     * 表示一个路由组件的条件分支。
     */
    public static class ConditionalBranch {
        private Map<String, String> conditionMap; // condition -> target component

        public ConditionalBranch() {
            this.conditionMap = new HashMap<>();
        }

        public void addBranch(String condition, String targetComponentId) {
            conditionMap.put(condition, targetComponentId);
        }

        public String getTargetComponent(String condition) {
            return conditionMap.get(condition);
        }

        public Map<String, String> getAllBranches() {
            return new HashMap<>(conditionMap);
        }
    }

    @Override
    public String toString() {
        return "ExecutionPlan{" +
                "sequential=" + sequentialComponents +
                ", conditional=" + conditionalBranches.keySet() +
                ", parallel=" + parallelBranches +
                '}';
    }
}
