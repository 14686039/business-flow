package com.eking.flow.example.components;

import com.eking.flow.component.NodeComponent;
import com.eking.flow.example.OrderContext;
import org.springframework.stereotype.Component;

/**
 * Component to validate order
 */
@Component("validateOrder")
class ValidateOrderComponent extends NodeComponent {

    @Override
    public void process() {
        System.out.println("[ValidateOrder] Validating order...");

        OrderContext context = (OrderContext) getContext();

        // Validate order data
        if (context.getOrderId() == null || context.getOrderId().isEmpty()) {
            throw new IllegalArgumentException("Order ID is required");
        }

        if (context.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        System.out.println("[ValidateOrder] Order validation passed");
        System.out.println("[ValidateOrder] Order ID: " + context.getOrderId());
        System.out.println("[ValidateOrder] Product ID: " + context.getProductId());
        System.out.println("[ValidateOrder] Quantity: " + context.getQuantity());
    }
}
