package com.eking.flow.routing;

import java.util.List;

/**
 * Represents the result of a fork operation.
 */
public class ForkResult {

    private List<String> branchComponentIds;

    private ForkResult(List<String> branchComponentIds) {
        this.branchComponentIds = branchComponentIds;
    }

    /**
     * Create a fork result that branches into multiple components
     */
    public static ForkResult forkTo(String... componentIds) {
        return new ForkResult(List.of(componentIds));
    }

    /**
     * Create a fork result from a list of component IDs
     */
    public static ForkResult forkTo(List<String> componentIds) {
        return new ForkResult(componentIds);
    }

    /**
     * Create a fork result that continues sequentially (no actual fork)
     */
    public static ForkResult continueSequential(String componentId) {
        return new ForkResult(List.of(componentId));
    }

    public List<String> getBranchComponentIds() {
        return branchComponentIds;
    }

    public boolean shouldFork() {
        return branchComponentIds.size() > 1;
    }
}
