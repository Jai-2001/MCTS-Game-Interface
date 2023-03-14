package uk.ac.rhul.CS3821_GO;


import java.util.Scanner;

public interface View {
    void printBoard(byte[][] asciiBoard);
    String promptInput(String playerName);

    String promptInput(String playerName, String[] scores, Scanner inputBuffer);
}
