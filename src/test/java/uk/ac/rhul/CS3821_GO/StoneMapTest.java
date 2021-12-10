package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StoneMapTest {

    GoModel parent;
    TurnState turn;

    @BeforeEach()
    void setUp(){
        TurnState.flush();
        turn = new TurnState();
        parent = new GoModel(new StoneMap(9,9), turn);

    }

    @AfterEach
    void tearDown(){
        parent = null;
        TurnState.flush();
        turn = null;
    }

    @Test
    void testBoundedOffsets(){
        Set<int[]> topLeftRejects = new HashSet<>(Arrays.asList(new int[][]{{-1,0}, {0,-1}}));
        Set<int[]> topLeftBounded = StoneMap.prepareOffsets(0,0);
        for (int[] rejected : topLeftRejects) {
            assertFalse(topLeftBounded.stream().anyMatch(bounded -> Arrays.equals(bounded, rejected)));
        }
        Set<int[]> bottomRightRejects = new HashSet<>(Arrays.asList(new int[][]{{0,1},{1,0}}));
        Set<int[]> bottomRightBounded = StoneMap.prepareOffsets(8,8);
        for (int[] rejected : bottomRightRejects) {
            assertFalse(bottomRightBounded.stream().anyMatch(bounded -> Arrays.equals(bounded, rejected)));
        }
    }

    @Test
    void testGoMovesValidity(){
        int[][] validMoves = {{1,1},{2,1},{9,9},{2,2},{9,8},{1,3},{7,7},{6,6}};
        for (int[] prepMove : validMoves) {
            assertTrue(parent.tryMove(prepMove[0] - 1, prepMove[1] - 1),
                    "first moves create string with a single liberty");
            parent.nextTurn();
        }
        assertFalse(parent.tryMove(0,1),
                "last move fills single liberty, which is invalid");
    }

    @Test
    void testCapturingMove(){
        int[][] moveSequence = {{3,1},{2,1},{2,2},{1,2},{1,3},{8,8}};
        for (int[] move : moveSequence) {
            assertTrue(parent.tryMove(move[0]- 1, move[1]-1),
                    "Creates string with one free eye.");
            parent.nextTurn();
        }
        assertTrue(parent.tryMove(0,0),
                "Black should be able to place with no liberties, these pieces will be captured.");
    }

    @Test
    void testSelfCapture(){
        int[][] moveSequence = {{3,1},{1,2},{3,2},{2,1},{2,2},{-1,-1},{2,3},{-1,-1},{1,3}};
        for (int[] move : moveSequence) {
            if(move[0]==-1){
                turn.changePlayer();
            } else {
                assertTrue(parent.tryMove(move[0] - 1, move[1] - 1));
                parent.nextTurn();
            }
        }
        assertTrue(parent.tryMove(0,0),
                "Black only has the one eye, self capture should be allowed.");
    }

    @Test
    void testKo(){
        int[][] moveSequence = {{2,1},{3,1},{1,2},{4,2},{2,3},{3,3},{3,2},{2,2}};
        for (int[] move : moveSequence) {
            assertTrue(parent.tryMove(move[0] - 1, move[1] - 1));
            parent.nextTurn();
        }
        assertTrue(parent.tryMove(2,1),
                "3,2 and 2,2 could repeat ad infinitum, so ko applies");
    }
}