package com.eking.flow.component;

import java.util.List;

/**
 * Extended component that supports join/aggregation after parallel execution.
 * This component will be executed after all forked branches complete.
 */
public abstract class JoinNodeComponent extends NodeComponent {

    /**
     * Join logic - executed after all parallel branches complete
     * Subclasses can override this to aggregate results from forked branches
     */
    public void process() throws Exception {
        System.out.println("[JoinNode] All branches completed, aggregating results...");
    }

    /**
     * Custom join logic that receives the list of completed component names
     * Subclasses can override this to access the list of completed branches
     */
    public void join(List<String> completedBranches) throws Exception {
        // Default implementation just calls process()
        process();
    }

    /**
     * Get the names of completed branches from slot data
     */
    protected List<String> getCompletedBranches() {
        return getData("__completed_branches__");
    }

    /**
     * Check if all expected branches have completed
     */
    protected boolean areAllBranchesCompleted(int expectedCount) {
        List<String> completed = getCompletedBranches();
        return completed != null && completed.size() >= expectedCount;
    }
}
