package com.eking.flow.executor;

import com.eking.flow.bus.FlowBus;
import com.eking.flow.component.NodeComponent;
import com.eking.flow.context.FlowContext;
import com.eking.flow.execution.ExecutionPlan;
import com.eking.flow.execution.ParallelExecutor;
import com.eking.flow.parser.ANTLR4FlowParser;
import com.eking.flow.response.LiteflowResponse;
import com.eking.flow.routing.RoutingResult;
import com.eking.flow.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Main execution engine for flow orchestration.
 * Inspired by LiteFlow's FlowExecutor design.
 * Now using ANTLR4-based parser for enterprise-grade nested expression support.
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
     * Execute a flow with a custom context
     */
    public LiteflowResponse execute(String flowId, FlowContext context) {
        logger.info("Starting flow execution: {}", flowId);

        LiteflowResponse response = LiteflowResponse.success();
        Slot slot = new Slot();
        response.setSlot(slot);
        response.setContext(context);

        try {
            // Get flow definition
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

            // Parse EL expression to execution plan
            logger.debug("Using ANTLR4 parser");
            ExecutionPlan plan = flowParser.parse(elExpression);
            logger.info("Parsed flow {} into plan: {}", flowId, plan);

            // Execute flow based on plan
            executeFlow(plan, response, slot);

            // Mark slot as completed
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
     * Execute flow based on execution plan
     */
    private void executeFlow(ExecutionPlan plan, LiteflowResponse response, Slot slot) throws Exception {
        Set<String> executedComponents = new HashSet<>();
        int index = 0;

        while (index < plan.getSequentialComponents().size() && response.isSuccess()) {
            String componentId = plan.getSequentialComponents().get(index);
            index++;

            // Check if this component has parallel branches (fork node)
            List<String> branches = plan.getParallelBranches(componentId);
            if (branches != null && !branches.isEmpty()) {
                // This is a fork node - execute branches in parallel
                logger.debug("Executing parallel branches for fork node: {}", componentId);

                // Execute the fork node itself
                executeComponent(componentId, response, slot);
                executedComponents.add(componentId);

                if (response.isSuccess()) {
                    // Execute all branches in parallel
                    parallelExecutor.executeParallel(
                        branches,
                        (branchId) -> executeComponent(branchId, response, slot),
                        response,
                        slot
                    );

                    // Mark all branches as executed
                    executedComponents.addAll(branches);

                    // Find and execute the join node (next component)
                    if (index < plan.getSequentialComponents().size()) {
                        String joinNodeId = plan.getSequentialComponents().get(index);
                        logger.debug("Executing join node: {}", joinNodeId);
                        executeComponent(joinNodeId, response, slot);
                        executedComponents.add(joinNodeId);
                        index++; // Skip the join node as it's already executed
                    }
                }
            } else {
                // Regular sequential execution
                executeComponent(componentId, response, slot);
                executedComponents.add(componentId);

                // Check if this was a routing component
                RoutingResult routingResult = checkRouting(componentId, response);
                if (routingResult != null && routingResult.shouldContinue()) {
                    String targetId = routingResult.getTargetComponentId();

                    // Handle stop routing
                    if ("__STOP__".equals(targetId)) {
                        logger.debug("Routing component {} requested to stop execution", componentId);
                        break;
                    }

                    // Check if target is in the plan
                    if (targetId != null && !executedComponents.contains(targetId)) {
                        logger.debug("Routing to component: {}", targetId);
                        executeComponent(targetId, response, slot);
                        executedComponents.add(targetId);
                    }
                }
            }

            // Check if we should stop on error
            if (!response.isSuccess() && !response.getContext().isContinueOnError()) {
                logger.warn("Flow execution stopped due to component failure: {}", componentId);
                break;
            }
        }
    }

    /**
     * Check if component is a routing component and get routing result
     */
    private RoutingResult checkRouting(String componentId, LiteflowResponse response) {
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
     * Execute a flow without context
     */
    public LiteflowResponse execute(String flowId) {
        return execute(flowId, new DefaultFlowContext());
    }

    /**
     * Execute a single component
     */
    public LiteflowResponse executeComponent(String componentId, FlowContext context) {
        LiteflowResponse response = LiteflowResponse.success();
        Slot slot = new Slot();
        executeComponent(componentId, response, slot);
        return response;
    }

    /**
     * Execute a single component with response and slot
     */
    private void executeComponent(String componentId, LiteflowResponse response, Slot slot) {
        try {
            // Get component from registry
            NodeComponent component = flowBus.getComponent(componentId);
            if (component == null) {
                String errorMsg = "Component not found: " + componentId;
                logger.error(errorMsg);
                response.setSuccess(false);
                response.setMessage(errorMsg);
                return;
            }

            // Setup component with context and slot
            component.setContext(response.getContext());
            component.setSlot(slot);

            logger.debug("Executing component: {}", componentId);

            // Execute component lifecycle
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
     * Default context implementation
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
