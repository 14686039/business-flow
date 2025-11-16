package com.eking.flow.example.components;

import com.eking.flow.component.NodeComponent;
import com.eking.flow.example.OrderContext;
import org.springframework.stereotype.Component;

/**
 * Component to process payment
 */
@Component("processPayment")
class ProcessPaymentComponent extends NodeComponent {

    @Override
    public void process() {
        System.out.println("[ProcessPayment] Processing payment...");

        OrderContext context = (OrderContext) getContext();

        // Simulate payment processing
        System.out.println("[ProcessPayment] Amount: $" + context.getAmount());
        System.out.println("[ProcessPayment] Payment Method: " + context.getPaymentMethod());

        // Simulate payment success
        try {
            Thread.sleep(100); // Simulate processing time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("[ProcessPayment] Payment processed successfully");
    }
}
