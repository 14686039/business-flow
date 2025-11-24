package com.eking.flow.executor;

import com.eking.flow.bus.FlowBus;
import com.eking.flow.component.NodeComponent;
import com.eking.flow.context.FlowContext;
import com.eking.flow.execution.ExecutionPlan;
import com.eking.flow.execution.ParallelExecutor;
import com.eking.flow.parser.ANTLR4FlowParser;
import com.eking.flow.response.EkingflowResponse;
import com.eking.flow.routing.RoutingResult;
import com.eking.flow.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 现在使用ANTLR4解析器支持企业级嵌套表达式。
 * 支持并行执行多个分支。
 */
public class FlowExecutor {

    private static final Logger logger = LoggerFactory.getLogger(FlowExecutor.class);

    private FlowBus flowBus;
    private ANTLR4FlowParser flowParser;
    private ParallelExecutor parallelExecutor;

    public FlowExecutor() {
        this.flowBus = FlowBus.getInstance();
        this.flowParser = new ANTLR4FlowParser();
        this.parallelExecutor = new ParallelExecutor(64);
        logger.info("Initialized FlowExecutor with ANTLR4 parser");
    }

    public FlowExecutor(int threadPoolSize) {
        this.flowBus = FlowBus.getInstance();
        this.flowParser = new ANTLR4FlowParser();
        this.parallelExecutor = new ParallelExecutor(threadPoolSize);
        logger.info("Initialized FlowExecutor with ANTLR4 parser (thread pool: {})", threadPoolSize);
    }

    /**
     * 执行一个自定义上下文的流程。
     */
    public EkingflowResponse execute(String flowId, FlowContext context) {
        logger.info("Starting flow execution: {}", flowId);

        EkingflowResponse response = EkingflowResponse.success();
        Slot slot = new Slot();
        response.setSlot(slot);
        response.setContext(context);

        try {
            // 获取流程定义
            String elExpression = flowBus.getFlowDefinition(flowId);
            if (elExpression == null) {
                String errorMsg = "Flow not found: " + flowId;
                logger.error(errorMsg);
                // 确保失败的response也包含slot和context
                response.setSuccess(false);
                response.setMessage(errorMsg);
                slot.setEndTime(System.currentTimeMillis());
                return response;
            }

            // 匹配表达式到执行计划
            logger.debug("Using ANTLR4 parser");
            ExecutionPlan plan = flowParser.parse(elExpression);
            logger.info("Parsed flow {} into plan: {}", flowId, plan);

            // 执行流程
            executeFlow(plan, response, slot);

            // 标记slot为完成
            slot.setEndTime(System.currentTimeMillis());
            logger.info("Flow execution completed: {} in {}ms", flowId, slot.getDuration());

        } catch (Exception e) {
            logger.error("Error executing flow: " + flowId, e);
            // 确保异常的response也包含slot和context
            response.setSuccess(false);
            response.setException(e);
            slot.setEndTime(System.currentTimeMillis());
            return response;
        }

        return response;
    }

