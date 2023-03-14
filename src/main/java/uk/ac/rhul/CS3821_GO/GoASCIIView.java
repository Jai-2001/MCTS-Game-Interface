package uk.ac.rhul.CS3821_GO;

import java.util.Scanner;

public class GoASCIIView implements View {

    public void printBoard(byte[][] asciiBoard) {
            for (byte[] row : asciiBoard) {
                    for (int position : row) {
                        switch (position) {
                            case 0 -> System.out.print('+');
                            case 1 -> System.out.print('B');
                            case 2 -> System.out.print('W');
                        }
                    }
                System.out.println();
            }
    }

    @Override
    public String promptInput(String playerName) {
        return promptInput(playerName, new Scanner(System.in));
    }


    public String promptInput(String playerName, Scanner inputBuffer) {
        return promptInput(playerName, new String[]{"N/A", "N/A"}, inputBuffer);
    }

    public String promptInput(String playerName, String[] scores, Scanner inputBuffer) {
            do {
                    try {
                        System.out.printf("%s, please input the intersection to place onto in the format 'x,y', 'p' to pass, or 'q' to quit:\n", playerName);
                        System.out.printf("[Black:%s, White:%s] > ", scores[0], scores[1]);
                        String response = inputBuffer.nextLine();
                        System.out.print("\n");
                        return response;
                    } catch (Exception e){
                        System.out.println("INVALID!!!!!!!\n");
                    }
            } while(true);
    }
}