package com.eking.flow.example.complex.components;

import com.eking.flow.component.NodeComponent;
import com.eking.flow.example.ComplexFlowContext;
import org.springframework.stereotype.Component;

/**
 * Component J - 完成节点
 * 流程的最终完成处理
 */
@Component("J")
class ComponentJ extends NodeComponent {

    @Override
    public void process() {
        System.out.println("[ComponentJ] ========================================");
        System.out.println("[ComponentJ] 完成阶段 - 流程结束");
        System.out.println("[ComponentJ] ========================================\n");

        ComplexFlowContext context = (ComplexFlowContext) getContext();
        System.out.println("[ComponentJ] 执行最终清理...");
        System.out.println("[ComponentJ] 流程请求ID: " + context.getRequestId());
        System.out.println("[ComponentJ] 处理金额: $" + context.getAmount());
        System.out.println("[ComponentJ] 最终状态: " + context.getStatus());
        System.out.println("[ComponentJ] 完成分支: " + context.getBranch());

        // 模拟最终清理
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        context.setStatus("COMPLETED");
        System.out.println("\n[ComponentJ] ✓✓✓ 流程执行完成 ✓✓✓");
        System.out.println("[ComponentJ] ✓ 所有组件执行完毕");
        System.out.println("[ComponentJ] ✓ 数据处理完成");
        System.out.println("[ComponentJ] ✓ 资源清理完成");
        System.out.println("[ComponentJ] ========================================\n");
    }
}
