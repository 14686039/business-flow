package com.eking.flow.example;

import com.eking.flow.bus.FlowBus;
import com.eking.flow.executor.FlowExecutor;
import com.eking.flow.response.EkingflowResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = {"com.eking.flow"})
public class FlowOrchestratorExampleApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(FlowOrchestratorExampleApplication.class, args);

        // Get FlowExecutor from Spring context
        FlowExecutor flowExecutor = context.getBean(FlowExecutor.class);

        // Get FlowBus from Spring context
        FlowBus flowBus = context.getBean(FlowBus.class);

        // Register flow with conditional routing
        flowBus.registerFlow("routedOrderFlow",
                "validateOrder THEN orderAmountRouter THEN sendConfirmation");

        // Register flow with parallel execution (fork/join)
        flowBus.registerFlow("parallelOrderFlow",
                "validateOrder FORK processInventory,processPayment,sendNotification JOIN orderAggregationJoin");

        // Demo 1: Premium order (amount >= 100) - Conditional routing
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║  Demo 1: Premium Order (Amount >= $100)   ║");
        System.out.println("║         (Conditional Routing)             ║");
        System.out.println("╚════════════════════════════════════════════╝");
        executeRoutedOrderFlow(flowExecutor, 150.00);

        // Demo 2: Standard order (amount < 100) - Conditional routing
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║  Demo 2: Standard Order (Amount < $100)   ║");
        System.out.println("║         (Conditional Routing)             ║");
        System.out.println("╚════════════════════════════════════════════╝");
        executeRoutedOrderFlow(flowExecutor, 75.50);

        // Demo 3: Parallel execution - Fork/Join
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║  Demo 3: Parallel Order Processing        ║");
        System.out.println("║         (Fork/Join - 3 Parallel Branches) ║");
        System.out.println("╚════════════════════════════════════════════╝");
        executeParallelOrderFlow(flowExecutor);

        System.exit(0);
    }

    private static void executeRoutedOrderFlow(FlowExecutor flowExecutor, double amount) {
        System.out.println("\n--- Executing routed order flow ---");
        System.out.println("Order amount: $" + amount);

        OrderContext context = new OrderContext();
        context.setOrderId("ORDER-" + System.currentTimeMillis());
        context.setProductId("PRODUCT-67890");
        context.setQuantity(1);
        context.setAmount(amount);
        context.setPaymentMethod("Credit Card");
        context.setCustomerEmail("customer@example.com");

        EkingflowResponse response = flowExecutor.execute("routedOrderFlow", context);

        System.out.println("\n--- Flow Execution Result ---");
        System.out.println("Success: " + response.isSuccess());
        System.out.println("Executed Components: " + response.getExecutedComponents());
        System.out.println("Duration: " + response.getSlot().getDuration() + "ms");

        if (!response.isSuccess() && response.getException() != null) {
            System.out.println("Exception: " + response.getException().getMessage());
        }
    }

    private static void executeParallelOrderFlow(FlowExecutor flowExecutor) {
        System.out.println("\n--- Executing parallel order flow ---");

        OrderContext context = new OrderContext();
        context.setOrderId("ORDER-" + System.currentTimeMillis());
        context.setProductId("PRODUCT-67890");
        context.setQuantity(3);
        context.setAmount(250.00);
        context.setPaymentMethod("Credit Card");
        context.setCustomerEmail("customer@example.com");

        System.out.println("Executing 3 parallel branches:");
        System.out.println("  1. Process Inventory");
        System.out.println("  2. Process Payment");
        System.out.println("  3. Send Notification");
        System.out.println("\nStarting execution...\n");

        long startTime = System.currentTimeMillis();
        EkingflowResponse response = flowExecutor.execute("parallelOrderFlow", context);
        long endTime = System.currentTimeMillis();

        System.out.println("\n--- Parallel Flow Execution Result ---");
        System.out.println("Success: " + response.isSuccess());
        System.out.println("Executed Components: " + response.getExecutedComponents());
        System.out.println("Total Duration: " + (endTime - startTime) + "ms");

        // 只有在成功时才打印Slot Duration
        if (response.getSlot() != null) {
            System.out.println("Slot Duration: " + response.getSlot().getDuration() + "ms");
        } else {
            System.out.println("Slot Duration: N/A (flow execution failed)");
        }

        if (!response.isSuccess() && response.getException() != null) {
            System.out.println("Exception: " + response.getException().getMessage());
        }
    }
}
