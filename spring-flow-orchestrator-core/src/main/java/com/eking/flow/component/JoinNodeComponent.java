package com.eking.flow.component;

import java.util.List;

/**
 *
 * 加入节点组件 - 用于并行执行分支后合并结果
 */
public abstract class JoinNodeComponent extends NodeComponent {

    /**
     *
     * 加入逻辑 - 所有并行分支完成后执行
     * 子类可以覆盖此方法来聚合来自分支的结果
     */
    public void process() throws Exception {
        System.out.println("[JoinNode] All branches completed, aggregating results...");
    }

    /**
     * Custom join logic that receives the list of completed component names
     * Subclasses can override this to access the list of completed branches
     *
     */
    public void join(List<String> completedBranches) throws Exception {
        // Default implementation just calls process()
        process();
    }

    /**
     *
     * 获取所有已完成分支的组件名称列表
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
