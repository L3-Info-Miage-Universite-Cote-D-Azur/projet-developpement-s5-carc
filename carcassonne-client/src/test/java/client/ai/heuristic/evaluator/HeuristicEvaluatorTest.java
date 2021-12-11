package client.ai.heuristic.evaluator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HeuristicEvaluatorTest {
    @Test
    void testAddScore() {
        HeuristicEvaluator heuristicEvaluator = new HeuristicEvaluator();
        heuristicEvaluator.addScore(10);
        heuristicEvaluator.addScore(10);
        assertEquals(20, heuristicEvaluator.finalizeScore());
    }

    @Test
    void testAddPenalty() {
        HeuristicEvaluator heuristicEvaluator = new HeuristicEvaluator();
        heuristicEvaluator.addPenalty(10);
        heuristicEvaluator.addPenalty(10);
        assertEquals(-20, heuristicEvaluator.finalizeScore());
    }

    @Test
    void testAddScoreAndPenalty() {
        HeuristicEvaluator heuristicEvaluator = new HeuristicEvaluator();
        heuristicEvaluator.addScore(100);
        heuristicEvaluator.addPenalty(10);
        assertEquals(90, heuristicEvaluator.finalizeScore());
    }

    @Test
    void testMultiplyScore() {
        HeuristicEvaluator heuristicEvaluator = new HeuristicEvaluator();
        heuristicEvaluator.addScore(10);
        heuristicEvaluator.multiplyScore(10);
        heuristicEvaluator.addScore(20);
        assertEquals(120, heuristicEvaluator.finalizeScore());
    }

    @Test
    void testSetMultiplier() {
        HeuristicEvaluator heuristicEvaluator = new HeuristicEvaluator();
        heuristicEvaluator.addScore(10);
        heuristicEvaluator.setMultiplier(10);
        heuristicEvaluator.addScore(50);
        heuristicEvaluator.resetMultiplier();
        heuristicEvaluator.addScore(10);
        assertEquals(520, heuristicEvaluator.finalizeScore());
    }
}
