package com.eking.flow.example.components;

import com.eking.flow.component.JoinNodeComponent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Join component that aggregates results from parallel branches
 */
@Component("orderAggregationJoin")
class OrderAggregationJoinComponent extends JoinNodeComponent {

    @Override
    public void process() {
        System.out.println("[OrderAggregationJoin] Aggregating results from all branches...");

        // Get list of completed branches
        List<String> completedBranches = getCompletedBranches();

        if (completedBranches != null) {
            System.out.println("[OrderAggregationJoin] Completed branches:");
            for (String branch : completedBranches) {
                System.out.println("[OrderAggregationJoin]   ✓ " + branch);
            }
        }

        System.out.println("[OrderAggregationJoin] ✓ All parallel branches completed successfully");
    }

    @Override
    public void join(List<String> completedBranches) throws Exception {
        System.out.println("\n[OrderAggregationJoin] === Aggregating Order Processing Results ===");

        if (completedBranches != null && !completedBranches.isEmpty()) {
            System.out.println("[OrderAggregationJoin] Completed " + completedBranches.size() + " parallel tasks:");
            for (String branch : completedBranches) {
                System.out.println("[OrderAggregationJoin]   ✓ " + branch);
            }
        }

        System.out.println("[OrderAggregationJoin] Order processing fully completed!");
    }
}
