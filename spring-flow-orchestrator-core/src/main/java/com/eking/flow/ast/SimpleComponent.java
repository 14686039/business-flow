package com.eking.flow.ast;

import com.eking.flow.execution.ExecutionPlan;

/**
 * Simple component representing a single component ID
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
