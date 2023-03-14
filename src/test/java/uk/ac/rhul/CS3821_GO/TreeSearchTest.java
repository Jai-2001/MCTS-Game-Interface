package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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
                new MonteCarloTreeSearch(1,  Math.sqrt(2), 1, 82, new Random(111), true, 5);
        MCTSNode childAA = new MCTSNode(EndStates.LOST, null);
        MCTSNode childA = new MCTSNode(EndStates.RUNNING, Collections.singletonMap(childAA.hashCode(),childAA));
        MCTSNode childBA = new MCTSNode(EndStates.LOST, null);
        MCTSNode childBB = new MCTSNode(EndStates.WON, null);

        MCTSNode childB = new MCTSNode(EndStates.RUNNING,null);
        childB.add(childBA);
        childB.add(childBB);
        childB.setScore(1.0);
        MCTSNode root = new MCTSNode(EndStates.RUNNING, null);
        root.add(childA);
        root.add(childB);
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
                new MonteCarloTreeSearch(1,  Math.sqrt(2), 1, 82, new Random(189219), true, 1);
        autonomous.moveTaken(new byte[]{1, 0}); //CPU is black, places one below top-left corner
        autonomous.moveTaken(new byte[]{0, 0});//Player places in top left corner
        assertArrayEquals(new byte[]{0, 1}, autonomous.path(),
                "Taking any other move than {0,1} is ignoring an immediate win.");
    }
}