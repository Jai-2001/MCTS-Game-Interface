package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerModelTest {

    PlayerModel single;

    @BeforeEach
    void setUp() {
        single = new PlayerModel(StoneTypes.BLACK);
    }

    @Test
    void testValidInit() {
        assertDoesNotThrow(
                () -> {
                    PlayerModel playerWhite = new PlayerModel(StoneTypes.WHITE);
                }
        );
        assertDoesNotThrow(
                () -> {
                    PlayerModel playerBlack = new PlayerModel(StoneTypes.BLACK);
                }
        );
    }

    @Test
    void testInvalidInit(){
        assertThrows(IllegalArgumentException.class,
                () -> {
                    PlayerModel playerNone = new PlayerModel(StoneTypes.NONE);
                }
        );
    }

    @Test
    void testStoneNotPresent(){
        Intersection notPresent = new Intersection();
        assertThrows(IllegalArgumentException.class,
                ()-> single.getGroup(notPresent)
        );
    }

    @Test
    void testAddStone(){
        Intersection unique = new Intersection();
        single.addStone(0, unique);
        assertEquals(0, single.getGroup(unique),
                "Player instance should be able to identify the stones that it's placed.");
    }

    @Test
    void testDifferentStones(){
        Intersection belongsToZero = new Intersection();
        Intersection belongsToOne = new Intersection();
        single.addStone(0, belongsToZero);
        single.addStone(1, belongsToOne);
        assertEquals(0, single.getGroup(belongsToZero),
                "Should be able to identify corresponding string for owned stones.");
        assertEquals(1, single.getGroup(belongsToOne),
                "Should be able to differentiate between strings");
    }

    @Test
    void testCombineStrings(){
        Intersection belongsToZero = new Intersection();
        Intersection oneOrZero = new Intersection();
        Intersection willSeeAll = new Intersection();
        single.addStone(0, belongsToZero);
        single.addStone(1, oneOrZero);
        single.addStone(2, willSeeAll);
        single.combineStrings(1,2);
        single.combineStrings(0,1);
        assertEquals(0, single.getGroup(oneOrZero));
        assertEquals(0, single.getGroup(willSeeAll));
    }
}