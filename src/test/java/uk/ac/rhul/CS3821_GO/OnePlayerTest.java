package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OnePlayerTest {

    OnePlayerManager testGame;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        testGame = null;
    }

    @Test
    void testInit() {
        testGame = new OnePlayerManager(10,true);
        assertEquals(TurnState.PLAYER_WHITE,testGame.model.getCurrentTurn().getCurrentPlayer());
    }
}