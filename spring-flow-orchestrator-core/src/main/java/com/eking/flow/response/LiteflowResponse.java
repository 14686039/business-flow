package com.eking.flow.response;

import com.eking.flow.context.FlowContext;
import com.eking.flow.slot.Slot;

import java.util.ArrayList;
import java.util.List;

/**
 * Response wrapper for flow execution.
 * Inspired by LiteFlow's LiteflowResponse design.
 */
public class LiteflowResponse {

    private boolean success;
    private String message;
    private Exception exception;
    private FlowContext context;
    private Slot slot;
    private List<String> executedComponents;
    private Object data;

    public LiteflowResponse() {
        this.executedComponents = new ArrayList<>();
    }

    /**
     * Create successful response
     */
    public static LiteflowResponse success() {
        LiteflowResponse response = new LiteflowResponse();
        response.setSuccess(true);
        return response;
    }

    /**
     * Create failed response
     */
    public static LiteflowResponse fail(Exception exception) {
        LiteflowResponse response = new LiteflowResponse();
        response.setSuccess(false);
        response.setException(exception);
        return response;
    }

    /**
     * Create failed response with message
     */
    public static LiteflowResponse fail(String message) {
        LiteflowResponse response = new LiteflowResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    /**
     * Check if execution was successful
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Set success flag
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Get message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get exception
     */
    public Exception getException() {
        return exception;
    }

    /**
     * Set exception
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }

    /**
     * Get context
     */
    public FlowContext getContext() {
        return context;
    }

    /**
     * Set context
     */
    public void setContext(FlowContext context) {
        this.context = context;
    }

    /**
     * Get slot
     */
    public Slot getSlot() {
        return slot;
    }

    /**
     * Set slot
     */
    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    /**
     * Get list of executed component names
     */
    public List<String> getExecutedComponents() {
        return executedComponents;
    }

    /**
     * Add executed component name
     */
    public void addExecutedComponent(String componentName) {
        this.executedComponents.add(componentName);
    }

    /**
     * Get response data
     */
    public Object getData() {
        return data;
    }

    /**
     * Set response data
     */
    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LiteflowResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", exception=" + (exception != null ? exception.getClass().getSimpleName() : "null") +
                ", executedComponents=" + executedComponents.size() +
                '}';
    }
}
