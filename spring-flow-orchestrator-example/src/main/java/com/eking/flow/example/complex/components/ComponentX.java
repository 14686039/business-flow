package com.eking.flow.example.complex.components;

import com.eking.flow.component.NodeComponent;
import com.eking.flow.example.ComplexFlowContext;
import org.springframework.stereotype.Component;

/**
 * Component X - 汇聚后处理（JOIN后的第一个组件）
 */
@Component("X")
class ComponentX extends NodeComponent {

    @Override
    public void process() {
        System.out.println("[ComponentX] >>> 汇聚后处理 <<<");
        System.out.println("[ComponentX] 汇聚所有分支结果...");

        ComplexFlowContext context = (ComplexFlowContext) getContext();
        System.out.println("[ComponentX] 请求ID: " + context.getRequestId());
        System.out.println("[ComponentX] 汇聚时间: " + System.currentTimeMillis());

        // 模拟汇聚处理
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        context.setStatus("AGGREGATED");
        System.out.println("[ComponentX] ✓ 汇聚处理完成");
        System.out.println("[ComponentX] >>> 汇聚后处理结束 <<<\n");
    }
}
