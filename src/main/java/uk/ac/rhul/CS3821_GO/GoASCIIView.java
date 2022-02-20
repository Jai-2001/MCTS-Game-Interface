package uk.ac.rhul.CS3821_GO;

import java.util.Scanner;

public class GoASCIIView {

    public void printBoard(int[][] asciiBoard) {
        for(int[] row : asciiBoard){
            for (int position : row){
                switch (position){
                    case 0: System.out.print('+');
                        break;
                    case 1: System.out.print('B');
                        break;
                    case 2: System.out.print('W');
                }
            }
            System.out.println();
        }
    }

    public String promptInput(String playerName, Scanner inputBuffer) {
        return promptInput(playerName, new String[]{"N/A", "N/A"}, inputBuffer);
    }

    public String promptInput(String playerName, String[] scores, Scanner inputBuffer) {
        System.out.printf("%s, please input the intersection to place onto in the format 'x,y', 'p' to pass, or 'q' to quit:\n", playerName);
        System.out.printf("[Black:%s, White:%s] > ", scores[0], scores[1]);
        String response = inputBuffer.nextLine();
        System.out.print("\n");
        return response;
    }
}
