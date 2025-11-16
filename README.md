# Spring Flow Orchestrator

A lightweight workflow orchestration framework for Spring Boot 3, inspired by LiteFlow. This project is designed for learning purposes to understand the core patterns and architecture of enterprise workflow engines.

## Overview

Spring Flow Orchestrator provides a simple yet powerful way to orchestrate business processes using component-based workflows. It allows you to define complex business flows using EL (Expression Language) expressions and execute them with context isolation and error handling.

## Features

- **Component-based Architecture**: Break down complex business logic into reusable components
- **EL Expression Support**: Define flows using simple expression language (THEN, AND, OR operators)
- **Context Isolation**: Slot mechanism for thread-safe concurrent execution
- **Spring Boot 3 Integration**: Auto-configuration and dependency injection support
- **Error Handling**: Flexible error handling with continue-on-error options
- **Lifecycle Hooks**: Before/after process hooks for each component

## Project Structure

This is a Maven multi-module project:

- **spring-flow-orchestrator-core** - Core orchestration engine
- **spring-flow-orchestrator-spring-boot-starter** - Spring Boot auto-configuration
- **spring-flow-orchestrator-example** - Example application demonstrating usage

## Quick Start

### 1. Add Dependency

Add to your Spring Boot 3 project:

```xml
<dependency>
    <groupId>com.orchestrator</groupId>
    <artifactId>spring-flow-orchestrator-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. Configuration

Create a custom context class:

```java
public class MyContext extends FlowContext {
    private String data;

    // getters and setters
}
```

### 3. Create Components

Extend `NodeComponent` and annotate with `@Component`:

```java
@Component("step1")
public class Step1Component extends NodeComponent {
    @Override
    public void process() {
        // Your business logic here
        MyContext context = (MyContext) getContext();
        // Process context.getData()
    }
}

@Component("step2")
public class Step2Component extends NodeComponent {
    @Override
    public void process() {
        // Your business logic here
    }
}
```

### 4. Register and Execute Flow

```java
@Service
public class MyService {

    @Autowired
    private FlowExecutor flowExecutor;

    @Autowired
    private FlowBus flowBus;

    public void executeFlow() {
        // Register flow definition
        flowBus.registerFlow("myFlow", "step1 THEN step2");

        // Create context
        MyContext context = new MyContext();
        context.setData("test data");

        // Execute flow
        LiteflowResponse response = flowExecutor.execute("myFlow", context);

        // Check result
        if (response.isSuccess()) {
            System.out.println("Flow executed successfully!");
        } else {
            System.out.println("Flow failed: " + response.getMessage());
        }
    }
}
```

## Architecture

### Core Components

1. **NodeComponent** - Base class for all workflow components
   - `process()` - Main execution logic
   - `beforeProcess()` - Pre-execution hook
   - `afterProcess()` - Post-execution hook
   - `onError()` - Error handling hook

2. **FlowExecutor** - Main execution engine
   - Parses EL expressions
   - Executes components sequentially
   - Manages context and slot lifecycle
   - Handles errors and exceptions

3. **FlowBus** - Central registry
   - Registers components and flows
   - Singleton pattern for global access
   - Thread-safe component lookup

4. **FlowContext** - Context abstraction
   - Holds business data
   - Passed to all components
   - Supports custom context classes

5. **Slot** - Context isolation mechanism
   - Provides data storage
   - Tracks execution metrics
   - Enables concurrent execution

6. **LiteflowResponse** - Execution result wrapper
   - Success/failure status
   - Execution metrics
   - List of executed components

### EL Expression Syntax

#### Basic Patterns

**Sequential Execution (Chain)**
```java
"step1 THEN step2 THEN step3"
"step1 -> step2 -> step3"
```

**Conditional Branching**
```java
"router ? trueBranch : falseBranch"
"validateAmount THEN processPayment ? confirmOrder : cancelOrder"
```

**Fork/Join (Parallel Execution)**
```java
"FORK(branch1, branch2, branch3) JOIN aggregator"
"validateOrder FORK(processInventory, processPayment, sendNotification) JOIN aggregateResults"
```

#### Advanced Patterns

**Nested Expressions**
```java
"a->b->FORK(c,d)JOIN e"              // Sequential + Parallel
"a->b?c:d"                            // Sequential + Conditional
"validateOrder FORK(processInventory, processPayment) JOIN sendConfirmation"
"amountRouter THEN processPayment ? premiumBranch : standardBranch"
```

**Complex Combined**
```java
// The parser supports arbitrarily nested combinations:
"start -> validate -> FORK(task1, task2) JOIN aggregate -> route ? success : failure -> end"
```

### Configuration Properties

```yaml
flow:
  orchestrator:
    enabled: true              # Enable/disable the orchestrator
    slot-size: 1024            # Context slot size
    print-execution-log: true  # Enable detailed logging
    thread-pool-size: 64       # Thread pool size
