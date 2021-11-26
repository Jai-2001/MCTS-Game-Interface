package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void testGoMovesValidity() {
        int[][] validMoves = {{1,1},{1,2},{1,3},{2,1},{3,1},{3,2},{3,3},{2,3}};//first 8 moves create string with a single liberty
        int[] invalidMove = {2,2}; // last move fills single liberty, which is invalid
        for (int i = 0; i < validMoves.length; i++) {
            int[] prepMove = validMoves[i];
            assertTrue(grid.checkMove(prepMove[0] - 1,prepMove[1] - 1,TurnState.PLAYER_BLACK));
            grid.confirmMove();
        }
        assertFalse(grid.checkMove(invalidMove[0] - 1,invalidMove[1] - 1, TurnState.PLAYER_BLACK));
    }
}