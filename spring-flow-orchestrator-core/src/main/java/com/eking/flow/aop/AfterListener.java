package com.eking.flow.aop;

import org.springframework.core.Ordered;

public interface AfterListener extends Ordered {
    void onAfter(Object source);
}