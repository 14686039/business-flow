package com.eking.flow.example.complex.components;

import com.eking.flow.component.ForkNodeComponent;
import com.eking.flow.example.ComplexFlowContext;
import com.eking.flow.routing.ForkResult;
import org.springframework.stereotype.Component;

/**
 * Component D - 快速通道分发节点
 * 分发到F1和F2并行执行
 */
@Component("D")
class ComponentD extends ForkNodeComponent {

    @Override
    public ForkResult fork() throws Exception {
        System.out.println("[ComponentD] ========================================");
        System.out.println("[ComponentD] 快速通道 - 准备分发任务");
        System.out.println("[ComponentD] ========================================\n");

        ComplexFlowContext context = (ComplexFlowContext) getContext();
        System.out.println("[ComponentD] 正在处理快速订单...");
        System.out.println("[ComponentD] 订单金额: $" + context.getAmount());

        System.out.println("[ComponentD] → 分发到两个并行任务:");
        System.out.println("[ComponentD]   1. Component F1 - 快速处理订单");
        System.out.println("[ComponentD]   2. Component F2 - 发送确认邮件\n");

        // 分发到F1和F2并行执行
        return ForkResult.forkTo("F1", "F2");
    }
}
