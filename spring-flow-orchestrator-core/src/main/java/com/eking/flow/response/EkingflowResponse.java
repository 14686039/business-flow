package com.eking.flow.response;

import com.eking.flow.context.FlowContext;
import com.eking.flow.slot.Slot;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程执行响应类
 */
public class EkingflowResponse {

    private boolean success;
    private String message;
    private Exception exception;
    private FlowContext context;
    private Slot slot;
    private List<String> executedComponents;
    private Object data;

    public EkingflowResponse() {
        this.executedComponents = new ArrayList<>();
    }

    /**
     * 创建成功响应
     */
    public static EkingflowResponse success() {
        EkingflowResponse response = new EkingflowResponse();
        response.setSuccess(true);
        return response;
    }

    /**
     * 创建失败响应
     */
    public static EkingflowResponse fail(Exception exception) {
        EkingflowResponse response = new EkingflowResponse();
        response.setSuccess(false);
        response.setException(exception);
        return response;
    }

    /**
     * 创建失败响应
     */
    public static EkingflowResponse fail(String message) {
        EkingflowResponse response = new EkingflowResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    /**
     * 检查执行是否成功
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * 设置执行成功标志
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * 获取失败消息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置失败消息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取异常
     */
    public Exception getException() {
        return exception;
    }

    /**
     * 设置异常
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }

    /**
     * 获取上下文
     */
    public FlowContext getContext() {
        return context;
    }

    /**
     * 设置上下文
     */
    public void setContext(FlowContext context) {
        this.context = context;
    }

    /**
     * 获取槽位
     */
    public Slot getSlot() {
        return slot;
    }

    /**
     * 设置槽位
     */
    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    /**
     * 获取执行的组件列表
     */
    public List<String> getExecutedComponents() {
        return executedComponents;
    }

    /**
     * 添加执行的组件名称
     */
    public void addExecutedComponent(String componentName) {
        this.executedComponents.add(componentName);
    }

    /**
     * 获取响应数据
     */
    public Object getData() {
        return data;
    }

    /**
     * 设置响应数据
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
