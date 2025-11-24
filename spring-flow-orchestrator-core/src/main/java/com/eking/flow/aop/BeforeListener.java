package com.eking.flow.aop;

import org.springframework.core.Ordered;

public interface BeforeListener extends Ordered {
    /**
     * 在流程执行前调用
     * @param source 流程源对象
     */
    void onBefore(Object source);
}