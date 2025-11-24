package com.eking.flow.ast;

import com.eking.flow.execution.ExecutionPlan;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 顺序组件
 * 顺序组件表示顺序执行：A -> B -> C
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
    /**
     * 转换为字符串表示
     * 顺序组件的字符串表示为：A -> B -> C
     */
    public String toString() {
        return steps.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" -> "));
    }
    /**
     * 转换为执行计划格式
     * 顺序组件的执行计划表示为：A -> B -> C
     */
    @Override
    public ExecutionPlan toExecutionPlan() {
        ExecutionPlan result = new ExecutionPlan();

        for (FlowComponent step : steps) {
            ExecutionPlan stepPlan = step.toExecutionPlan();

            // 合并顺序组件
            for (String componentId : stepPlan.getSequentialComponents()) {
                result.addSequentialComponent(componentId);
            }

            // 合并并行分支
            for (String key : stepPlan.getParallelBranches().keySet()) {
                result.getParallelBranches().put(key, stepPlan.getParallelBranches().get(key));
            }

            // 合并条件分支
            result.getConditionalBranches().putAll(stepPlan.getConditionalBranches());
        }

        return result;
    }
}