```

## Building the Project

### Build All Modules
```bash
mvn clean install
```

### Build Specific Module
```bash
mvn clean install -pl spring-flow-orchestrator-core -am
```

### Run Example
```bash
cd spring-flow-orchestrator-example
mvn spring-boot:run
```

### Run Tests
```bash
mvn test
```

## Example Flow: Order Processing

See `spring-flow-orchestrator-example` for a complete order processing flow:

1. **ValidateOrder** - Validates order data
2. **ProcessPayment** - Processes payment
3. **SendConfirmation** - Sends confirmation email

Flow definition:
```java
flowBus.registerFlow("orderFlow", "validateOrder THEN processPayment THEN sendConfirmation");
```

## Learning Points

This project demonstrates several key enterprise patterns:

1. **Singleton Registry Pattern** - FlowBus uses singleton for global component access
2. **Context Object Pattern** - FlowContext and Slot for data isolation
3. **Template Method Pattern** - NodeComponent lifecycle hooks
4. **Strategy Pattern** - Swappable flow execution strategies
5. **Spring Boot Auto-configuration** - Automatic bean registration and configuration

## Comparison with LiteFlow

| Feature | LiteFlow | Spring Flow Orchestrator |
|---------|----------|-------------------------|
| Components | Full-featured | Simplified |
| EL Support | Advanced | Basic (THEN/AND/OR) |
| Rule Sources | Multiple (ZK, Nacos, etc.) | In-memory only |
| Script Languages | 9+ languages | Not supported |
| Hot Deployment | Yes | No |
| Complexity | Production-ready | Learning-oriented |

## Requirements

- Java 17+
- Spring Boot 3.2+
- Maven 3.6+

## Limitations

This is a learning project with the following limitations:

1. **No Hot Deployment** - Flows must be registered at startup
2. **No External Rule Sources** - Only in-memory flow definitions
3. **No Script Language Support** - Components must be Java classes
4. **Sequential Execution** - No true parallel execution (yet)
5. **No Monitoring** - Basic logging only
6. **No Retry Mechanism** - Simple error handling only

## Extending the Framework

To add new features:

1. **Parallel Execution** - Extend FlowParser to build execution graphs
2. **External Sources** - Implement RuleSource interface for loading from files/DB
3. **Script Languages** - Add ScriptComponent for non-Java logic
4. **Monitoring** - Add metrics collection to FlowExecutor
5. **Retry Logic** - Implement retry policies in NodeComponent

## Contributing

This is a learning project. Feel free to:
- Study the code to understand workflow engine patterns
- Use it as a base for your own experiments
- Submit improvements or new features

## License

Apache License 2.0

## References

- [LiteFlow Official](https://liteflow.cc/)
- Spring Boot Documentation
- Enterprise Integration Patterns

## Author

Created for learning purposes, inspired by the excellent work of the LiteFlow team.
