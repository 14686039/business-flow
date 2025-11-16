package com.eking.flow.example.components;

import com.eking.flow.component.NodeComponent;
import com.eking.flow.example.OrderContext;
import org.springframework.stereotype.Component;

/**
 * Component to send confirmation email
 */
@Component("sendConfirmation")
class SendConfirmationComponent extends NodeComponent {

    @Override
    public void process() {
        System.out.println("[SendConfirmation] Sending confirmation email...");

        OrderContext context = (OrderContext) getContext();

        // Simulate sending email
        System.out.println("[SendConfirmation] To: " + context.getCustomerEmail());
        System.out.println("[SendConfirmation] Order ID: " + context.getOrderId());
        System.out.println("[SendConfirmation] Amount: $" + context.getAmount());

        System.out.println("[SendConfirmation] Confirmation email sent successfully");
    }
}
