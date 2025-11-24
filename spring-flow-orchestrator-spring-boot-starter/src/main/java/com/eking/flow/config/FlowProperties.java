package com.eking.flow.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 流程协调器配置属性
 */
@ConfigurationProperties(prefix = "flow.orchestrator")
public class FlowProperties {

    /**
     * 是否启用流程协调器
     */
    private boolean enabled = true;

    /**
     * 上下文隔离槽位大小
     */
    private int slotSize = 1024;

    /**
     * 是否启用详细执行日志打印
     */
    private boolean printExecutionLog = true;

    /**
     * 默认线程池大小
     */
    private int threadPoolSize = 64;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getSlotSize() {
        return slotSize;
    }

    public void setSlotSize(int slotSize) {
        this.slotSize = slotSize;
    }

    public boolean isPrintExecutionLog() {
        return printExecutionLog;
    }

    public void setPrintExecutionLog(boolean printExecutionLog) {
        this.printExecutionLog = printExecutionLog;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    @Override
    public String toString() {
        return "FlowProperties{" +
                "enabled=" + enabled +
                ", slotSize=" + slotSize +
                ", printExecutionLog=" + printExecutionLog +
                ", threadPoolSize=" + threadPoolSize +
                '}';
    }
}
