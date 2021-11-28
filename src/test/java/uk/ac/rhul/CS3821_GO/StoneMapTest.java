package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StoneMapTest {

    StoneMap grid;
    @BeforeEach
    void setUp() {
        grid = new StoneMap(GoModel.BOARD_SIZE_X,GoModel.BOARD_SIZE_Y);
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
        int[][] validMoves = {{1,1},{1,2},{1,3},{2,1},{3,1},{3,2},{3,3},{2,3}};
        for (int[] prepMove : validMoves) {
            assertTrue(grid.checkMove(prepMove[0] - 1,prepMove[1] - 1,TurnState.PLAYER_BLACK),
                    "first 8 moves create string with a single liberty");
            grid.confirmMove();
        }
        assertFalse(grid.checkMove(2,2, TurnState.PLAYER_BLACK),
                "last move fills single liberty, which is invalid");
    }

    @Test
    void testCapturingMove(){
        int[][] deadString = {{1,1},{1,2},{1,3},{2,1},{3,1},{3,2},{3,3},{2,3}};
        for (int[] blackMove : deadString) {
            grid.checkMove(blackMove[0], blackMove[1], TurnState.PLAYER_BLACK);
            grid.confirmMove();
        }
        assertTrue(grid.checkMove(2,2 , TurnState.PLAYER_WHITE),
                "White should be able to place with no liberties, these pieces will be captured.");
    }
}