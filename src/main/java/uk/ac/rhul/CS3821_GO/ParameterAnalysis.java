package uk.ac.rhul.CS3821_GO;

import java.util.Random;
import java.util.Scanner;

public class ParameterAnalysis {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        do {
            int limit = takeInt("Please enter a score limit:", input);
            double confidence = takeDouble("Please enter a value for exploration confidence:", input) + 0.1;
            int depth = takeInt("Please enter a value for search depth:", input);
            int rolls = takeInt("Please enter a value for maximum rollouts:", input);
            int iterations = takeInt("Please enter a value for search iterations:", input);
            OnePlayerManager tester = new OnePlayerManager(limit, true, confidence, depth, rolls, iterations, new Random());
            GoMCTSInterface player = new GoMCTSInterface( true, limit, new Random());
            long startTime = System.nanoTime();
            String winner = "MCTS";
            while (!tester.someoneWon()) {
                player.randomMove(tester.model);
                tester.updateBoardState();
                if (tester.someoneWon()) {
                    winner = "Random Moves";
                }
                tester.play();
                tester.updateBoardState();
            }
            long runtime = (System.nanoTime() - startTime) / 1000000;

            System.out.print(winner + " won in " + runtime + "ms.");
            System.out.printf("limit: %d, confidence: %f, depth: %d, rollouts: %d,iterations: %d\n", limit, confidence, depth, rolls, iterations);
            System.out.print("Enter 'q' to quit:\n> ");
        } while (!input.nextLine().equals("q"));
    }

    public static int takeInt(String prompt, Scanner buffer){
        return (int) takeDouble(prompt, buffer);
    }

    public static double takeDouble(String prompt, Scanner buffer){
        boolean invalidInput;
        double response = 0;
        do {
            invalidInput = false;
            try{
                System.out.print(prompt+"\n> ");
                response = Double.parseDouble(buffer.nextLine());
            } catch(Exception e){
                invalidInput = true;
            }
        } while(invalidInput);
        return response;
    }
}