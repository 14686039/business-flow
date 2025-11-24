package com.eking.flow.slot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 用于并发执行中的上下文隔离。
 */
public class Slot {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);

    private final Long id;
    private final Long startTime;
    private Long endTime;
    private Map<String, Object> data;

    public Slot() {
        this.id = ID_GENERATOR.incrementAndGet();
        this.startTime = System.currentTimeMillis();
        this.data = new HashMap<>();
    }

    /**
     * 获取槽 ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 获取开始时间
     */
    public Long getStartTime() {
        return startTime;
    }

    /**
     * 获取结束时间
     */
    public Long getEndTime() {
        return endTime;
    }

    /**
     * 设置结束时间
     */
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取执行持续时间（毫秒）
     */
    public Long getDuration() {
        if (endTime == null) {
            return System.currentTimeMillis() - startTime;
        }
        return endTime - startTime;
    }

    /**
     * 获取数据
     */
    @SuppressWarnings("unchecked")
    public <T> T getData(String key) {
        return (T) data.get(key);
    }

    /**
     * 设置数据
     */
    public void setData(String key, Object value) {
        data.put(key, value);
    }

    /**
     * 移除数据
     */
    public void removeData(String key) {
        data.remove(key);
    }

    /**
     * 检查是否存在指定键的数据
     */
    public boolean hasData(String key) {
        return data.containsKey(key);
    }

    /**
     * 获取所有数据
     */
    public Map<String, Object> getData() {
        return new HashMap<>(data);
    }

    /**
     * 清除所有数据
     */
    public void clearData() {
        data.clear();
    }

    @Override
    public String toString() {
        return "Slot{" +
                "id=" + id +
                ", duration=" + getDuration() + "ms" +
                '}';
    }
}
