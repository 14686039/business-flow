package com.eking.flow.ast;

import com.eking.flow.execution.ExecutionPlan;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Sequence component representing sequential execution: A -> B -> C
 */
public class SequenceComponent implements FlowComponent {
    private final List<FlowComponent> steps;

    public SequenceComponent(List<FlowComponent> steps) {
        this.steps = steps;
    }

    public List<FlowComponent> getSteps() {
        return steps;
    }

    @Override
    public String toString() {
        return steps.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" -> "));
    }

    @Override
    public ExecutionPlan toExecutionPlan() {
        ExecutionPlan result = new ExecutionPlan();

        for (FlowComponent step : steps) {
            ExecutionPlan stepPlan = step.toExecutionPlan();

            // Merge sequential components
            for (String componentId : stepPlan.getSequentialComponents()) {
                result.addSequentialComponent(componentId);
            }

            // Merge parallel branches
            for (String key : stepPlan.getParallelBranches().keySet()) {
                result.getParallelBranches().put(key, stepPlan.getParallelBranches().get(key));
            }

            // Merge conditional branches
            result.getConditionalBranches().putAll(stepPlan.getConditionalBranches());
        }

        return result;
    }
}
