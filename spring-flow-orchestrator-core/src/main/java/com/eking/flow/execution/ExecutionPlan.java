package com.eking.flow.execution;

import java.util.*;

/**
 * Represents the execution plan for a flow.
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

    public void addSequentialComponent(String componentId) {
        sequentialComponents.add(componentId);
    }

    public void addConditionalBranch(String routerId, String condition, String targetComponentId) {
        conditionalBranches.computeIfAbsent(routerId, k -> new ConditionalBranch())
                           .addBranch(condition, targetComponentId);
    }

    public void addParallelBranches(String forkNodeId, List<String> branchComponentIds) {
        parallelBranches.put(forkNodeId, branchComponentIds);
    }

    public List<String> getSequentialComponents() {
        return sequentialComponents;
    }

    public Map<String, ConditionalBranch> getConditionalBranches() {
        return conditionalBranches;
    }

    public Map<String, List<String>> getParallelBranches() {
        return parallelBranches;
    }

    /**
     * Get parallel branches for a fork node
     */
    public List<String> getParallelBranches(String forkNodeId) {
        return parallelBranches.get(forkNodeId);
    }

    /**
     * Get conditional branches for a router node
     */
    public ConditionalBranch getConditionalBranches(String routerId) {
        return conditionalBranches.get(routerId);
    }

    /**
     * Represents a conditional branch from a routing component
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
