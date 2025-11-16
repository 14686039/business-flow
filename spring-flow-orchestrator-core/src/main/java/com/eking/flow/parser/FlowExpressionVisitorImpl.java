package com.eking.flow.parser;

import com.eking.flow.ast.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Visitor implementation for FlowExpression grammar
 * Converts Parse Tree to AST (FlowComponent)
 */
public class FlowExpressionVisitorImpl extends FlowExpressionBaseVisitor<FlowComponent>{

    // 用于存储已定义的变量/子流程，如 X = A->B
    private final Map<String, FlowComponent> definitions;

    public FlowExpressionVisitorImpl(Map<String, FlowComponent> definitions) {
        this.definitions = definitions != null ? definitions : new HashMap<>();
    }

    public FlowExpressionVisitorImpl() {
        this(new HashMap<>());
    }

    // 处理顺序流程: A -> B -> C
    @Override
    public FlowComponent visitFlow(FlowExpressionParser.FlowContext ctx) {
        if (ctx.conditional().size() > 1) {
            List<FlowComponent> steps = ctx.conditional().stream()
                    .map(this::visit)
                    .collect(Collectors.toList());
            return new SequenceComponent(steps);
        }
        return visit(ctx.conditional(0));
    }

    // 处理分支流程: A ? B : C
    @Override
    public FlowComponent visitConditional(FlowExpressionParser.ConditionalContext ctx) {
        if (ctx.QMARK() != null) {
            FlowComponent condition = visit(ctx.parallel());
            FlowComponent trueBranch = visit(ctx.conditional(0));
            FlowComponent falseBranch = visit(ctx.conditional(1));
            return new ConditionalComponent(condition, trueBranch, falseBranch);
        }
        return visit(ctx.parallel());
    }

    // 处理并行流程: (A, B, C)
    @Override
    public FlowComponent visitParallel(FlowExpressionParser.ParallelContext ctx) {
        if (ctx.LPAREN() != null && ctx.flow().size() > 0) {
            List<FlowComponent> branches = ctx.flow().stream()
                    .map(this::visit)
                    .collect(Collectors.toList());
            return new ParallelComponent(branches);
        }
        return visit(ctx.atom());
    }

    // 处理原子单元: ID 或 (flow)
    @Override
    public FlowComponent visitAtom(FlowExpressionParser.AtomContext ctx) {
        if (ctx.ID() != null) {
            String id = ctx.ID().getText();
            // 如果 ID 是一个已定义的变量，则返回其对应的 AST
            if (definitions.containsKey(id)) {
                return definitions.get(id);
            }
            return new SimpleComponent(id);
        }
        // 如果是 (flow)，则直接访问其内部的 flow
        return visit(ctx.flow());
    }
}
