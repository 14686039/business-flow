package com.eking.flow.example.complex.components;

import com.eking.flow.component.NodeComponent;
import com.eking.flow.example.ComplexFlowContext;
import org.springframework.stereotype.Component;

/**
 * Component F6 - 低金额处理（分支2的false路径）
 */
@Component("F6")
class ComponentF6 extends NodeComponent {

    @Override
    public void process() {
        System.out.println("[ComponentF6] >>> 分支2 - 低金额处理 (false路径) <<<");
        System.out.println("[ComponentF6] 执行低金额订单标准处理...");

        ComplexFlowContext context = (ComplexFlowContext) getContext();
        System.out.println("[ComponentF6] 请求ID: " + context.getRequestId());
        System.out.println("[ComponentF6] 金额: $" + context.getAmount());

        // 模拟低金额标准处理
        try {
            Thread.sleep(80);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        context.setStatus("LOW_AMOUNT_PROCESSED");
        System.out.println("[ComponentF6] ✓ 低金额处理完成");
        System.out.println("[ComponentF6] >>> 分支2 - 低金额处理结束 <<<\n");
    }
}
