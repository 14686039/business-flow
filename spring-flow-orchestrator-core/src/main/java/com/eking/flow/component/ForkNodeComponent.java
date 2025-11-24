package com.eking.flow.component;

import com.eking.flow.routing.ForkResult;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 支持分支 / 并行执行的扩展组件
 * 子类需要实现 fork() 方法来定义分支逻辑
 * fork() 方法应该返回一个 ForkResult 对象，指定要并行执行的组件 ID 列表
 * 如果返回 null，则表示继续顺序执行后续组件
 */
public abstract class ForkNodeComponent extends NodeComponent {

    /**
     *
     * 定义分支逻辑，返回要并行执行的组件 ID 列表
     * 如果返回 null，则表示继续顺序执行后续组件
     */
    public abstract ForkResult fork() throws Exception;

    /**
     *
     * 重构：执行 fork 逻辑
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
     *
     * 从列表中提取多个分支 ID
     */
    protected ForkResult forkAll(String... componentIds) {
        return ForkResult.forkTo(componentIds);
    }

    /**
     * 创建一个分支结果，指定要并行执行的组件 ID 列表
     */
    protected ForkResult forkTo(String componentId1, String componentId2) {
        return ForkResult.forkTo(componentId1, componentId2);
    }

    /**
     * 创建一个分支结果，指定要并行执行的组件 ID 列表（最多 3 个）
     */
    protected ForkResult forkTo(String componentId1, String componentId2, String componentId3) {
        List<String> branches = new ArrayList<>();
        branches.add(componentId1);
        branches.add(componentId2);
        branches.add(componentId3);
        return ForkResult.forkTo(branches);
    }

    /**
     * 继续顺序执行后续组件（不实际分支）
     */
    protected ForkResult continueSequential(String componentId) {
        return ForkResult.continueSequential(componentId);
    }
}
