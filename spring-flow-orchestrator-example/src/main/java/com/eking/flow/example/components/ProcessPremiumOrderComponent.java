package com.eking.flow.example.components;

import com.eking.flow.component.NodeComponent;
import com.eking.flow.example.OrderContext;
import org.springframework.stereotype.Component;

/**
 * Component for processing premium orders (amount >= $100)
 */
@Component("processPremiumOrder")
class ProcessPremiumOrderComponent extends NodeComponent {

    @Override
    public void process() {
        System.out.println("[ProcessPremiumOrder] Processing premium order...");

        OrderContext context = (OrderContext) getContext();

        // Premium order processing logic
        System.out.println("[ProcessPremiumOrder] Applying premium benefits:");
        System.out.println("[ProcessPremiumOrder] - Free shipping");
        System.out.println("[ProcessPremiumOrder] - 10% discount");
        System.out.println("[ProcessPremiumOrder] - Express delivery");

        // Simulate processing time
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("[ProcessPremiumOrder] Premium order processed successfully");
    }
}
