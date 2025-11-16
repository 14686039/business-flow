package com.eking.flow.example.complex.components;

import com.eking.flow.component.NodeComponent;
import com.eking.flow.example.ComplexFlowContext;
import org.springframework.stereotype.Component;

/**
 * Component F5 - 高金额处理（分支2的true路径）
 */
@Component("F5")
class ComponentF5 extends NodeComponent {

    @Override
    public void process() {
        System.out.println("[ComponentF5] >>> 分支2 - 高金额处理 (true路径) <<<");
        System.out.println("[ComponentF5] 执行高金额订单特殊处理...");

        ComplexFlowContext context = (ComplexFlowContext) getContext();
        System.out.println("[ComponentF5] 请求ID: " + context.getRequestId());
        System.out.println("[ComponentF5] 金额: $" + context.getAmount());
        System.out.println("[ComponentF5] 优先级: " + context.getPriority());

        // 模拟高金额特殊处理
        try {
            Thread.sleep(120);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        context.setStatus("HIGH_AMOUNT_PROCESSED");
        System.out.println("[ComponentF5] ✓ 高金额处理完成");
        System.out.println("[ComponentF5] >>> 分支2 - 高金额处理结束 <<<\n");
    }
}
