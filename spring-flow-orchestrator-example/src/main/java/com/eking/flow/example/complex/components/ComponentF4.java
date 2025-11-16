package com.eking.flow.example.complex.components;

import com.eking.flow.component.RoutingNodeComponent;
import com.eking.flow.example.ComplexFlowContext;
import com.eking.flow.routing.RoutingResult;
import org.springframework.stereotype.Component;

/**
 * Component F4 - 条件判断组件（决定F5或F6）
 */
@Component("F4")
public class ComponentF4 extends RoutingNodeComponent {

    @Override
    public void process() {
        System.out.println("[ComponentF4] >>> 分支2 - 条件判断 <<<");
        System.out.println("[ComponentF4] 进行条件判断...");

        ComplexFlowContext context = (ComplexFlowContext) getContext();
        System.out.println("[ComponentF4] 金额: $" + context.getAmount());

        // 模拟条件判断处理
        try {
            Thread.sleep(80);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("[ComponentF4] ✓ 条件判断完成");
        System.out.println("[ComponentF4] >>> 分支2 - 条件判断结束 <<<\n");
    }

    @Override
    public RoutingResult route() throws Exception {
        ComplexFlowContext context = (ComplexFlowContext) getContext();

        // 根据金额决定路由
        if (context.getAmount() >= 150) {
            System.out.println("[ComponentF4] → 路由到 Component F5 (高金额处理)\n");
            return RoutingResult.continueTo("F5");
        } else {
            System.out.println("[ComponentF4] → 路由到 Component F6 (低金额处理)\n");
            return RoutingResult.continueTo("F6");
        }
    }
}
