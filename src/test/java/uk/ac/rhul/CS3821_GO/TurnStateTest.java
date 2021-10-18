package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurnStateTest {
    TurnState defaultStates;
    @BeforeEach
    void setUp() {
         defaultStates = new TurnState();
    }

    @Test
    void testInit(){
       StoneTypes currentStone = defaultStates.getCurrentPlayer().getType();
       assertEquals(StoneTypes.WHITE, currentStone);
    }

    @Test
    void testTurnSwitch(){
        StoneTypes whiteStone = defaultStates.getCurrentPlayer().getType();
        assertEquals(StoneTypes.WHITE, whiteStone);
        defaultStates.changePlayer();
        StoneTypes currentStone = defaultStates.getCurrentPlayer().getType();
        assertEquals(StoneTypes.WHITE, currentStone);
    }

}