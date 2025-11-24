package com.eking.flow.aop;

import org.springframework.core.Ordered;

public interface ErrorListener extends Ordered {
    /**
     * 在流程执行出错时调用
     * @param source 流程源对象
     */
    void onError(Object source);
}