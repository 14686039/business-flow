package com.eking.flow.aop;


import com.eking.flow.annontation.TargetedListener;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils; // 导入Spring的注解工具类
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

@Aspect
@Component
public class ComponentListenerAspect {

    private static final Logger logger = LoggerFactory.getLogger(ComponentListenerAspect.class);

    // 依然注入所有的监听器
    private final List<BeforeListener> allBeforeListeners;
    private final List<AfterListener> allAfterListeners;
    private final List<ErrorListener> allErrorListeners;

    public ComponentListenerAspect(List<BeforeListener> allBeforeListeners, List<AfterListener> allAfterListeners, List<ErrorListener> allErrorListeners) {
        this.allBeforeListeners = allBeforeListeners;
        this.allAfterListeners = allAfterListeners;
        this.allErrorListeners = allErrorListeners;
    }

    /**
     * 匹配所有 NodeComponent 及其所有子类中的任何方法执行。
     * astractComponent+ 表示 AbstractComponent 及其所有子类。
     */
    @Pointcut("within(com.eking.flow.component.NodeComponent+)")
    public void anyComponentExecution() {}

    @After("anyComponentExecution() && execution(* beforeProcess())")
    public void enhanceBefore(JoinPoint joinPoint) {
        // 调用通用的通知方法，传入所有 before 监听器
        notifyTargetedListeners(joinPoint, allBeforeListeners, listener -> listener.onBefore(joinPoint.getTarget()));
    }

    @After("anyComponentExecution() && execution(* afterProcess())")
    public void enhanceAfter(JoinPoint joinPoint) {
        // 调用通用的通知方法，传入所有 after 监听器
        notifyTargetedListeners(joinPoint, allAfterListeners, listener -> listener.onAfter(joinPoint.getTarget()));
    }

    @After("anyComponentExecution() && execution(* onError())")
    public void enhanceError(JoinPoint joinPoint) {
        // 调用通用的通知方法，传入所有 error 监听器
        notifyTargetedListeners(joinPoint, allErrorListeners, listener -> listener.onError(joinPoint.getTarget()));
    }


    /**
     * ★★★ 核心逻辑：通用的、带过滤和排序功能的监听器通知方法
     * @param joinPoint 切点信息，用于获取目标对象
     * @param allListeners 所有同类型的监听器列表
     * @param action 对过滤和排序后的监听器要执行的操作
     * @param <T> 监听器类型
     */
    private <T extends Ordered> void notifyTargetedListeners(JoinPoint joinPoint, List<T> allListeners, Consumer<T> action) {
        Object target = joinPoint.getTarget();
        Class<?> targetClass = target.getClass();

        logger.info("  (AOP ->) 截获到 {}.{}()，开始筛选监听器...", targetClass.getSimpleName(), joinPoint.getSignature().getName());

        allListeners.stream()
            // 1. 过滤：只保留那些注解了 @TargetedListener 且 value() 与当前目标类匹配的监听器
            .filter(listener -> {
                TargetedListener annotation = AnnotationUtils.findAnnotation(listener.getClass(), TargetedListener.class);
                return annotation != null && annotation.value().isAssignableFrom(targetClass);
            })
            // 2. 排序：根据 @Order 或 getOrder() 的值进行排序
            .sorted(Comparator.comparingInt(Ordered::getOrder))
            // 3. 执行：对筛选并排序后的监听器列表，执行传入的 action
            .forEach(action);
    }
}