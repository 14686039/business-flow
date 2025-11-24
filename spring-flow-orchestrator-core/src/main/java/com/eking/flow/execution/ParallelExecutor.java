package com.eking.flow.execution;

import com.eking.flow.response.EkingflowResponse;
import com.eking.flow.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * 管理并行执行多个分支的组件。
 */
public class ParallelExecutor {

    private static final Logger logger = LoggerFactory.getLogger(ParallelExecutor.class);

    private final ExecutorService executorService;

    public ParallelExecutor(int threadPoolSize) {
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    /**
     * 并行执行多个组件。
     * @param componentIds 组件ID列表
     * @param executor 组件执行器
     * @param response 流程响应对象
     * @param slot 插槽对象
     * @throws Exception 如果执行过程中发生错误
     */
    public void executeParallel(List<String> componentIds,
                                RunnableComponentExecutor executor,
                                EkingflowResponse response,
                                Slot slot) throws Exception {

        if (componentIds == null || componentIds.isEmpty()) {
            return;
        }

        if (componentIds.size() == 1) {
            // 单个组件顺序执行
            executor.execute(componentIds.get(0));
            return;
        }

        logger.info("Executing {} branches in parallel: {}", componentIds.size(), componentIds);

        List<Future<Void>> futures = new ArrayList<>();
        List<String> completedBranches = Collections.synchronizedList(new ArrayList<>());

        // 提交所有分支执行任务到线程池
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

        //  等等所有分支执行完成
        try {
            for (Future<Void> future : futures) {
                future.get(); // 等待分支执行完成，若有异常会抛出
            }

            // 存储已完成的分支到插槽，用于合并节点
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
     * 关闭并行执行器，释放线程池资源。
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
     * 函数式接口，用于执行一个组件。
     */
    @FunctionalInterface
    public interface RunnableComponentExecutor {
        void execute(String componentId) throws Exception;
    }
}
