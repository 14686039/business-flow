package com.eking.flow.ast;

import com.eking.flow.execution.ExecutionPlan;

import java.util.List;

/**
 * Conditional component representing ternary expression: A ? B : C
 */
public class ConditionalComponent implements FlowComponent {
    private final FlowComponent condition;
    private final FlowComponent trueBranch;
    private final FlowComponent falseBranch;

    public ConditionalComponent(FlowComponent condition, FlowComponent trueBranch, FlowComponent falseBranch) {
        this.condition = condition;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
    }

    public FlowComponent getCondition() {
        return condition;
    }

    public FlowComponent getTrueBranch() {
        return trueBranch;
    }

    public FlowComponent getFalseBranch() {
        return falseBranch;
    }

    @Override
    public String toString() {
        return String.format("%s ? %s : %s", condition, trueBranch, falseBranch);
    }

    @Override
    public ExecutionPlan toExecutionPlan() {
        ExecutionPlan result = new ExecutionPlan();

        // Convert condition to plan
        ExecutionPlan conditionPlan = condition.toExecutionPlan();
        List<String> conditionComponents = conditionPlan.getSequentialComponents();

        if (!conditionComponents.isEmpty()) {
            String routerId = conditionComponents.get(conditionComponents.size() - 1);

            // Convert true branch
            ExecutionPlan truePlan = trueBranch.toExecutionPlan();
            List<String> trueComponents = truePlan.getSequentialComponents();
            if (!trueComponents.isEmpty()) {
                String trueBranchId = trueComponents.get(0);
                result.addConditionalBranch(routerId, "true", trueBranchId);
            }

            // Convert false branch
            ExecutionPlan falsePlan = falseBranch.toExecutionPlan();
            List<String> falseComponents = falsePlan.getSequentialComponents();
            if (!falseComponents.isEmpty()) {
                String falseBranchId = falseComponents.get(0);
                result.addConditionalBranch(routerId, "false", falseBranchId);
            }

            // Add all components from condition, true branch, and false branch
            for (String comp : conditionComponents) {
                result.addSequentialComponent(comp);
            }
            for (String comp : trueComponents) {
                result.addSequentialComponent(comp);
            }
            for (String comp : falseComponents) {
                result.addSequentialComponent(comp);
            }

            // Merge parallel branches from condition, true, and false plans
            result.getParallelBranches().putAll(conditionPlan.getParallelBranches());
            result.getParallelBranches().putAll(truePlan.getParallelBranches());
            result.getParallelBranches().putAll(falsePlan.getParallelBranches());
        }

        return result;
    }
}
