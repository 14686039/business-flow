package com.eking.flow.ast;

import com.eking.flow.execution.ExecutionPlan;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Parallel component representing parallel execution: (A, B, C)
 * or FORK...JOIN pattern
 */
public class ParallelComponent implements FlowComponent {
    private final List<FlowComponent> branches;
    private final String forkNodeId;
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

            // Merge parallel branches
            for (String key : branchPlan.getParallelBranches().keySet()) {
                result.getParallelBranches().put(key, branchPlan.getParallelBranches().get(key));
            }

            // Merge conditional branches
            result.getConditionalBranches().putAll(branchPlan.getConditionalBranches());
        }

        // Register parallel branches from fork node
        if (forkNodeId != null) {
            result.addParallelBranches(forkNodeId, allBranchComponents);
        }

        // Add join node
        if (joinNodeId != null) {
            result.addSequentialComponent(joinNodeId);
        }

        // If no fork node, just add all branch components
        if (forkNodeId == null) {
            for (String comp : allBranchComponents) {
                result.addSequentialComponent(comp);
            }
        }

        return result;
    }
}
