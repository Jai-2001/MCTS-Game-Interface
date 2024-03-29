package uk.ac.rhul.CS3821_GO.GoDemo.Views;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.rhul.CS3821_GO.GoDemo.Views.GoASCIIView;
import uk.ac.rhul.CS3821_GO.GoDemo.GameModelImpl.GoModel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class GoASCIIViewTest {

    PrintStream authenticOutput = System.out;
    ByteArrayOutputStream testOutput = new ByteArrayOutputStream();

    InputStream authenticInput = System.in;

    GoASCIIView textView;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(testOutput));
        textView = new GoASCIIView();
    }

    @AfterEach
    void tearDown() {
        System.setOut(new PrintStream(authenticOutput));
        System.setIn(authenticInput);
    }

    @Test
    void testDimensions(){
        textView.printBoard(new byte[GoModel.BOARD_SIZE_X][GoModel.BOARD_SIZE_Y]);
        String viewOutput = testOutput.toString().replace("\n","").replace("\r","");
        int desiredCharLength = GoModel.BOARD_SIZE_X * GoModel.BOARD_SIZE_Y;
        assertEquals(desiredCharLength, viewOutput.length());
    }

    @Test
    void testAcceptsASCII(){
        String arbitraryValidInput = "2,2\r\n";
        ByteArrayInputStream testInput = new ByteArrayInputStream(arbitraryValidInput.getBytes());
        System.setIn(testInput);
        Scanner bufferScanner = new Scanner(System.in);
        assertDoesNotThrow(
                () -> {
                    textView.promptInput("White", bufferScanner);
                }
        );

    }
}