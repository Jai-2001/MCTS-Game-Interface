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
    StoneMap grid;
    TurnState turn;

    @BeforeEach
    void setUp() {
        grid = new StoneMap(GoModel.BOARD_SIZE_X, GoModel.BOARD_SIZE_Y);
        turn = new TurnState();
        parent = new GoModel(grid, turn);
    }

    @AfterEach
    void tearDown() {
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
    void testGoMovesValidity() {
        int[][] validMoves = {{1,1},{2,1},{9,9},{2,2},{9,8},{1,3}};
        for (int i = 0; i < validMoves.length; i++) {
            int[] prepMove = validMoves[i];
            assertTrue(parent.tryMove(prepMove[0] - 1,prepMove[1] - 1),
                    "first 8 moves create string with a single liberty");
            parent.nextTurn();
        }
        int[] invalidMove = {1,2};
        assertFalse(parent.tryMove(invalidMove[0] - 1,invalidMove[1] - 1),
                "last move fills single liberty, which is invalid");
    }

    @Test
    void testCapturingMove(){
        int[][] moveSequence = {{3,1},{2,1},{2,2},{1,2},{1,3},{8,8}};
        for (int[] move : moveSequence) {
            assertTrue(parent.tryMove(move[0]- 1, move[1]-1));
            parent.nextTurn();
        }
        assertTrue(parent.tryMove(0,0),
                "Black should be able to place with no liberties, these pieces will be captured.");
    }
}