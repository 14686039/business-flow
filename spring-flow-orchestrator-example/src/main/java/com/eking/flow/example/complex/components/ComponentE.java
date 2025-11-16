package com.eking.flow.example.complex.components;

import com.eking.flow.component.NodeComponent;
import com.eking.flow.example.ComplexFlowContext;
import org.springframework.stereotype.Component;

/**
 * Component E - 标准通道处理
 */
@Component("E")
class ComponentE extends NodeComponent {

    @Override
    public void process() {
        System.out.println("[ComponentE] ========================================");
        System.out.println("[ComponentE] 标准通道 - 逐步处理订单");
        System.out.println("[ComponentE] ========================================\n");

        ComplexFlowContext context = (ComplexFlowContext) getContext();
        System.out.println("[ComponentE] 正在处理标准订单...");
        System.out.println("[ComponentE] 订单金额: $" + context.getAmount());
        System.out.println("[ComponentE] 优先级: " + context.getPriority());

        // 模拟标准处理步骤
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        context.setStatus("PROCESSED_STANDARD");
        System.out.println("[ComponentE] ✓ 标准处理完成");
        System.out.println("[ComponentE] ✓ 状态: " + context.getStatus());
        System.out.println("[ComponentE] ✓ 准备与快速通道合并\n");
    }
}
