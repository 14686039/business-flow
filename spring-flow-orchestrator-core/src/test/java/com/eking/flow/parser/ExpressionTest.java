package com.eking.flow.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Test different ANTLR4 flow expression patterns
 */
public class ExpressionTest {

    private static final Logger logger = LoggerFactory.getLogger(ExpressionTest.class);

    private ANTLR4FlowParser parser = new ANTLR4FlowParser();

    private List<String> testCases = new ArrayList<>();

    public void addTestCase(String expression) {
        testCases.add(expression);
    }

    public void runAllTests() {
        logger.info("========================================");
        logger.info("Testing ANTLR4 Flow Expression Parser");
        logger.info("========================================");

        for (int i = 0; i < testCases.size(); i++) {
            String expression = testCases.get(i);
            logger.info("\nTest Case {}: {}", i + 1, expression);
            logger.info("-".repeat(50));

            try {
                parser.parseAndPrint(expression);
                logger.info("✓ SUCCESS");
            } catch (Exception e) {
                logger.error("✗ FAILED: {}", e.getMessage(), e);
            }
        }

        logger.info("\n========================================");
        logger.info("Test Summary: {}/{} passed", getPassedCount(), testCases.size());
        logger.info("========================================");
    }

    private int getPassedCount() {
        // This is a placeholder - in real implementation we'd track results
        return 0;
    }

    public static void main(String[] args) {
        ExpressionTest test = new ExpressionTest();

        // Test 1: Sequential execution A -> B -> C
        test.addTestCase("A -> B -> C");

        // Test 2: Conditional execution A -> B ? C : D
        test.addTestCase("A -> B ? C : D");

        // Test 3: Parallel execution A -> (B, C) -> D
        test.addTestCase("A -> (B, C) -> D");

        // Test 4: Mixed execution with fork-join A -> FORK (B, C) JOIN -> D
        test.addTestCase("A -> FORK (B, C) JOIN D");

        // Test 5: Complex nested expression
        test.addTestCase("A -> (B -> C, D -> E ? F : G) -> H -> I");

        // Test 6: The original failing case
        test.addTestCase("A -> B -> D -> FORK (F1, F2) JOIN G -> H -> I -> J");

        // Test 7: Variable assignment and reference
        test.addTestCase("X = A -> B ? C : D");
        test.addTestCase("M -> X -> N");

        // Test 8: Simple FORK-JOIN
        test.addTestCase("FORK (A, B) JOIN C");

        test.runAllTests();
    }
}
