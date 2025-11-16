package com.eking.flow.component;

import com.eking.flow.routing.RoutingResult;

/**
 * Extended component that supports routing logic.
 * Subclasses should implement route() method to determine the next component.
 */
public abstract class RoutingNodeComponent extends NodeComponent {

    /**
     * Define routing logic - return the ID of the next component to execute
     * Return null to stop execution
     */
    public abstract RoutingResult route() throws Exception;

    /**
     * Override process() to implement routing logic
     */
    @Override
    public void process() throws Exception {
        // Execute routing logic
        RoutingResult result = route();

        if (result != null && result.shouldContinue()) {
            String targetComponentId = result.getTargetComponentId();
            if (targetComponentId != null) {
                setData("__routing_target__", targetComponentId);
            } else {
                setData("__routing_target__", "__STOP__");
            }
        }
    }
}
