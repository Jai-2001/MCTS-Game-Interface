package uk.ac.rhul.CS3821_GO.GoDemo.Controllers;


import uk.ac.rhul.CS3821_GO.GoDemo.GoMCTSInterface;

import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class ParameterAnalysis {
    public static void main(String[] args) throws IOException {

        Random seeded = new Random(20223821);
        Scanner input = new Scanner(System.in);
        int maxLimit = takeInt("Please enter an upper bound for score limit:", input);
        double confidence = takeDouble("Please enter a value for exploration confidence:", input);
        int maxDepth = takeInt("Please enter an upper bound for search depth:", input);
        int maxRolls = takeInt("Please enter a value for maximum rollouts:", input);
        int maxIterations = takeInt("Please enter an upper bound value for search iterations:", input);
        System.out.println("Beginning analysis with randomised parameters");
        System.out.println("limit,confidence,depth,rollouts,iterations,winner,time");
                while(true){
                    int limit = seeded.nextInt(maxLimit - 1) + 1;
                    //Players introduced to capture Go typically only progress up to a 10 score max before playing normal Go.
                    int depth = seeded.nextInt(maxDepth - 1 ) + 1;
                    //Longest pro go game lasted 411 moves, double that should suffice, at longest, the simulation may end before the limit.
                    int rolls = seeded.nextInt(maxRolls - 1 ) + 1;
                    //9*9 + 1 for passing
                    int iterations = seeded.nextInt(maxIterations - 1) + 1;
                    //AlphaGo, which requires significantly more resources, operates at this limit, making it an effective max.
                    paramsWithRandom(seeded, limit, confidence, depth, rolls, iterations);
                }
    }

    public static void paramsWithRandom(Random seed, int score, double confidence, int depth, int rolls, int iterations){
        System.out.printf("%d,%f,%d,%d,%d,", score, confidence, depth, rolls, iterations);
        OnePlayerManager tester = new OnePlayerManager(score, true, confidence, depth, rolls, iterations, seed);
        GoMCTSInterface player = new GoMCTSInterface( true, score, seed);
        long startTime = System.nanoTime();
        String winner = "MCTS";
        PrintStream authenticOutput = System.out;
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
        while (!tester.someoneWon()) {
            player.randomMove(tester.model);
            tester.updateBoardState();
            if (tester.someoneWon()) {
                winner = "Random Moves";
            }
            tester.play();
            tester.updateBoardState();
        }
        System.setOut(authenticOutput);
        long runtime = (System.nanoTime() - startTime) / 1000000;
        System.out.println(winner + "," + runtime);
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