package com.eking.flow.slot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Slot for context isolation in concurrent execution.
 * Inspired by LiteFlow's Slot design.
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
     * Get slot ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Get start time
     */
    public Long getStartTime() {
        return startTime;
    }

    /**
     * Get end time
     */
    public Long getEndTime() {
        return endTime;
    }

    /**
     * Set end time
     */
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    /**
     * Get execution duration in milliseconds
     */
    public Long getDuration() {
        if (endTime == null) {
            return System.currentTimeMillis() - startTime;
        }
        return endTime - startTime;
    }

    /**
     * Get data by key
     */
    @SuppressWarnings("unchecked")
    public <T> T getData(String key) {
        return (T) data.get(key);
    }

    /**
     * Set data by key
     */
    public void setData(String key, Object value) {
        data.put(key, value);
    }

    /**
     * Remove data by key
     */
    public void removeData(String key) {
        data.remove(key);
    }

    /**
     * Check if data exists for key
     */
    public boolean hasData(String key) {
        return data.containsKey(key);
    }

    /**
     * Get all data
     */
    public Map<String, Object> getData() {
        return new HashMap<>(data);
    }

    /**
     * Clear all data
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
