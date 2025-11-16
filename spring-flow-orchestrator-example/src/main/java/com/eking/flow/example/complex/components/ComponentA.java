package com.eking.flow.example.complex.components;

import com.eking.flow.component.NodeComponent;
import com.eking.flow.example.ComplexFlowContext;
import org.springframework.stereotype.Component;

/**
 * Component A - 流程开始
 */
@Component("A")
public class ComponentA extends NodeComponent {

    @Override
    public void process() {
        System.out.println("\n[ComponentA] ========================================");
        System.out.println("[ComponentA] 流程开始 - 初始化请求");
        System.out.println("[ComponentA] ========================================\n");

        ComplexFlowContext context = (ComplexFlowContext) getContext();
        context.setRequestId(System.currentTimeMillis());
        context.setStatus("INITIALIZED");

        System.out.println("[ComponentA] ✓ 请求已初始化");
        System.out.println("[ComponentA] ✓ 请求ID: " + context.getRequestId());
        System.out.println("[ComponentA] ✓ 状态: " + context.getStatus());
    }
}
