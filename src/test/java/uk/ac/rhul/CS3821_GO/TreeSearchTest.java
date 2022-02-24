package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TreeSearchTest {

    @Test
    void testOptimalSelection() {
        MonteCarloTreeSearch traversal = new MonteCarloTreeSearch(1, true);
        GoNode childA = new GoNode();
        GoNode childB = new GoNode();
        GoNode root = new GoNode();
        childB.setScore(999999);
        root.add(childA);
        root.add(childB);
        Assertions.assertEquals(childB, traversal.UCB(root));

    }

    @Test
    void testNestedSelection() {
        MonteCarloTreeSearch nestedGame =
                new MonteCarloTreeSearch(1,  0.96, 7, 82, new Random(111), true);
        GoNode childAA = new GoNode(EndStates.LOST, null);
        GoNode childA = new GoNode(EndStates.RUNNING, new ArrayList<GoNode>(List.of(childAA)));
        GoNode childBA = new GoNode(EndStates.LOST, null);
        GoNode childBB = new GoNode(EndStates.WON, null);
        childA.setScore(-9999);
        childBB.setScore(9999);
        GoNode childB = new GoNode(EndStates.RUNNING, new ArrayList<GoNode>(List.of(childBA, childBB)));
        GoNode root = new GoNode(EndStates.RUNNING, new ArrayList<GoNode>(List.of(childA, childB)));
        GoNode intermediate = nestedGame.UCB(root);
        Assertions.assertEquals(childBB, nestedGame.UCB(intermediate));
    }
}