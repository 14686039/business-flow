package com.eking.flow.example.complex.components;

import com.eking.flow.component.NodeComponent;
import com.eking.flow.example.ComplexFlowContext;
import org.springframework.stereotype.Component;

/**
 * Component I - 验证节点
 * 验证处理结果的正确性
 */
@Component("I")
class ComponentI extends NodeComponent {

    @Override
    public void process() {
        System.out.println("[ComponentI] ========================================");
        System.out.println("[ComponentI] 验证阶段 - 数据验证");
        System.out.println("[ComponentI] ========================================\n");

        ComplexFlowContext context = (ComplexFlowContext) getContext();
        System.out.println("[ComponentI] 验证处理结果...");
        System.out.println("[ComponentI] 请求ID: " + context.getRequestId());
        System.out.println("[ComponentI] 订单金额: $" + context.getAmount());
        System.out.println("[ComponentI] 当前状态: " + context.getStatus());

        // 模拟验证逻辑
        try {
            Thread.sleep(80);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean isValid = context.getStatus() != null && !context.getStatus().isEmpty();
        if (isValid) {
            System.out.println("[ComponentI] ✓ 验证通过 - 数据完整");
            context.setStatus("VALIDATED");
        } else {
            System.out.println("[ComponentI] ✗ 验证失败 - 数据不完整");
            context.setStatus("VALIDATION_FAILED");
        }

        System.out.println("[ComponentI] ✓ 验证阶段完成");
        System.out.println("[ComponentI] ✓ 最终状态: " + context.getStatus());
        System.out.println("[ComponentI] ✓ 准备进入完成阶段\n");
    }
}
