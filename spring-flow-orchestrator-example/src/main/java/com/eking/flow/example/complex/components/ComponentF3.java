package com.eking.flow.example.complex.components;

import com.eking.flow.component.RoutingNodeComponent;
import com.eking.flow.example.ComplexFlowContext;
import com.eking.flow.routing.RoutingResult;
import org.springframework.stereotype.Component;

/**
 * Component F3 - 数据准备组件（并行分支2的第一步）
 */
@Component("F3")
class ComponentF3 extends RoutingNodeComponent {

    @Override
    public void process() {
        System.out.println("[ComponentF3] >>> 分支2 - 第1步 <<<");
        System.out.println("[ComponentF3] 准备额外数据...");

        ComplexFlowContext context = (ComplexFlowContext) getContext();
        System.out.println("[ComponentF3] 请求ID: " + context.getRequestId());
        System.out.println("[ComponentF3] 金额: $" + context.getAmount());

        // 模拟数据准备
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("[ComponentF3] ✓ 数据准备完成");
        System.out.println("[ComponentF3] >>> 分支2 - 第1步结束 <<<\n");
    }

    @Override
    public RoutingResult route() throws Exception {
        ComplexFlowContext context = (ComplexFlowContext) getContext();

        // 根据金额决定路由（但F4才是真正的条件判断）
        // 这里直接路由到F4，让F4做条件判断
        System.out.println("[ComponentF3] → 路由到 Component F4 (条件判断)\n");
        return RoutingResult.continueTo("F4");
    }
}
