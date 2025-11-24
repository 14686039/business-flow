package com.eking.flow.routing;

import java.util.List;

/**
 * 表示 fork 操作的结果。
 */
public class ForkResult {

    private List<String> branchComponentIds;

    private ForkResult(List<String> branchComponentIds) {
        this.branchComponentIds = branchComponentIds;
    }

    /**
     * 创建一个 fork 结果，将流量分支到多个组件
     */
    public static ForkResult forkTo(String... componentIds) {
        return new ForkResult(List.of(componentIds));
    }

    /**
     * 创建一个 fork 结果，从组件 ID 列表中分支流量
     */
    public static ForkResult forkTo(List<String> componentIds) {
        return new ForkResult(componentIds);
    }

    /**
     * 创建一个 fork 结果，继续顺序执行（无实际 fork）
     */
    public static ForkResult continueSequential(String componentId) {
        return new ForkResult(List.of(componentId));
    }
    /**
     * 获取分支组件 ID 列表
     */
    public List<String> getBranchComponentIds() {
        return branchComponentIds;
    }

    /**
     * 判断是否需要 fork
     */
    public boolean shouldFork() {
        return branchComponentIds.size() > 1;
    }
}
