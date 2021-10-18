package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerModelTest {

    @Test
    void testInit() {
        player = PlayerModel(StoneTypes.WHITE);
        player = PlayerModel(StoneTypes.BLACK);
    }
}