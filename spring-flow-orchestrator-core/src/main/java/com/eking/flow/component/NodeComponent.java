package com.eking.flow.component;

import com.eking.flow.context.FlowContext;
import com.eking.flow.slot.Slot;

/**
 * 基础组件类，所有用户自定义组件必须继承自该类。
 *
 */
public abstract class NodeComponent {

    private String id;
    private Slot slot;
    private FlowContext context;
    private boolean continueOnError = false;

    /**
     * 主要负责处理组件的业务逻辑。
     * 组件在执行时，会先调用beforeProcess()，然后执行process()，最后调用afterProcess()。
     * 如果process()抛出异常，会调用onError()处理异常。
     */
    public abstract void process() throws Exception;

    /**
     * 组件执行前调用，用于初始化组件状态或准备数据。
     * 可以在子类中重写此方法以实现自定义逻辑。
     */
    public void beforeProcess() throws Exception {
        // Default: no-op
    }

    /**
     * 组件执行后调用，用于清理资源或处理结果。
     * 可以在子类中重写此方法以实现自定义逻辑。
     */
    public void afterProcess() throws Exception {
        // Default: no-op
    }

    /**
     * 组件执行过程中抛出异常时调用，用于处理异常。
     * 可以在子类中重写此方法以实现自定义逻辑。
     * 默认行为是抛出异常。
     */
    public void onError(Exception e) throws Exception {
        throw e;
    }

    /**
     * 获取组件的ID。
     * @return 组件的ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置组件的ID。
     * @param id 组件的ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取当前组件所在的槽位。
     * @return 当前组件所在的槽位
     */
    public Slot getSlot() {
        return slot;
    }

    /**
     * 设置当前组件所在的槽位。
     * @param slot 当前组件所在的槽位
     */
    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    /**
     * 获取当前组件的上下文。
     * @return 当前组件的上下文
     */
    public FlowContext getContext() {
        return context;
    }

    /**
     * 设置当前组件的上下文。
     * @param context 当前组件的上下文
     */
    public void setContext(FlowContext context) {
        this.context = context;
    }

    /**
     * 获取当前组件所在槽位的数据。
     * @param key 数据的键
     * @return 对应键的数据值
     */
    public <T> T getData(String key) {
        return slot != null ? slot.getData(key) : null;
    }

    /**
     * 设置当前组件所在槽位的数据。
     * @param key 数据的键
     * @param value 对应键的数据值
     */
    public void setData(String key, Object value) {
        if (slot != null) {
            slot.setData(key, value);
        }
    }

    /**
     * 设置当前组件是否继续执行，即使在执行过程中抛出异常。
     * @param continueOnError 是否继续执行
     * @return 当前组件实例
     */
    public NodeComponent setContinueOnError(boolean continueOnError) {
        this.continueOnError = continueOnError;
        return this;
    }

    /**
     * 检查当前组件是否继续执行，即使在执行过程中抛出异常。
     * @return 是否继续执行
     */
    public boolean isContinueOnError() {
        return continueOnError;
    }

    /**
     * 获取当前组件的名称，用于日志记录等场景。
     * @return 当前组件的名称
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
