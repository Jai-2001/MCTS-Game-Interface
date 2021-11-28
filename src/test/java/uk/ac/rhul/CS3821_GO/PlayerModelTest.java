package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerModelTest {

    @Test
    void testValidInit() {
        assertDoesNotThrow(
                () -> {
                    new PlayerModel(StoneTypes.WHITE);
                }
        );
        assertDoesNotThrow(
                () -> {
                    new PlayerModel(StoneTypes.BLACK);
                }
        );
    }

    @Test
    void testInvalidInit(){
        assertThrows(IllegalArgumentException.class,
                () -> new PlayerModel(StoneTypes.NONE)
        );
    }

    @Test
    void testStoneNotPresent(){
        PlayerModel single = new PlayerModel(StoneTypes.BLACK);
        Intersection notPresent = new Intersection();
        assertThrows(IllegalArgumentException.class,
                ()-> single.getGroup(notPresent)
        );
    }

    @Test
    void testAddStone(){
        PlayerModel single = new PlayerModel(StoneTypes.BLACK);
        Intersection unique = new Intersection();
        single.addStone(0, unique);
        assertEquals(0, single.getGroup(unique),
                "Player instance should be able to identify the stones that it's placed.");
    }

    @Test
    void testDifferentStones(){
        PlayerModel single = new PlayerModel(StoneTypes.BLACK);
        Intersection belongsToZero = new Intersection();
        Intersection belongsToOne = new Intersection();
        single.addStone(0, belongsToZero);
        single.addStone(1, belongsToOne);
        assertEquals(0, single.getGroup(belongsToZero),
                "Should be able to identify corresponding string for owned stones.");
        assertEquals(1, single.getGroup(belongsToOne),
                "Should be able to differentiate between strings");
    }
}