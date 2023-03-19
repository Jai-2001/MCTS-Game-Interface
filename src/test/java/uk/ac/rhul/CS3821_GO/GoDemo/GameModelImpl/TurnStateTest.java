package uk.ac.rhul.CS3821_GO.GoDemo.GameModelImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.rhul.CS3821_GO.GoDemo.GameModelImpl.StoneTypes;
import uk.ac.rhul.CS3821_GO.GoDemo.GameModelImpl.TurnState;

import static org.junit.jupiter.api.Assertions.*;

class TurnStateTest {
    TurnState defaultStates;
    @BeforeEach
    void setUp() {
         defaultStates = new TurnState();
    }

    @AfterEach
    void tearDown(){
        defaultStates = null;
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
        for (int i = 0; i < 10000; i++) {
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