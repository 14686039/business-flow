package com.eking.flow.example.complex.components;

import com.eking.flow.component.NodeComponent;
import com.eking.flow.example.ComplexFlowContext;
import org.springframework.stereotype.Component;

/**
 * Component Y - 最终完成（流程最后一个组件）
 */
@Component("Y")
class ComponentY extends NodeComponent {

    @Override
    public void process() {
        System.out.println("[ComponentY] >>> 最终完成 <<<");
        System.out.println("[ComponentY] 执行最终处理...");

        ComplexFlowContext context = (ComplexFlowContext) getContext();
        System.out.println("[ComponentY] 请求ID: " + context.getRequestId());
        System.out.println("[ComponentY] 最终状态: " + context.getStatus());

        // 模拟最终处理
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        context.setStatus("COMPLETED");
        System.out.println("[ComponentY] ✓ 流程最终完成");
        System.out.println("[ComponentY] >>> 最终完成结束 <<<\n");
    }
}
