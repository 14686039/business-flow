package com.eking.flow.config;

import com.eking.flow.bus.FlowBus;
import com.eking.flow.component.NodeComponent;
import com.eking.flow.executor.FlowExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 *
 * 自动配置类 - 配置流程执行器、消息总线和节点组件
 */
@Configuration
@ConditionalOnClass({FlowExecutor.class, FlowBus.class})
@EnableConfigurationProperties(FlowProperties.class)
@ConditionalOnProperty(name = "flow.orchestrator.enabled", havingValue = "true", matchIfMissing = true)
public class FlowAutoConfiguration {

    /**
     *
     * 创建流程执行器 bean
     */
    @Bean
    @ConditionalOnMissingBean
    public FlowExecutor flowExecutor() {
        return new FlowExecutor();
    }

    /**
     *
     * 创建消息总线 bean
     */
    @Bean
    @ConditionalOnMissingBean
    public FlowBus flowBus() {
        return FlowBus.getInstance();
    }

    /**
     *
     * 自动注册节点组件 bean
     */
    @Configuration
    @ConditionalOnClass(NodeComponent.class)
    public static class ComponentRegistrationAutoConfiguration {

        private final FlowBus flowBus;

        public ComponentRegistrationAutoConfiguration(FlowBus flowBus) {
            this.flowBus = flowBus;
        }

        /**
         *
         * 自动注册所有 NodeComponent bean
         */
        @Bean
        public ComponentRegistrar componentRegistrar(Map<String, NodeComponent> componentMap) {
            return new ComponentRegistrar(componentMap, flowBus);
        }
    }

    /**
     *
     * 帮助类 - 自动注册所有 NodeComponent bean
     */
    public static class ComponentRegistrar {

        private final Map<String, NodeComponent> componentMap;
        private final FlowBus flowBus;

        public ComponentRegistrar(Map<String, NodeComponent> componentMap, FlowBus flowBus) {
            this.componentMap = componentMap;
            this.flowBus = flowBus;
            registerComponents();
        }

        private void registerComponents() {
            componentMap.forEach((beanName, component) -> {
                // Use bean name as component ID
                flowBus.registerComponent(beanName, component);
            });
        }
    }
}
