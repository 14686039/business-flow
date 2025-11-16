package com.eking.flow.example.components;

import com.eking.flow.component.RoutingNodeComponent;
import com.eking.flow.example.OrderContext;
import com.eking.flow.routing.RoutingResult;
import org.springframework.stereotype.Component;

/**
 * Router component to decide next step based on order amount
 */
@Component("orderAmountRouter")
class OrderAmountRouterComponent extends RoutingNodeComponent {

    @Override
    public RoutingResult route() throws Exception {
        System.out.println("[OrderAmountRouter] Evaluating order amount...");

        OrderContext context = (OrderContext) getContext();
        double amount = context.getAmount();

        System.out.println("[OrderAmountRouter] Order amount: $" + amount);

        // Route based on amount threshold
        if (amount >= 100.0) {
            System.out.println("[OrderAmountRouter] Amount >= $100, routing to premium branch");
            return RoutingResult.continueTo("processPremiumOrder");
        } else {
            System.out.println("[OrderAmountRouter] Amount < $100, routing to standard branch");
            return RoutingResult.continueTo("processStandardOrder");
        }
    }
}
