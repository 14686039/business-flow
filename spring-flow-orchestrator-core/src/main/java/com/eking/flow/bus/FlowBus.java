package com.eking.flow.bus;

import com.eking.flow.component.NodeComponent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Central registry for flow definitions and components.
 * Inspired by LiteFlow's FlowBus design.
 */
public class FlowBus {

    private static FlowBus instance;

    // Component registry: componentId -> NodeComponent
    private final Map<String, NodeComponent> componentMap;

    // Flow definition registry: flowId -> EL expression
    private final Map<String, String> flowDefinitionMap;

    private FlowBus() {
        this.componentMap = new ConcurrentHashMap<>();
        this.flowDefinitionMap = new ConcurrentHashMap<>();
    }

    /**
     * Get singleton instance
     */
    public static synchronized FlowBus getInstance() {
        if (instance == null) {
            instance = new FlowBus();
        }
        return instance;
    }

    /**
     * Register a component
     */
    public void registerComponent(String componentId, NodeComponent component) {
        component.setId(componentId);
        componentMap.put(componentId, component);
    }

    /**
     * Get a component by ID
     */
    public NodeComponent getComponent(String componentId) {
        return componentMap.get(componentId);
    }

    /**
     * Check if a component exists
     */
    public boolean hasComponent(String componentId) {
        return componentMap.containsKey(componentId);
    }

    /**
     * Remove a component
     */
    public void removeComponent(String componentId) {
        componentMap.remove(componentId);
    }

    /**
     * Register a flow definition
     */
    public void registerFlow(String flowId, String elExpression) {
        flowDefinitionMap.put(flowId, elExpression);
    }

    /**
     * Get a flow definition
     */
    public String getFlowDefinition(String flowId) {
        return flowDefinitionMap.get(flowId);
    }

    /**
     * Check if a flow exists
     */
    public boolean hasFlow(String flowId) {
        return flowDefinitionMap.containsKey(flowId);
    }

    /**
     * Remove a flow
     */
    public void removeFlow(String flowId) {
        flowDefinitionMap.remove(flowId);
    }

    /**
     * Get all registered component IDs
     */
    public List<String> getComponentIds() {
        return new ArrayList<>(componentMap.keySet());
    }

    /**
     * Get all registered flow IDs
     */
    public List<String> getFlowIds() {
        return new ArrayList<>(flowDefinitionMap.keySet());
    }

    /**
     * Clear all registrations
     */
    public void clear() {
        componentMap.clear();
        flowDefinitionMap.clear();
    }

    /**
     * Get component count
     */
    public int getComponentCount() {
        return componentMap.size();
    }

    /**
     * Get flow count
     */
    public int getFlowCount() {
        return flowDefinitionMap.size();
    }
}
