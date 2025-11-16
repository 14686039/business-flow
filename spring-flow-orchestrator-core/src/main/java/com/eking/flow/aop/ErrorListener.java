package com.eking.flow.aop;

import org.springframework.core.Ordered;

public interface ErrorListener extends Ordered {
    void onError(Object source);
}