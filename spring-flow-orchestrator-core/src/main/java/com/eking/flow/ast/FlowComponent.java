package com.eking.flow.ast;

import com.eking.flow.execution.ExecutionPlan;

/**
 *
 * 所有流程组件的基础接口
 */
public interface FlowComponent {
    /**
     *
     * 转换为字符串表示
     */
    String toString();

    /**
     *
     * 将组件转换为执行计划格式
     * @return 执行计划
     */
    ExecutionPlan toExecutionPlan();
}
