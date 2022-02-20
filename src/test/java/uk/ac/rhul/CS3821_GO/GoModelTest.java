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
        TurnState.flush();
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
        int[][]  moves = {{0,2},{1,2},{1,1},{0,1},{1,3},{2,1},{2,2}};
        for (int[] move: moves){
            model.tryMove(move[0],move[1]);
            model.nextTurn();
        }
        int[] results = model.countPoints();
        assertEquals(1, results[0]);
        assertEquals(0, results[1]);
    }

    @Test
    void testMorePoints(){
        int[][]  moreMoves = {{0,2},{1,2},{1,1},{0,1},{1,3},{2,1},{2,2},{1,0},{2,0},{1,2},{3,1},{0,0},{1,1}};
        for (int[] move: moreMoves){
            model.tryMove(move[0],move[1]);
            model.nextTurn();
        }
        int[] moreResults = model.countPoints();
        assertEquals(6, moreResults[0]);
        assertEquals(1, moreResults[1]);
    }
}