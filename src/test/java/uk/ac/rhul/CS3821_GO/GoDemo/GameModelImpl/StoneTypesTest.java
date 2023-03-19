package uk.ac.rhul.CS3821_GO.GoDemo.GameModelImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.rhul.CS3821_GO.GoDemo.GameModelImpl.StoneTypes;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
class StoneTypesTest {

    @BeforeEach
    void setUp() {
        
    }

    @Test
    void testCardinality(){
        assertEquals(3, StoneTypes.values().length);
    }


    @Test
    void testPlacedStones(){
        boolean containsWhite = Arrays.stream(StoneTypes.values()).anyMatch(StoneTypes.WHITE::equals);
        assertTrue(containsWhite);
        boolean containsBlack = Arrays.stream(StoneTypes.values()).anyMatch(StoneTypes.BLACK::equals);
        assertTrue(containsBlack);

    }

    @Test
    void testNoStonePlaced(){
        boolean containsNone = Arrays.stream(StoneTypes.values()).anyMatch(StoneTypes.NONE::equals);
        assertTrue(containsNone);
    }
}