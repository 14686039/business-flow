package com.eking.flow.example.components;

import com.eking.flow.component.NodeComponent;
import com.eking.flow.example.OrderContext;
import org.springframework.stereotype.Component;

/**
 * Component for processing standard orders (amount < $100)
 */
@Component("processStandardOrder")
class ProcessStandardOrderComponent extends NodeComponent {

    @Override
    public void process() {
        System.out.println("[ProcessStandardOrder] Processing standard order...");

        OrderContext context = (OrderContext) getContext();

        // Standard order processing logic
        System.out.println("[ProcessStandardOrder] Applying standard benefits:");
        System.out.println("[ProcessStandardOrder] - Regular shipping fee: $5");
        System.out.println("[ProcessStandardOrder] - No discount");

        // Simulate processing time
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("[ProcessStandardOrder] Standard order processed successfully");
    }
}
