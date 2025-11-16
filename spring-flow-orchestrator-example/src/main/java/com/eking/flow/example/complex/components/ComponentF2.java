package com.eking.flow.example.complex.components;

import com.eking.flow.component.NodeComponent;
import com.eking.flow.example.ComplexFlowContext;
import org.springframework.stereotype.Component;

/**
 * Component F2 - 发送确认邮件（并行分支2）
 */
@Component("F2")
class ComponentF2 extends NodeComponent {

    @Override
    public void process() {
        System.out.println("[ComponentF2] >>> 并行分支 2 - 开始 <<<");
        System.out.println("[ComponentF2] 发送确认邮件...");

        ComplexFlowContext context = (ComplexFlowContext) getContext();
        System.out.println("[ComponentF2] 收件人: customer@example.com");
        System.out.println("[ComponentF2] 金额: $" + context.getAmount());

        // 模拟发送邮件
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("[ComponentF2] ✓ 确认邮件已发送");
        System.out.println("[ComponentF2] >>> 并行分支 2 - 结束 <<<\n");
    }
}
