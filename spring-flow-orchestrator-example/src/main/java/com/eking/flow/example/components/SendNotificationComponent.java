package com.eking.flow.example.components;

import com.eking.flow.component.NodeComponent;
import com.eking.flow.example.OrderContext;
import org.springframework.stereotype.Component;

/**
 * Component for sending notifications (branch 3)
 */
@Component("sendNotification")
class SendNotificationComponent extends NodeComponent {

    @Override
    public void process() {
        System.out.println("[SendNotification] Sending notification...");

        OrderContext context = (OrderContext) getContext();
        System.out.println("[SendNotification] Customer Email: " + context.getCustomerEmail());

        // Simulate sending notification
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("[SendNotification] ✓ Notification sent");
    }
}
