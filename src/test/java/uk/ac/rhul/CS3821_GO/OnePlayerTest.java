package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Random;
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
                testGame.play();
                testGame.updateBoardState();
            }
        scanPlay.close();
        assertEquals(TurnState.PLAYER_BLACK,testGame.model.getCurrentTurn().getCurrentPlayer());
    }
    @Test
    void testWinCondition() {
        testGame = new OnePlayerManager(1, false);
        String oneWin = "1,3\r\n2,3\r\n2,2\r\n1,2\r\n2,4\r\n3,2\r\n3,3\r\n1,1\r\np\r\n";
        ByteArrayInputStream winBuf = new ByteArrayInputStream(oneWin.getBytes());
        System.setIn(winBuf);
        Scanner winScan = new Scanner(System.in);
        for (int i = 0; i < 9; i++) {
            testGame.inputMove(winScan);
            testGame.updateBoardState();
        }
        assertTrue(testGame.someoneWon());
    }

    @Test
    void testRollOuts(){
        OnePlayerManager autonomous = new OnePlayerManager
                (1, false ,1.5, 5, 82, new Random(303));
        String adverseInput = "1,1\r\n1,2\r\n9,9\r\n"; //1,1 is literally a sitting duck
        ByteArrayInputStream adverseBuf = new ByteArrayInputStream(adverseInput.getBytes());
        System.setIn(adverseBuf);
        Scanner moves = new Scanner(System.in);
        for (int i = 0; i < 3; i++) {
            autonomous.inputMove(moves);
            autonomous.updateBoardState();
        }
        autonomous.play();
        moves.close();
        assertEquals(1, autonomous.model.countPoints()[0]);
    }
}