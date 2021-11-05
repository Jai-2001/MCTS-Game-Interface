package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

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
    }

    @Test
    void testProcessMove(){
        String arbitraryValidInput = "2,2\r\nq\r\n";
        ByteArrayInputStream testInput = new ByteArrayInputStream(arbitraryValidInput.getBytes());
        System.setIn(testInput);
        testController.inputMove();
        testController.updateBoardState();
        String anotherValidInput = "q\r\n";
        ByteArrayInputStream anotherStream = new ByteArrayInputStream(anotherValidInput.getBytes());
        System.setIn(anotherStream);
        testController.inputMove();
        String viewOutput = testOutput.toString().replace("\r","");
        String[] lines = viewOutput.split("\n");
        int linePlacedOn = GoModel.BOARD_SIZE_Y + 2 + 1;
        assertEquals('B', lines[linePlacedOn].charAt(1));
    }
}