package com.eking.flow.example.complex.components;

import com.eking.flow.component.RoutingNodeComponent;
import com.eking.flow.example.ComplexFlowContext;
import com.eking.flow.routing.RoutingResult;
import org.springframework.stereotype.Component;

/**
 * Component C - 条件路由组件
 * 根据金额决定走快速通道还是标准通道
 */
@Component("C")
class ComponentC extends RoutingNodeComponent {

    @Override
    public RoutingResult route() throws Exception {
        System.out.println("[ComponentC] =========================================");
        System.out.println("[ComponentC] 决策点 - 选择处理路径");
        System.out.println("[ComponentC] =========================================\n");

        ComplexFlowContext context = (ComplexFlowContext) getContext();
        double amount = context.getAmount();

        System.out.println("[ComponentC] 当前金额: $" + amount);

        // 如果金额大于100，使用快速通道（D）
        // 否则使用标准通道（E）
        if (amount >= 100.0) {
            System.out.println("[ComponentC] ✓ 金额 >= $100，走快速通道");
            System.out.println("[ComponentC] → 路由到 Component D (快速处理)\n");
            return RoutingResult.continueTo("D");
        } else {
            System.out.println("[ComponentC] ✓ 金额 < $100，走标准通道");
            System.out.println("[ComponentC] → 路由到 Component E (标准处理)\n");
            return RoutingResult.continueTo("E");
        }
    }
}
