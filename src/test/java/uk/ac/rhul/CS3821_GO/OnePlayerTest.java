package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class OnePlayerTest {

    OnePlayerManager testGame;
    PrintStream authenticOutput = System.out;
    ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
    InputStream authenticInput = System.in;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(testOutput));
    }

    @AfterEach
    void tearDown() {
        System.setOut(new PrintStream(authenticOutput));
        System.setIn(authenticInput);
        TurnState.flush();
    }

    @Test
    void testInit() {
        testGame = new OnePlayerManager(10,true);
        assertEquals(TurnState.PLAYER_WHITE,testGame.model.getCurrentTurn().getCurrentPlayer());
    }

    @Test
    void testPlayerStart() {
        testGame = new OnePlayerManager(10,false);
        String pStart = "1,1\r\n1,1\r\np\r\n";
        ByteArrayInputStream playerStream = new ByteArrayInputStream(pStart.getBytes());
        Scanner scanPlay = new Scanner(playerStream);
            for (int i = 0; i < 2; i++) {
                testGame.inputMove(scanPlay);
                testGame.updateBoardState();
            }
        scanPlay.close();
        testGame = new OnePlayerManager(10,true);
        assertEquals(TurnState.PLAYER_BLACK,testGame.model.getCurrentTurn().getCurrentPlayer());

    }
}