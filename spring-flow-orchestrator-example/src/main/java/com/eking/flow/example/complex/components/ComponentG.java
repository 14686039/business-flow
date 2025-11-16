package com.eking.flow.example.complex.components;

import com.eking.flow.component.JoinNodeComponent;
import com.eking.flow.example.ComplexFlowContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Component G - 汇总组件
 * 等待所有分支完成后进行汇总
 */
@Component("G")
class ComponentG extends JoinNodeComponent {

    @Override
    public void join(List<String> completedBranches) throws Exception {
        System.out.println("[ComponentG] ========================================");
        System.out.println("[ComponentG] 汇总节点 - 合并所有分支结果");
        System.out.println("[ComponentG] ========================================\n");

        ComplexFlowContext context = (ComplexFlowContext) getContext();

        System.out.println("[ComponentG] 等待的分支:");
        System.out.println("[ComponentG]   - Component D 的并行分支 (F1, F2)");
        System.out.println("[ComponentG]   - Component E 的标准路径");

        System.out.println("\n[ComponentG] 已完成的分支:");
        if (completedBranches != null && !completedBranches.isEmpty()) {
            for (String branch : completedBranches) {
                System.out.println("[ComponentG]   ✓ " + branch);
            }
        } else {
            System.out.println("[ComponentG]   (暂无并行分支)");
        }

        // 汇总结果
        context.setBranch("MERGED");
        context.setStatus("AGGREGATED");

        System.out.println("\n[ComponentG] ✓ 所有分支已合并");
        System.out.println("[ComponentG] ✓ 状态: " + context.getStatus());
        System.out.println("[ComponentG] ✓ 准备进入下一阶段\n");
    }
}
