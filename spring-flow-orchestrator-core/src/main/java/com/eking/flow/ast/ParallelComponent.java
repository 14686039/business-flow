package com.eking.flow.ast;

import com.eking.flow.execution.ExecutionPlan;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * 并行组件表示并行执行：(A, B, C)
 * 或者 FORK...JOIN 模式
 */
public class ParallelComponent implements FlowComponent {
    /**
     * 并行分支组件列表
     */
    private final List<FlowComponent> branches;
    /**
     * 并行分支的FORK节点ID
     */
    private final String forkNodeId;
    /**
     * 并行分支的JOIN节点ID
     */
    private final String joinNodeId;

    public ParallelComponent(List<FlowComponent> branches) {
        this(branches, null, null);
    }

    public ParallelComponent(List<FlowComponent> branches, String forkNodeId, String joinNodeId) {
        this.branches = branches;
        this.forkNodeId = forkNodeId;
        this.joinNodeId = joinNodeId;
    }

    public List<FlowComponent> getBranches() {
        return branches;
    }

    public String getForkNodeId() {
        return forkNodeId;
    }

    public String getJoinNodeId() {
        return joinNodeId;
    }

    @Override
    public String toString() {
        if (forkNodeId != null && joinNodeId != null) {
            return String.format("%s FORK(%s) JOIN %s",
                forkNodeId,
                branches.stream().map(Object::toString).collect(Collectors.joining(", ")),
                joinNodeId);
        }
        return "(" + branches.stream().map(Object::toString).collect(Collectors.joining(", ")) + ")";
    }

    @Override
    public ExecutionPlan toExecutionPlan() {
        ExecutionPlan result = new ExecutionPlan();

        List<String> allBranchComponents = new ArrayList<>();

        // Convert all branches
        for (FlowComponent branch : branches) {
            ExecutionPlan branchPlan = branch.toExecutionPlan();
            allBranchComponents.addAll(branchPlan.getSequentialComponents());

            // 合并并行分支
            for (String key : branchPlan.getParallelBranches().keySet()) {
                result.getParallelBranches().put(key, branchPlan.getParallelBranches().get(key));
            }

            // 合并条件分支
            result.getConditionalBranches().putAll(branchPlan.getConditionalBranches());
        }

        // 注册并行分支
        if (forkNodeId != null) {
            result.addParallelBranches(forkNodeId, allBranchComponents);
        }

        // 添加JOIN节点
        if (joinNodeId != null) {
            result.addSequentialComponent(joinNodeId);
        }

        // 如果没有FORK节点，直接添加所有分支组件
        if (forkNodeId == null) {
            for (String comp : allBranchComponents) {
                result.addSequentialComponent(comp);
            }
        }

        return result;
    }
}
