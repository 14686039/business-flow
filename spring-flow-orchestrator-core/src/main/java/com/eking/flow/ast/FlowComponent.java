package com.eking.flow.ast;

import com.eking.flow.execution.ExecutionPlan;

/**
 * Base interface for all flow components
 */
public interface FlowComponent {
    /**
     * Convert to string representation
     */
    String toString();

    /**
     * Convert to ExecutionPlan format
     * This method will be implemented by each concrete component
     */
    ExecutionPlan toExecutionPlan();
}
