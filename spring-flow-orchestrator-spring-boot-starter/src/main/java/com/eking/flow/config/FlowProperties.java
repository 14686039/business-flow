package com.eking.flow.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for flow orchestrator.
 * Inspired by LiteFlow's LiteflowConfig design.
 */
@ConfigurationProperties(prefix = "flow.orchestrator")
public class FlowProperties {

    /**
     * Enable flow orchestrator
     */
    private boolean enabled = true;

    /**
     * Slot size for context isolation
     */
    private int slotSize = 1024;

    /**
     * Enable detailed logging
     */
    private boolean printExecutionLog = true;

    /**
     * Default thread pool size
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
