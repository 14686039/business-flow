package com.eking.flow.example.complex;

import com.eking.flow.annontation.TargetedListener;
import com.eking.flow.aop.AfterListener;
import com.eking.flow.example.complex.components.ComponentA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// --- After 监听器示例 ---

@Component
@Order(1) // 数字越小，优先级越高。首先执行。
@TargetedListener(ComponentA.class)
public class MonitoringAfterListener implements AfterListener {
    private static final Logger logger = LoggerFactory.getLogger(MonitoringAfterListener.class);
    @Override
    public void onAfter(Object source) {
        logger.info("    [监听器-监控(Order 1)]: {} 执行成功，上报状态。", source.getClass().getSimpleName());
        if(source instanceof ComponentA componentA) {
            logger.info("    上下参数："+componentA.getContext().toString());
        }
    }
    @Override
    public int getOrder() { return 1; } // 也可以通过实现 getOrder() 方法返回顺序
}