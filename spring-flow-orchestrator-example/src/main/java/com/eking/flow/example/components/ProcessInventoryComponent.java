package com.eking.flow.example.components;

import com.eking.flow.component.NodeComponent;
import com.eking.flow.example.OrderContext;
import org.springframework.stereotype.Component;

/**
 * Component for processing inventory (branch 1)
 */
@Component("processInventory")
class ProcessInventoryComponent extends NodeComponent {

    @Override
    public void process() {
        System.out.println("[ProcessInventory] Checking inventory...");

        OrderContext context = (OrderContext) getContext();
        System.out.println("[ProcessInventory] Product ID: " + context.getProductId());
        System.out.println("[ProcessInventory] Quantity: " + context.getQuantity());

        // Simulate inventory check
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("[ProcessInventory] ✓ Inventory check completed");
    }
}
