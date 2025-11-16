package com.eking.flow.execution;

import com.eking.flow.response.LiteflowResponse;
import com.eking.flow.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * Manages parallel execution of multiple branches.
 */
public class ParallelExecutor {

    private static final Logger logger = LoggerFactory.getLogger(ParallelExecutor.class);

    private final ExecutorService executorService;

    public ParallelExecutor(int threadPoolSize) {
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    /**
     * Execute multiple components in parallel
     */
    public void executeParallel(List<String> componentIds,
                                RunnableComponentExecutor executor,
                                LiteflowResponse response,
                                Slot slot) throws Exception {

        if (componentIds == null || componentIds.isEmpty()) {
            return;
        }

        if (componentIds.size() == 1) {
            // Single component, execute sequentially
            executor.execute(componentIds.get(0));
            return;
        }

        logger.info("Executing {} branches in parallel: {}", componentIds.size(), componentIds);

        List<Future<Void>> futures = new ArrayList<>();
        List<String> completedBranches = Collections.synchronizedList(new ArrayList<>());

        // Submit all branch executions
        for (String componentId : componentIds) {
            Future<Void> future = executorService.submit(() -> {
                try {
                    executor.execute(componentId);
                    completedBranches.add(componentId);
                    logger.debug("Branch completed: {}", componentId);
                } catch (Exception e) {
                    logger.error("Error in branch: " + componentId, e);
                    throw e;
                }
                return null;
            });
            futures.add(future);
        }

        // Wait for all branches to complete
        try {
            for (Future<Void> future : futures) {
                future.get(); // This will throw exception if any branch failed
            }

            // Store completed branches in slot for join node
            slot.setData("__completed_branches__", completedBranches);

            logger.info("All {} branches completed successfully", componentIds.size());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Parallel execution interrupted");
            throw e;
        } catch (ExecutionException e) {
            logger.error("One or more branches failed", e);
            throw e;
        }
    }

    /**
     * Shutdown the executor
     */
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Functional interface for executing a component
     */
    @FunctionalInterface
    public interface RunnableComponentExecutor {
        void execute(String componentId) throws Exception;
    }
}
