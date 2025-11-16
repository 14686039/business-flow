package com.eking.flow.routing;

/**
 * Represents the result of a routing decision.
 */
public class RoutingResult {

    private String targetComponentId;
    private boolean shouldContinue;

    private RoutingResult(String targetComponentId, boolean shouldContinue) {
        this.targetComponentId = targetComponentId;
        this.shouldContinue = shouldContinue;
    }

    /**
     * Create a routing result that continues to a specific component
     */
    public static RoutingResult continueTo(String targetComponentId) {
        return new RoutingResult(targetComponentId, true);
    }

    /**
     * Create a routing result that stops execution
     */
    public static RoutingResult stop() {
        return new RoutingResult(null, false);
    }

    public String getTargetComponentId() {
        return targetComponentId;
    }

    public boolean shouldContinue() {
        return shouldContinue;
    }
}
