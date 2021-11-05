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

}