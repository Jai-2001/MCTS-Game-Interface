package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerModelTest {

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
        PlayerModel single = new PlayerModel(StoneTypes.BLACK);
        Intersection notPresent = new Intersection();
        assertThrows(IllegalArgumentException.class,
                ()-> {
                    single.getKey(notPresent);
                }
        );
    }

    @Test
    void testStringsKey(){
        PlayerModel single = new PlayerModel(StoneTypes.BLACK);
        Intersection unique = new Intersection();
        unique.setBlack();
        single.addStone(0, unique);
        assertEquals(0, single.getKey(unique),
                "Player instance should be able to identify the group one of it's Intersection object belongs to");
    }
}