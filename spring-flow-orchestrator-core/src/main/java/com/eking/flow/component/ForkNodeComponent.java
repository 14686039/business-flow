package com.eking.flow.component;

import com.eking.flow.routing.ForkResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Extended component that supports fork/parallel execution.
 * Subclasses should implement fork() method to define the branches.
 */
public abstract class ForkNodeComponent extends NodeComponent {

    /**
     * Define fork logic - return the list of component IDs to execute in parallel
     */
    public abstract ForkResult fork() throws Exception;

    /**
     * Override process() to implement fork logic
     */
    @Override
    public void process() throws Exception {
        // Execute fork logic
        ForkResult result = fork();

        if (result != null) {
            List<String> branchIds = result.getBranchComponentIds();
            setData("__fork_branches__", branchIds);
            System.out.println("[ForkNode] Forking into " + branchIds.size() + " branches: " + branchIds);
        }
    }

    /**
     * Helper method to extract multiple branch IDs from a list
     */
    protected ForkResult forkAll(String... componentIds) {
        return ForkResult.forkTo(componentIds);
    }

    /**
     * Helper method to create a fork result
     */
    protected ForkResult forkTo(String componentId1, String componentId2) {
        return ForkResult.forkTo(componentId1, componentId2);
    }

    /**
     * Helper method to create a fork result with three branches
     */
    protected ForkResult forkTo(String componentId1, String componentId2, String componentId3) {
        List<String> branches = new ArrayList<>();
        branches.add(componentId1);
        branches.add(componentId2);
        branches.add(componentId3);
        return ForkResult.forkTo(branches);
    }

    /**
     * Helper method to continue sequentially (no actual fork)
     */
    protected ForkResult continueSequential(String componentId) {
        return ForkResult.continueSequential(componentId);
    }
}
