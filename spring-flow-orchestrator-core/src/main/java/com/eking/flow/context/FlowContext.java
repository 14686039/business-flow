package com.eking.flow.context;

/**
 * Base class for user-defined flow contexts.
 * Extend this class to define your custom context data.
 * Inspired by LiteFlow's Context design.
 */
public abstract class FlowContext {

    private Long requestId;

    /**
     * Get request ID
     */
    public Long getRequestId() {
        return requestId;
    }

    /**
     * Set request ID
     */
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    /**
     * Initialize context - can be overridden for custom initialization
     */
    public void init() {
        // Default: no-op
    }

    /**
     * Destroy context - can be overridden for custom cleanup
     */
    public void destroy() {
        // Default: no-op
    }

    /**
     * Check if execution should continue on error - can be overridden for custom behavior
     */
    public boolean isContinueOnError() {
        return false;
    }
}
