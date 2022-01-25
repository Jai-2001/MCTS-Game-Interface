package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoModelTest {
    GoModel model;

    @BeforeEach
    void setUp() {
        model = new GoModel();
    }

    @AfterEach
    void tearDown(){
        model = null;
    }
    @Test
    void testInit(){
        PlayerModel currentPlayer = model.getCurrentTurn().getCurrentPlayer();
        assertEquals(TurnState.PLAYER_BLACK, currentPlayer);
    }

    @Test
    void testNextTurn(){
        model.tryMove(1,1);
        model.nextTurn();
        PlayerModel currentPlayer = model.getCurrentTurn().getCurrentPlayer();
        assertEquals(TurnState.PLAYER_WHITE, currentPlayer);
    }

    @Test
    void testEmptyBoard(){
        Intersection[][] currentBoard = model.getBoard();
        for (Intersection[] column: currentBoard) {
            for (Intersection piece: column) {
                assertEquals(0, piece.getRepresentation());
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
        Intersection[][] changedBoard = model.getBoard();
        assertEquals(1,changedBoard[2][2].getRepresentation());
    }

    @Test
    void testPoints(){
        model.tryMove(2,2);
        model.nextTurn();
        model.tryMove(4,4);
        model.nextTurn();
        model.tryMove(5,5);
        model.nextTurn();
        int[] results = model.countPoints();
        assertEquals(2, results[0]);
        assertEquals(1, results[1]);
    }

}