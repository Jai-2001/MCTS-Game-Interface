package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class GoViewControllerTest {

    PrintStream authenticOutput = System.out;
    ByteArrayOutputStream testOutput = new ByteArrayOutputStream();

    InputStream authenticInput = System.in;

    GoASCIIView textView;
    GoModel testModel;
    GoViewController testController;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(testOutput));
        textView = new GoASCIIView();
        testModel = new GoModel();
        testController = new GoViewController(testModel, textView);
    }

    @AfterEach
    void tearDown() {
        System.setOut(new PrintStream(authenticOutput));
        System.setIn(authenticInput);
        TurnState.flush();
        testModel = null;
    }

    @Test
    void testProcessMove(){
        String arbitraryValidInput = "2,2\r\n3,3\r\nq\r\n";
        ByteArrayInputStream testInput = new ByteArrayInputStream(arbitraryValidInput.getBytes());
        System.setIn(testInput);
        Scanner inputBuffer = new Scanner(System.in);
        testController.inputMove(inputBuffer);
        testController.updateBoardState();
        testController.inputMove(inputBuffer);
        testController.updateBoardState();
        inputBuffer.close();
        String viewOutput = testOutput.toString().replace("\r","");
        String[] lines = viewOutput.split("\n");
        int linePlacedOn = GoModel.BOARD_SIZE_Y + 2 + 1;
        assertEquals('B', lines[linePlacedOn].charAt(1));
    }


    @Test
    void testMultipleMoves(){
        String arbitraryValidInput = "2,2\r\n2,3\r\n2,4\r\n2,2\r\n3,2\r\n";
        ByteArrayInputStream testInput = new ByteArrayInputStream(arbitraryValidInput.getBytes());
        System.setIn(testInput);
        Scanner inputBuffer = new Scanner(System.in);
        for (int i = 0; i < 4; i++) {
            testController.inputMove(inputBuffer);
            testController.updateBoardState();
        }
        inputBuffer.close();
        int[][] resultBoard = testController.getIntBoard();
        assertEquals(1, resultBoard[1][1]);
        assertEquals(2, resultBoard[1][2]);
        assertEquals(1, resultBoard[3][1]);
        assertEquals(2, resultBoard[2][1]);
    }

    @Test
    void testPassMove(){
        String inputIncludesPass = "2,2\r\np\r\n2,4\r\n2,2\r\n";
        ByteArrayInputStream anotherTestInput = new ByteArrayInputStream(inputIncludesPass.getBytes());
        System.setIn(anotherTestInput);
        Scanner tooInputBuffer = new Scanner(System.in);
        for (int i = 0; i < 3; i++) {
            testController.inputMove(tooInputBuffer);
            testController.updateBoardState();
        }
        tooInputBuffer.close();
        assertTrue(testModel.getBoard()[3][1].isBlack());

    }

    @Test
    void testConsequentPasses(){
        String includesPasses = "2,2\r\np\r\np\r\n";
        ByteArrayInputStream anotherTestInput = new ByteArrayInputStream(includesPasses.getBytes());
        System.setIn(anotherTestInput);
        Scanner inputBuffer = new Scanner(System.in);
        for (int i = 0; i < 2; i++) {
            testController.inputMove(inputBuffer);
            testController.updateBoardState();
            assertFalse(testController.hasEnded());
        }
        inputBuffer.close();
    }

    @Test
    void testGameEnd(){
        String includesPasses = "2,2\r\np\r\np\r\np\r\n";
        ByteArrayInputStream anotherTestInput = new ByteArrayInputStream(includesPasses.getBytes());
        System.setIn(anotherTestInput);
        Scanner threeInputBuffer = new Scanner(System.in);
        for (int i = 0; i < 3; i++) {
            testController.inputMove(threeInputBuffer);
            testController.updateBoardState();
        }
        testController.inputMove(threeInputBuffer);
        testController.updateBoardState();
        assertTrue(testController.hasEnded());
        threeInputBuffer.close();
    }

    @Test
    void testScorePrint(){
        String makesPoints = "1,3\r\n2,3\r\n2,2\r\n1,2\r\n2,4\r\n3,2\r\n3,3\r\n2,1\r\n3,1\r\n2,3\r\n4,2\r\n1,1\r\n2,2\r\np\r\n";
        ByteArrayInputStream scoreInput = new ByteArrayInputStream(makesPoints.getBytes());
        System.setIn(scoreInput);
        Scanner scoreScanner = new Scanner(System.in);
            for (int i = 0; i < 14; i++) {
                testController.inputMove(scoreScanner);
                testController.updateBoardState();
            }
        scoreScanner.close();
        assertTrue(testOutput.toString().contains("Black:6, White:1"));
    }

    @Test
    void testScorePrintAgain(){
        String morePoints = "1,3\r\n2,3\r\n2,2\r\n1,2\r\n2,4\r\n3,2\r\n3,3\r\np\r\n";
        ByteArrayInputStream scoreInputToo = new ByteArrayInputStream(morePoints.getBytes());
        System.setIn(scoreInputToo);
        Scanner scoreScannerToo = new Scanner(System.in);
        for (int i = 0; i < 8; i++) {
            testController.inputMove(scoreScannerToo);
            testController.updateBoardState();
        }
        scoreScannerToo.close();
        assertTrue(testOutput.toString().contains("Black:1, White:0"));
    }


}