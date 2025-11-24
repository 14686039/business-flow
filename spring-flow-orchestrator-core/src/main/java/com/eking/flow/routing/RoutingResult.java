package com.eking.flow.routing;

/**
 * 表示路由决策的结果。
 */
public class RoutingResult {

    private String targetComponentId;
    private boolean shouldContinue;

    private RoutingResult(String targetComponentId, boolean shouldContinue) {
        this.targetComponentId = targetComponentId;
        this.shouldContinue = shouldContinue;
    }

    /**
     * 创建一个路由结果，继续执行到指定组件
     */
    public static RoutingResult continueTo(String targetComponentId) {
        return new RoutingResult(targetComponentId, true);
    }

    /**
     * 创建一个路由结果，停止执行
     */
    public static RoutingResult stop() {
        return new RoutingResult(null, false);
    }

    /**
     * 获取目标组件 ID
     */
    public String getTargetComponentId() {
        return targetComponentId;
    }

    /**
     * 判断是否应该继续执行
     */
    public boolean shouldContinue() {
        return shouldContinue;
    }
}
