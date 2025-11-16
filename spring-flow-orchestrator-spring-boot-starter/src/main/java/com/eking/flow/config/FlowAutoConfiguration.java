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
 * Spring Boot auto-configuration for flow orchestrator.
 */
@Configuration
@ConditionalOnClass({FlowExecutor.class, FlowBus.class})
@EnableConfigurationProperties(FlowProperties.class)
@ConditionalOnProperty(name = "flow.orchestrator.enabled", havingValue = "true", matchIfMissing = true)
public class FlowAutoConfiguration {

    /**
     * Create FlowExecutor bean
     */
    @Bean
    @ConditionalOnMissingBean
    public FlowExecutor flowExecutor() {
        return new FlowExecutor();
    }

    /**
     * Create FlowBus bean
     */
    @Bean
    @ConditionalOnMissingBean
    public FlowBus flowBus() {
        return FlowBus.getInstance();
    }

    /**
     * Auto-register NodeComponent beans
     */
    @Configuration
    @ConditionalOnClass(NodeComponent.class)
    public static class ComponentRegistrationAutoConfiguration {

        private final FlowBus flowBus;

        public ComponentRegistrationAutoConfiguration(FlowBus flowBus) {
            this.flowBus = flowBus;
        }

        /**
         * Register all NodeComponent beans automatically
         */
        @Bean
        public ComponentRegistrar componentRegistrar(Map<String, NodeComponent> componentMap) {
            return new ComponentRegistrar(componentMap, flowBus);
        }
    }

    /**
     * Helper class to register all NodeComponent beans
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
