package com.eking.flow.parser;

import com.eking.flow.ast.FlowComponent;
import com.eking.flow.execution.ExecutionPlan;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * ANTLR4-based Flow Parser
 * Uses generated parser code and custom Visitor to convert flow expressions to ExecutionPlan
 */
public class ANTLR4FlowParser {

    private static final Logger logger = LoggerFactory.getLogger(ANTLR4FlowParser.class);

    /**
     * Parse EL expression and return execution plan
     */
    public ExecutionPlan parse(String elExpression) {
        if (elExpression == null || elExpression.trim().isEmpty()) {
            throw new IllegalArgumentException("EL expression cannot be empty");
        }

        String trimmedExpression = elExpression.trim();
        logger.debug("Parsing EL expression with ANTLR4: {}", trimmedExpression);

        try {
            // Create character stream from input
            CharStream charStream = CharStreams.fromString(trimmedExpression);

            // Create lexer
            FlowExpressionLexer lexer = new FlowExpressionLexer(charStream);

            // Create token stream
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            // Create parser
            FlowExpressionParser parser = new FlowExpressionParser(tokens);

            // Parse from program rule
            ParseTree tree = parser.program();

            // Create visitor and definitions map
            Map<String, FlowComponent> definitions = new HashMap<>();
            FlowExpressionVisitorImpl visitor = new FlowExpressionVisitorImpl(definitions);

            // Visit the parse tree
            FlowExpressionParser.ProgramContext programCtx = (FlowExpressionParser.ProgramContext) tree;

            // 用于存储最终的AST
            FlowComponent finalAst = null;

            // 遍历所有语句（赋值或流程）
            for (FlowExpressionParser.StatementContext stmtCtx : programCtx.statement()) {
                if (stmtCtx instanceof FlowExpressionParser.AssignmentStatementContext) {
                    // 处理变量赋值: X = A -> B
                    FlowExpressionParser.AssignmentStatementContext assignCtx = (FlowExpressionParser.AssignmentStatementContext) stmtCtx;
                    String varName = assignCtx.ID().getText();
                    FlowComponent component = visitor.visit(assignCtx.flow());
                    definitions.put(varName, component);
                    logger.debug("Defined variable: {} = {}", varName, component);
                } else if (stmtCtx instanceof FlowExpressionParser.FlowStatementContext) {
                    // 处理流程语句
                    finalAst = visitor.visit(stmtCtx);
                    logger.debug("Parsed AST: {}", finalAst);
                }
            }

            if (finalAst == null) {
                logger.error("No flow statement found or visitor returned null AST!");
                logger.error("Parse tree structure: {}", tree.toStringTree(parser));
                throw new RuntimeException("Visitor failed to create AST - returned null");
            }

            logger.debug("AST toString: {}", finalAst.toString());

            // Convert AST to ExecutionPlan
            ExecutionPlan plan = finalAst.toExecutionPlan();

            if (plan == null) {
                logger.error("toExecutionPlan() returned null!");
                throw new RuntimeException("Failed to convert AST to ExecutionPlan");
            }

            logger.debug("Parsed successfully: {}", plan);
            return plan;

        } catch (Exception e) {
            logger.error("Failed to parse EL expression: {}", trimmedExpression, e);
            throw new RuntimeException("Failed to parse EL expression: " + trimmedExpression, e);
        }
    }

    /**
     * Test method to parse and print AST
     */

    public void parseAndPrint(String input) {
        try {
            CharStream charStream = CharStreams.fromString(input);
            FlowExpressionLexer lexer = new FlowExpressionLexer(charStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            FlowExpressionParser parser = new FlowExpressionParser(tokens);

            ParseTree tree = parser.program(); // 从顶层规则 program 开始解析

            // 用于存储变量定义
            Map<String, FlowComponent> definitions = new HashMap<>();
            FlowExpressionVisitorImpl visitor = new FlowExpressionVisitorImpl(definitions);

            // 遍历所有语句（赋值或流程）
            for (FlowExpressionParser.StatementContext stmtCtx : ((FlowExpressionParser.ProgramContext) tree).statement()) {
                if (stmtCtx instanceof FlowExpressionParser.AssignmentStatementContext) {
                    FlowExpressionParser.AssignmentStatementContext assignCtx = (FlowExpressionParser.AssignmentStatementContext) stmtCtx;
                    String varName = assignCtx.ID().getText();
                    FlowComponent component = visitor.visit(assignCtx.flow());
                    definitions.put(varName, component);
                    System.out.println("Result: Defined " + varName + " = " + component);
                } else if (stmtCtx instanceof FlowExpressionParser.FlowStatementContext) {
                    FlowComponent finalAst = visitor.visit(stmtCtx);
                    System.out.println("Result AST: " + finalAst);
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing input: " + e.getMessage());
        }
    }
}
