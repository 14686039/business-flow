package com.eking.flow.example.complex.components;

import com.eking.flow.component.NodeComponent;
import com.eking.flow.example.ComplexFlowContext;
import org.springframework.stereotype.Component;

/**
 * Component B - 数据准备
 */
@Component("B")
class ComponentB extends NodeComponent {

    @Override
    public void process() {
        System.out.println("[ComponentB] 准备处理数据...");

        ComplexFlowContext context = (ComplexFlowContext) getContext();
        context.setPriority(5);
        context.setCategory("ORDER");
        context.setVipUser(false);
        context.setAmount(150.75);

        System.out.println("[ComponentB] ✓ 优先级: " + context.getPriority());
        System.out.println("[ComponentB] ✓ 分类: " + context.getCategory());
        System.out.println("[ComponentB] ✓ VIP用户: " + context.isVipUser());
        System.out.println("[ComponentB] ✓ 金额: $" + context.getAmount());
        System.out.println("[ComponentB] ✓ 数据准备完成\n");
    }
}
