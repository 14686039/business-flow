package com.eking.flow.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test class for ANTLR4 Flow Parser
 */
public class ANTLR4ParserTest {

    private static final Logger logger = LoggerFactory.getLogger(ANTLR4ParserTest.class);

    public static void main(String[] args) {
        ANTLR4FlowParser parser = new ANTLR4FlowParser();

        System.out.println("Testing ANTLR4 Flow Parser");
        System.out.println("========================================\n");

        // Test 1: Simple sequential execution
        testParser(parser, "A -> B -> C");

        // Test 2: Conditional execution
        testParser(parser, "A->B->C?E:H->I->J");

        // Test 3: Parallel execution
        testParser(parser, "A -> (B, C) -> D");

        // Test 4: Complex nested expression
        testParser(parser, "A -> (B -> C, D -> E ? F : G) -> H");

        // Test 5: FORK...JOIN with parentheses
        testParser(parser, "A -> FORK (B, C) JOIN D");

        // Test 6: FORK...JOIN without parentheses
        testParser(parser, "A -> FORK B,C JOIN D");

        // Test 7: The target complex expression
        testParser(parser, "A -> FORK(F1 -> F2, F3 -> F4 ? F5 : F6) JOIN X -> Y");

        System.out.println("\n========================================");
        System.out.println("All tests completed!");
    }

    private static void testParser(ANTLR4FlowParser parser, String expression) {
        System.out.println("\n----------------------------------------");
        System.out.println("Testing: {}"+ expression);
        System.out.println("----------------------------------------");

        try {
            parser.parseAndPrint(expression);
            System.out.println("✓ Success");
        } catch (Exception e) {
            System.out.println("✗ Failed: {}"+ e.getMessage()+e);
        }
    }
}
