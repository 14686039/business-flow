package com.eking.flow.example.complex.components;

import com.eking.flow.component.NodeComponent;
import com.eking.flow.example.ComplexFlowContext;
import org.springframework.stereotype.Component;

/**
 * Component H - 后处理节点
 * 聚合完成后进行最终处理
 */
@Component("H")
class ComponentH extends NodeComponent {

    @Override
    public void process() {
        System.out.println("[ComponentH] ========================================");
        System.out.println("[ComponentH] 后处理阶段 - 最终处理");
        System.out.println("[ComponentH] ========================================\n");

        ComplexFlowContext context = (ComplexFlowContext) getContext();
        System.out.println("[ComponentH] 执行最终数据处理...");
        System.out.println("[ComponentH] 合并状态: " + context.getStatus());
        System.out.println("[ComponentH] 分支标识: " + context.getBranch());

        // 模拟后处理
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        context.setStatus("FINAL_PROCESSED");
        System.out.println("[ComponentH] ✓ 最终处理完成");
        System.out.println("[ComponentH] ✓ 状态: " + context.getStatus());
        System.out.println("[ComponentH] ✓ 准备进入验证阶段\n");
    }
}
