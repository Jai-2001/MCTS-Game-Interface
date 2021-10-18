package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

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
       assertEquals(StoneTypes.BLACK, currentStone);
    }

    @Test
    void testTurnSwitch(){
        StoneTypes whiteStone = defaultStates.getCurrentPlayer().getType();
        assertEquals(StoneTypes.BLACK, whiteStone);
        defaultStates.changePlayer();
        StoneTypes blackStone = defaultStates.getCurrentPlayer().getType();
        assertEquals(StoneTypes.WHITE, blackStone);
    }

    @Test
    void testMassSwitches(){
        StoneTypes current;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            current = defaultStates.getCurrentPlayer().getType();
            if(i%2 == 0){
                assertEquals(StoneTypes.BLACK, current);
            }  else {
                assertEquals(StoneTypes.WHITE, current);
            }
            defaultStates.changePlayer();
        }
    }

    @Test
    void testArbitrarySwitches(){
        StoneTypes current;
        Random rng = new Random();
        for (int i = 0; i < rng.nextInt(); i++) {
            current = defaultStates.getCurrentPlayer().getType();
            if(i%2 == 0){
                assertEquals(StoneTypes.BLACK, current);
            }  else {
                assertEquals(StoneTypes.WHITE, current);
            }
            defaultStates.changePlayer();
        }
    }
}