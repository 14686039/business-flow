package com.eking.flow.aop;

import org.springframework.core.Ordered;

public interface AfterListener extends Ordered {
    /**
     * 在流程执行后调用
     * @param source 流程源对象
     */
    void onAfter(Object source);
}