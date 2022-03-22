package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TreeSearchTest {

    @Test
    void testOptimalSelection() {
        MonteCarloTreeSearch traversal = new MonteCarloTreeSearch(1, true);
        MCTSNode childA = new MCTSNode();
        MCTSNode childB = new MCTSNode();
        MCTSNode root = new MCTSNode();
        root.incrementVisits();
        root.incrementVisits();
        childA.incrementVisits();
        childA.incrementVisits();
        childB.incrementVisits();
        childB.setScore(1);
        root.add(childA);
        root.add(childB);
        Assertions.assertEquals(childB, traversal.UCB(root));

    }

    @Test
    void testNestedSelection() {
        MonteCarloTreeSearch nestedGame =
                new MonteCarloTreeSearch(1,  1, 1, 82, new Random(111), true, 5);
        MCTSNode childAA = new MCTSNode(EndStates.LOST, null);
        MCTSNode childA = new MCTSNode(EndStates.RUNNING, new ArrayList<MCTSNode>(List.of(childAA)));
        MCTSNode childBA = new MCTSNode(EndStates.LOST, null);
        MCTSNode childBB = new MCTSNode(EndStates.WON, null);
        MCTSNode childB = new MCTSNode(EndStates.RUNNING, new ArrayList<MCTSNode>(List.of(childBA, childBB)));
        childB.setScore(1.0);
        MCTSNode root = new MCTSNode(EndStates.RUNNING, new ArrayList<MCTSNode>(List.of(childA, childB)));
        root.incrementVisits();
        root.incrementVisits();
        childA.incrementVisits();
        childA.incrementVisits();
        childBA.incrementVisits();
        childBA.incrementVisits();
        childB.incrementVisits();
        childBB.incrementVisits();
        childBB.setScore(1);
        MCTSNode intermediate = nestedGame.UCB(root);
        Assertions.assertEquals(childBB, nestedGame.UCB(intermediate));
    }

    @Test
    void testRollOuts(){
        MonteCarloTreeSearch autonomous =
                new MonteCarloTreeSearch(1,  1, 0, 82, new Random(189219), false, 1);
        autonomous.moveTaken(new int[]{0, 0});
        autonomous.moveTaken(new int[]{0, 1});
        autonomous.moveTaken(new int[]{-1, -1});
        assertTrue(Arrays.equals(new int[]{1,0}, autonomous.path()));
    }
}