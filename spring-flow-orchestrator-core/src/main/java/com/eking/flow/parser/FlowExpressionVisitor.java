// Generated from com/orchestrator/flow/parser/FlowExpression.g4 by ANTLR 4.13.1
package com.eking.flow.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link FlowExpressionParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface FlowExpressionVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link FlowExpressionParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(FlowExpressionParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AssignmentStatement}
	 * labeled alternative in {@link FlowExpressionParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentStatement(FlowExpressionParser.AssignmentStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FlowStatement}
	 * labeled alternative in {@link FlowExpressionParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowStatement(FlowExpressionParser.FlowStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link FlowExpressionParser#flow}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlow(FlowExpressionParser.FlowContext ctx);
	/**
	 * Visit a parse tree produced by {@link FlowExpressionParser#conditional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditional(FlowExpressionParser.ConditionalContext ctx);
	/**
	 * Visit a parse tree produced by {@link FlowExpressionParser#parallel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParallel(FlowExpressionParser.ParallelContext ctx);
	/**
	 * Visit a parse tree produced by {@link FlowExpressionParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(FlowExpressionParser.AtomContext ctx);
}