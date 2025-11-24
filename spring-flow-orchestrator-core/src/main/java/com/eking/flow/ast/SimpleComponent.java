package com.eking.flow.ast;

import com.eking.flow.execution.ExecutionPlan;

/**
 * 简单组件
 * 简单组件表示一个单独的组件ID，例如：A
 */
public class SimpleComponent implements FlowComponent {
    private final String name;

    public SimpleComponent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public ExecutionPlan toExecutionPlan() {
        ExecutionPlan plan = new ExecutionPlan();
        plan.addSequentialComponent(name);
        return plan;
    }
}
