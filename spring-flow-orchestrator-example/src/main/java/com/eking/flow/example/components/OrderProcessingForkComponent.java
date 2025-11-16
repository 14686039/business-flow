package com.eking.flow.example.components;

import com.eking.flow.component.ForkNodeComponent;
import com.eking.flow.example.OrderContext;
import com.eking.flow.routing.ForkResult;
import org.springframework.stereotype.Component;

/**
 * Fork component that splits order processing into parallel branches
 */
@Component("orderProcessingFork")
class OrderProcessingForkComponent extends ForkNodeComponent {

    @Override
    public ForkResult fork() throws Exception {
        System.out.println("[OrderProcessingFork] Splitting order processing into parallel branches...");

        OrderContext context = (OrderContext) getContext();
        System.out.println("[OrderProcessingFork] Order ID: " + context.getOrderId());
        System.out.println("[OrderProcessingFork] Starting parallel processing:");

        // Fork into 3 parallel branches
        return ForkResult.forkTo(
            "processInventory",
            "processPayment",
            "sendNotification"
        );
    }
}
