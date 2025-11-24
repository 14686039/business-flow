package com.eking.flow.bus;

import com.eking.flow.component.NodeComponent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * 流程总线，用于注册和管理流程组件和定义
 */
public class FlowBus {

    private static FlowBus instance;

    /**
     * 组件注册表：组件ID -> 组件实例
     */
    private final Map<String, NodeComponent> componentMap;

    /**
     * 流程定义注册表：流程ID -> EL表达式
     */
    private final Map<String, String> flowDefinitionMap;

    private FlowBus() {
        this.componentMap = new ConcurrentHashMap<>();
        this.flowDefinitionMap = new ConcurrentHashMap<>();
    }

    /**
     * 获取单例实例
     * @return FlowBus instance
     */
    public static synchronized FlowBus getInstance() {
        if (instance == null) {
            instance = new FlowBus();
        }
        return instance;
    }

    /**
     * 注册一个组件实例
     * @param componentId ID of the component
     * @param component Component instance
     */
    public void registerComponent(String componentId, NodeComponent component) {
        component.setId(componentId);
        componentMap.put(componentId, component);
    }

    /**
     * 获取指定ID的组件实例
     * @param componentId ID of the component
     * @return Component instance or null if not found
     */
    public NodeComponent getComponent(String componentId) {
        return componentMap.get(componentId);
    }

    /**
     * 检测指定ID的组件是否已注册
     * @param componentId ID of the component
     * @return true if component exists, false otherwise
     */
    public boolean hasComponent(String componentId) {
        return componentMap.containsKey(componentId);
    }

    /**
     * 移除指定ID的组件
     * @param componentId ID of the component to remove
     */
    public void removeComponent(String componentId) {
        componentMap.remove(componentId);
    }

    /**
     * 注册一个流程定义
     * @param flowId ID of the flow
     * @param elExpression EL expression defining the flow
     */
    public void registerFlow(String flowId, String elExpression) {
        flowDefinitionMap.put(flowId, elExpression);
    }

    /**
     * 获取指定ID的流程定义
     * @param flowId ID of the flow
     * @return EL expression defining the flow or null if not found
     */
    public String getFlowDefinition(String flowId) {
        return flowDefinitionMap.get(flowId);
    }

    /**
     * 检测指定ID的流程定义是否已注册
     * @param flowId ID of the flow
     * @return true if flow exists, false otherwise
     */
    public boolean hasFlow(String flowId) {
        return flowDefinitionMap.containsKey(flowId);
    }

    /**
     * 移除指定ID的流程定义
     * @param flowId ID of the flow to remove
     */
    public void removeFlow(String flowId) {
        flowDefinitionMap.remove(flowId);
    }

    /**
     * 获取所有已注册的组件ID
     */
    public List<String> getComponentIds() {
        return new ArrayList<>(componentMap.keySet());
    }

    /**
     * 获取所有已注册的流程ID
     */
    public List<String> getFlowIds() {
        return new ArrayList<>(flowDefinitionMap.keySet());
    }

    /**
     * 清除所有注册的组件和流程定义
     */
    public void clear() {
        componentMap.clear();
        flowDefinitionMap.clear();
    }

    /**
     * 获取已注册组件的数量
     */
    public int getComponentCount() {
        return componentMap.size();
    }

    /**
     * 获取已注册流程定义的数量
     */
    public int getFlowCount() {
        return flowDefinitionMap.size();
    }
}
