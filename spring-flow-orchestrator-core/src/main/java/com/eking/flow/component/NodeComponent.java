package com.eking.flow.component;

import com.eking.flow.context.FlowContext;
import com.eking.flow.slot.Slot;

/**
 * Base component class that all user-defined components must extend.
 * Inspired by LiteFlow's NodeComponent design.
 */
public abstract class NodeComponent {

    private String id;
    private Slot slot;
    private FlowContext context;
    private boolean continueOnError = false;

    /**
     * Main execution method that must be implemented by user components
     */
    public abstract void process() throws Exception;

    /**
     * Called before process() - can be overridden for custom logic
     */
    public void beforeProcess() throws Exception {
        // Default: no-op
    }

    /**
     * Called after process() - can be overridden for custom logic
     */
    public void afterProcess() throws Exception {
        // Default: no-op
    }

    /**
     * Called when an exception occurs - can be overridden for custom error handling
     */
    public void onError(Exception e) throws Exception {
        throw e;
    }

    /**
     * Get component ID
     */
    public String getId() {
        return id;
    }

    /**
     * Set component ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get current slot
     */
    public Slot getSlot() {
        return slot;
    }

    /**
     * Set current slot
     */
    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    /**
     * Get flow context
     */
    public FlowContext getContext() {
        return context;
    }

    /**
     * Set flow context
     */
    public void setContext(FlowContext context) {
        this.context = context;
    }

    /**
     * Get data from slot by key
     */
    public <T> T getData(String key) {
        return slot != null ? slot.getData(key) : null;
    }

    /**
     * Set data to slot by key
     */
    public void setData(String key, Object value) {
        if (slot != null) {
            slot.setData(key, value);
        }
    }

    /**
     * Set flag to continue execution even if this component fails
     */
    public NodeComponent setContinueOnError(boolean continueOnError) {
        this.continueOnError = continueOnError;
        return this;
    }

    /**
     * Check if execution should continue on error
     */
    public boolean isContinueOnError() {
        return continueOnError;
    }

    /**
     * Get component name for logging
     */
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return "NodeComponent{" +
                "id='" + id + '\'' +
                ", name='" + getName() + '\'' +
                '}';
    }
}
