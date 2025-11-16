package com.eking.flow.aop;

import org.springframework.core.Ordered;

public interface BeforeListener extends Ordered {
    void onBefore(Object source);
}