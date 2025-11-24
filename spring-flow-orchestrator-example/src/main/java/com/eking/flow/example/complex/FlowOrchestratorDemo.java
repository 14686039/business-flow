package com.eking.flow.example.complex;

import com.eking.flow.bus.FlowBus;
import com.eking.flow.example.ComplexFlowContext;
import com.eking.flow.executor.FlowExecutor;
import com.eking.flow.response.EkingflowResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 复杂流程编排示例
 * 演示：顺序执行、条件路由、并行执行、汇聚合并
 */
@Component
public class FlowOrchestratorDemo implements CommandLineRunner {

    private final FlowExecutor flowExecutor;
    private final FlowBus flowBus;

    public FlowOrchestratorDemo(FlowExecutor flowExecutor, FlowBus flowBus) {
        this.flowExecutor = flowExecutor;
        this.flowBus = flowBus;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n");
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║         复杂流程编排示例 - 演示所有执行模式                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println("\n");

        // 注册复杂流程
        registerComplexFlows();

        // 测试场景1：高金额订单（走并行流程）
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("【场景1】高金额订单 - 走快速通道（并行处理）");
        System.out.println("═══════════════════════════════════════════════════════════════");
        ComplexFlowContext context1 = new ComplexFlowContext();
        context1.setRequestId(1001L);
        context1.setAmount(150.0);
        context1.setPriority(1);
        context1.setCategory("ELECTRONICS");
        context1.setVipUser(true);

        executeFlow("complex-flow-high", context1);

        Thread.sleep(500);

        // 测试场景2：低金额订单（走标准流程）
        System.out.println("\n");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("【场景2】低金额订单 - 走标准通道");
        System.out.println("═══════════════════════════════════════════════════════════════");
        ComplexFlowContext context2 = new ComplexFlowContext();
        context2.setRequestId(1002L);
        context2.setAmount(50.0);
        context2.setPriority(3);
        context2.setCategory("BOOKS");
        context2.setVipUser(false);

        executeFlow("complex-flow-low", context2);

        Thread.sleep(500);

        // 测试场景3：演示简单条件路由（不走并行）
        System.out.println("\n");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("【场景3】简单条件路由 - 不含并行执行");
        System.out.println("═══════════════════════════════════════════════════════════════");
        ComplexFlowContext context3 = new ComplexFlowContext();
        context3.setRequestId(1003L);
        context3.setAmount(200.0);
        context3.setPriority(2);
        context3.setCategory("FASHION");
        context3.setVipUser(false);

        executeFlow("complex-flow-simple", context3);

        Thread.sleep(500);

        // 测试场景4：演示强制并行执行
        System.out.println("\n");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("【场景4】强制并行执行 - 跳过条件路由");
        System.out.println("═══════════════════════════════════════════════════════════════");
        ComplexFlowContext context4 = new ComplexFlowContext();
        context4.setRequestId(1004L);
        context4.setAmount(300.0);
        context4.setPriority(1);
        context4.setCategory("HOME");
        context4.setVipUser(true);

        executeFlow("complex-flow-parallel", context4);

        Thread.sleep(500);

        // 测试场景5：演示超级嵌套表达式（企业级复杂场景）
        System.out.println("\n");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("【场景5】超级嵌套表达式 - 演示企业级复杂场景");
        System.out.println("═══════════════════════════════════════════════════════════════");
        ComplexFlowContext context5 = new ComplexFlowContext();
        context5.setRequestId(1005L);
        context5.setAmount(200.0);
        context5.setPriority(1);
        context5.setCategory("PREMIUM");
        context5.setVipUser(true);

        executeFlow("complex-flow-nested", context5);

        System.out.println("\n");
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                  所有场景执行完毕                             ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println("\n");
    }

    private void registerComplexFlows() {
        System.out.println("注册复杂流程定义...\n");

        // 最复杂的流程：包含条件路由和并行执行
        // 当金额>=100时：C路由到D（分发节点），D分发到F1、F2并行执行，然后汇聚到G
        // 当金额<100时：C路由到E（标准处理）
        // 表达式：A->B->C?D:E->H->I->J
        // 其中D是分发节点：它会分发到F1、F2，然后汇聚到G
        // 为了简化，我们使用两个独立的流程来演示
        flowBus.registerFlow("complex-flow-high",
            "A->B->D->(F1,F2) -> G->H->I->J");

        flowBus.registerFlow("complex-flow-low",
            "A->B->C?E:H->I->J");

        flowBus.registerFlow("complex-flow-full",
            "A->B->C?D:E->H->I->J");

        flowBus.registerFlow("complex-flow-simple",
            "A->B->C?D:E->H->I->J");

        // 并行流程：只演示并行执行
        flowBus.registerFlow("complex-flow-parallel",
            "A->B->D->(F1,F2)-> G->H->I->J");

        // 最复杂的嵌套表达式：包含嵌套的和条件
        // A -> (F1->F2, F3->F4?F5:F6) -> X -> Y
        // 分支1：F1 -> F2 (顺序执行)
        // 分支2：F3 -> F4?F5:F6 (条件路由，其中F3->F4是顺序)
        // ->后：X -> Y
        flowBus.registerFlow("complex-flow-nested",
            "A->(F1->F2, F3->F4?F5:F6)-> X->Y");

        System.out.println("✓ 高金额并行流程: A->B->D->(F1,F2)-> G->H->I->J");
        System.out.println("  (当金额>=100时，ComponentC路由到ComponentD)");
        System.out.println("✓ 低金额标准流程: A->B->C?E->H->I->J");
        System.out.println("  (当金额<100时，ComponentC路由到ComponentE)");
        System.out.println("✓ 完整复杂流程: A->B->C?D:E->H->I->J");
        System.out.println("  (包含：顺序执行 + 条件路由)");
        System.out.println("✓ 并行执行演示: A->B->D->(F1,F2)->G->H->I->J");
        System.out.println("✓ 超级嵌套流程: A->(F1->F2, F3->F4?F5:F6)-> X->Y");
        System.out.println("  (包含：嵌套 + 嵌套条件 + 箭头链)\n");
    }

    private void executeFlow(String flowId, ComplexFlowContext context) {
        try {
            System.out.println("执行流程: " + flowId);
            System.out.println("订单详情:");
            System.out.println("  - 请求ID: " + context.getRequestId());
            System.out.println("  - 订单金额: $" + context.getAmount());
            System.out.println("  - 优先级: " + context.getPriority());
            System.out.println("  - 商品类别: " + context.getCategory());
            System.out.println("  - VIP用户: " + (context.isVipUser() ? "是" : "否"));
            System.out.println();

            long startTime = System.currentTimeMillis();
            EkingflowResponse response = flowExecutor.execute(flowId, context);
            long endTime = System.currentTimeMillis();

            System.out.println("\n流程执行结果:");
            System.out.println("  - 执行状态: " + (response.isSuccess() ? "成功" : "失败"));
            System.out.println("  - 执行时间: " + (endTime - startTime) + "ms");
            System.out.println("  - 最终状态: " + context.getStatus());

            if (!response.isSuccess()) {
                System.out.println("  - 错误信息: " + response.getMessage());
            }

        } catch (Exception e) {
            System.err.println("流程执行出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
