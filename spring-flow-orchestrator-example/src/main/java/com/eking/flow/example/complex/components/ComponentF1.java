package com.eking.flow.example.complex.components;

import com.eking.flow.component.NodeComponent;
import com.eking.flow.example.ComplexFlowContext;
import org.springframework.stereotype.Component;

/**
 * Component F1 - 快速处理订单（并行分支1）
 */
@Component("F1")
class ComponentF1 extends NodeComponent {

    @Override
    public void process() {
        System.out.println("[ComponentF1] >>> 并行分支 1 - 开始 <<<");
        System.out.println("[ComponentF1] 执行订单快速处理...");

        ComplexFlowContext context = (ComplexFlowContext) getContext();
        System.out.println("[ComponentF1] 请求ID: " + context.getRequestId());
        System.out.println("[ComponentF1] 金额: $" + context.getAmount());

        // 模拟快速处理
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("[ComponentF1] ✓ 快速处理完成");
        System.out.println("[ComponentF1] >>> 并行分支 1 - 结束 <<<\n");
    }
}
