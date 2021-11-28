package uk.ac.rhul.CS3821_GO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
class StoneTypesTest {

    @BeforeEach
    void setUp() {
        
    }

    @Test
    void testCardinality(){
        assertEquals(3,StoneTypes.values().length);
    }


    @Test
    void testPlacedStones(){
        boolean containsWhite = Arrays.asList(StoneTypes.values()).contains(StoneTypes.WHITE);
        assertTrue(containsWhite);
        boolean containsBlack = Arrays.asList(StoneTypes.values()).contains(StoneTypes.BLACK);
        assertTrue(containsBlack);

    }

    @Test
    void testNoStonePlaced(){
        boolean containsNone = Arrays.asList(StoneTypes.values()).contains(StoneTypes.NONE);
        assertTrue(containsNone);
    }
}