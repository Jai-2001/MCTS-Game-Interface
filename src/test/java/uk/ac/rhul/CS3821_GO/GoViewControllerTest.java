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
    }

    @AfterEach
    void tearDown() {
        System.setOut(new PrintStream(authenticOutput));
        System.setIn(authenticInput);
    }

    @Test
    void testProcessMove(){
        textView = new GoASCIIView();
        testModel = new GoModel();
        testController = new GoViewController(testModel, textView);
        String arbitraryValidInput = "2,2\r\nq\r\n";
        ByteArrayInputStream testInput = new ByteArrayInputStream(arbitraryValidInput.getBytes());
        System.setIn(testInput);
        Scanner inputBuffer = new Scanner(System.in);
        testController.inputMove(inputBuffer);
        testController.updateBoardState();
        inputBuffer.close();
        String anotherValidInput = "q\r\n";
        ByteArrayInputStream anotherStream = new ByteArrayInputStream(anotherValidInput.getBytes());
        System.setIn(anotherStream);
        Scanner anotherInputBuffer = new Scanner(System.in);
        testController.inputMove(anotherInputBuffer);
        String viewOutput = testOutput.toString().replace("\r","");
        anotherInputBuffer.close();
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
        textView = new GoASCIIView();
        testModel = new GoModel();
        testController = new GoViewController(testModel, textView);
        for (int i = 0; i < 2; i++) {
            testController.inputMove(inputBuffer);
            testController.updateBoardState();

        }
        inputBuffer.close();
        String viewOutput = testOutput.toString().replace("\r","");

    }
}