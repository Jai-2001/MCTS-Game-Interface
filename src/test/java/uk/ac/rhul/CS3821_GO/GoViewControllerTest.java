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
        assertEquals(1, resultBoard[1][3]);
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
        assertTrue(testModel.getBoard()[1][3].isBlack());

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
    }
}