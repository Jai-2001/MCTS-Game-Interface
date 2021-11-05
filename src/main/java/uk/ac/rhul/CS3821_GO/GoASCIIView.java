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
                            case 2: System.out.println('W');
                        }
                    }
                System.out.println();
            }
    }

    public String promptInput(String playerName) {
        Scanner input = new Scanner(System.in);
        System.out.printf("%s, please input the intersection to place onto in the format 'x,y', or 'q' to quit:\n>  ", playerName);
        String response = input.nextLine();
        System.out.print("\n");
        return response;
    }
}
