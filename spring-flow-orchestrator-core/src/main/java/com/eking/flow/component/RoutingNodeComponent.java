package com.eking.flow.component;

import com.eking.flow.routing.RoutingResult;

/**
 * 路由组件是一种特殊的组件，它根据一定的规则来确定下一个要执行的组件。
 * 路由组件通常用于实现条件分支、循环等复杂的业务逻辑。
 * 路由组件在执行时，会调用route()方法来确定下一个要执行的组件。
 * 如果route()方法返回null，则表示路由结束，流程会停止执行。
 * 如果route()方法返回的RoutingResult对象的shouldContinue()方法返回false，则表示路由结束，流程会停止执行。
 * 如果route()方法返回的RoutingResult对象的shouldContinue()方法返回true，则表示路由继续，流程会继续执行下一个组件。
 */
public abstract class RoutingNodeComponent extends NodeComponent {

    /**
     * 定义路由逻辑，返回下一个要执行的组件的ID。
     * 如果返回null，则表示路由结束，流程会停止执行。
     * 如果返回的RoutingResult对象的shouldContinue()方法返回false，则表示路由结束，流程会停止执行。
     * 如果返回的RoutingResult对象的shouldContinue()方法返回true，则表示路由继续，流程会继续执行下一个组件。
     * @return RoutingResult object containing the next component ID and continue flag
     */
    public abstract RoutingResult route() throws Exception;

    /**
     * 覆盖process()方法，实现路由逻辑。
     * 在路由组件执行时，会先调用beforeProcess()，然后执行process()，最后调用afterProcess()。
     * 如果process()抛出异常，会调用onError()处理异常。
     */
    @Override
    public void process() throws Exception {
        // Execute routing logic
        RoutingResult result = route();

        if (result != null && result.shouldContinue()) {
            String targetComponentId = result.getTargetComponentId();
            if (targetComponentId != null) {
                setData("__routing_target__", targetComponentId);
            } else {
                setData("__routing_target__", "__STOP__");
            }
        }
    }
}
