package com.eking.flow.context;

/**
 * 流程上下文是一个用于存储流程执行过程中所需数据的容器。
 * 它可以在流程的不同组件之间传递数据，也可以在组件执行过程中存储临时数据。
 * 流程上下文通常在流程开始时创建，在流程结束时销毁。
 */
public abstract class FlowContext {

    private Long requestId;

    /**
     * 获取请求ID。
     * 请求ID通常用于标识一个流程实例，方便在日志、监控等场景中进行跟踪。
     * @return 请求ID
     */
    public Long getRequestId() {
        return requestId;
    }

    /**
     * 设置请求ID。
     * 请求ID通常用于标识一个流程实例，方便在日志、监控等场景中进行跟踪。
     * @param requestId 请求ID
     */
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    /**
     * 初始化上下文。
     * 可以在子类中重写此方法以实现自定义初始化逻辑。
     */
    public void init() {
        // Default: no-op
    }

    /**
     * 销毁上下文。
     * 可以在子类中重写此方法以实现自定义销毁逻辑。
     */
    public void destroy() {
        // Default: no-op
    }

    /**
     * 检查是否在发生错误时继续执行。
     * 可以在子类中重写此方法以实现自定义行为。
     * @return 如果在发生错误时继续执行，则返回true；否则返回false
     */
    public boolean isContinueOnError() {
        return false;
    }
}