    /**
     * 执行基于执行计划的流程。
     */
    private void executeFlow(ExecutionPlan plan, EkingflowResponse response, Slot slot) throws Exception {
        Set<String> executedComponents = new HashSet<>();
        int index = 0;

        while (index < plan.getSequentialComponents().size() && response.isSuccess()) {
            String componentId = plan.getSequentialComponents().get(index);
            index++;

            // 检查是否有并行分支（fork节点）
            List<String> branches = plan.getParallelBranches(componentId);
            if (branches != null && !branches.isEmpty()) {
                // 这是一个fork节点 - 并行执行分支
                logger.debug("Executing parallel branches for fork node: {}", componentId);

                // 先执行fork节点本身
                executeComponent(componentId, response, slot);
                executedComponents.add(componentId);

                if (response.isSuccess()) {
                    // 并行执行所有分支
                    parallelExecutor.executeParallel(
                        branches,
                        (branchId) -> executeComponent(branchId, response, slot),
                        response,
                        slot
                    );

                    // 标记所有分支为已执行
                    executedComponents.addAll(branches);

                    // 检查是否有join节点（下一个组件）
                    if (index < plan.getSequentialComponents().size()) {
                        String joinNodeId = plan.getSequentialComponents().get(index);
                        logger.debug("Executing join node: {}", joinNodeId);
                        executeComponent(joinNodeId, response, slot);
                        executedComponents.add(joinNodeId);
                        index++; // 跳过join节点，因为它已经执行过了
                    }
                }
            } else {
                // 这不是一个fork节点 - 顺序执行
                executeComponent(componentId, response, slot);
                executedComponents.add(componentId);

                // 检查是否有路由组件
                RoutingResult routingResult = checkRouting(componentId, response);
                if (routingResult != null && routingResult.shouldContinue()) {
                    String targetId = routingResult.getTargetComponentId();

                    // 处理路由结果 - 继续执行或停止
                    if ("__STOP__".equals(targetId)) {
                        logger.debug("Routing component {} requested to stop execution", componentId);
                        break;
                    }

                    // 处理路由结果 - 继续执行目标组件
                    if (targetId != null && !executedComponents.contains(targetId)) {
                        logger.debug("Routing to component: {}", targetId);
                        executeComponent(targetId, response, slot);
                        executedComponents.add(targetId);
                    }
                }
            }

            // 处理路由结果 - 检查是否应该继续执行
            if (!response.isSuccess() && !response.getContext().isContinueOnError()) {
                logger.warn("Flow execution stopped due to component failure: {}", componentId);
                break;
            }
        }
    }

    /**
     * 检查组件是否是路由组件并获取路由结果
     */
    private RoutingResult checkRouting(String componentId, EkingflowResponse response) {
        if (!response.getSlot().hasData("__routing_target__")) {
            return null;
        }

        String target = response.getSlot().getData("__routing_target__");
        response.getSlot().removeData("__routing_target__");

        if ("__STOP__".equals(target)) {
            return RoutingResult.stop();
        }

        if (target != null) {
            return RoutingResult.continueTo(target);
        }

        return null;
    }

    /**
     * 执行一个流程而不包含上下文
     */
    public EkingflowResponse execute(String flowId) {
        return execute(flowId, new DefaultFlowContext());
    }

    /**
     * 执行一个单独的组件并返回响应
     */
    public EkingflowResponse executeComponent(String componentId, FlowContext context) {
        EkingflowResponse response = EkingflowResponse.success();
        Slot slot = new Slot();
        executeComponent(componentId, response, slot);
        return response;
    }

    /**
     * 执行一个单独的组件并返回响应
     */
    private void executeComponent(String componentId, EkingflowResponse response, Slot slot) {
        try {
            // 从总线获取组件实例
            NodeComponent component = flowBus.getComponent(componentId);
            if (component == null) {
                String errorMsg = "Component not found: " + componentId;
                logger.error(errorMsg);
                response.setSuccess(false);
                response.setMessage(errorMsg);
                return;
            }

            // 设置组件上下文和插槽
            component.setContext(response.getContext());
            component.setSlot(slot);

            logger.debug("Executing component: {}", componentId);

            // 执行组件生命周期方法
            component.beforeProcess();
            response.addExecutedComponent(component.getName());

            component.process();

            component.afterProcess();

            logger.debug("Component executed successfully: {}", componentId);

        } catch (Exception e) {
            logger.error("Error executing component: " + componentId, e);
            response.setSuccess(false);
            response.setException(e);

            try {
                NodeComponent component = flowBus.getComponent(componentId);
                if (component != null) {
                    component.onError(e);
                }
            } catch (Exception handlerError) {
                logger.error("Error in component error handler: " + componentId, handlerError);
            }
        }
    }

    /**
     * 默认流程上下文实现
     */
    private static class DefaultFlowContext extends FlowContext {
        private boolean continueOnError = false;

        public DefaultFlowContext setContinueOnError(boolean continueOnError) {
            this.continueOnError = continueOnError;
            return this;
        }

        @Override
        public boolean isContinueOnError() {
            return continueOnError;
        }
    }
}
