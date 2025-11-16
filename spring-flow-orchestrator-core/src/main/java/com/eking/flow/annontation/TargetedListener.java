package com.eking.flow.annontation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于标记一个监听器具体关注的是哪个组件。
 * 监听器实现类使用此注解来“声明”自己的目标。
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetedListener {
    /**
     * 指定监听器所关注的目标组件的 class。
     * 例如 @TargetedListener(A.class)
     * @return 目标组件的 Class 对象
     */
    Class<?> value();
}