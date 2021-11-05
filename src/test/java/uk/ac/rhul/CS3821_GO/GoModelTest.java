package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoModelTest {
    GoModel model;

    @BeforeEach
    void setUp() {
        model = new GoModel();
    }

    @Test
    void testInit(){
        PlayerModel currentPlayer = model.getCurrentTurn().getCurrentPlayer();
        assertEquals(TurnState.PLAYER_BLACK, currentPlayer);
    }

    @Test
    void testNextTurn(){
        model.nextTurn();
        PlayerModel currentPlayer = model.getCurrentTurn().getCurrentPlayer();
        assertEquals(TurnState.PLAYER_WHITE, currentPlayer);
    }

    @Test
    void testEmptyBoard(){
        int[][] currentBoard = model.getBoard();
        for (int[] column: currentBoard) {
            for (int piece: column) {
                assertEquals(0, piece);
            }
        }
    }

    @Test
    void testTryMove(){
        assertTrue(model.tryMove(2,2));
    }

    @Test
    void testBadTryMove(){
        model.tryMove(2,2);
        model.nextTurn();
        assertFalse(model.tryMove(2,2));
    }

    @Test
    void testChangedBoard(){
        model.tryMove(2,2);
        model.nextTurn();
        int[][] changedBoard = model.getBoard();
        assertEquals(1,changedBoard[2][2]);
    }

}