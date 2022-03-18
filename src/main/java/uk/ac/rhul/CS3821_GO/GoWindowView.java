package uk.ac.rhul.CS3821_GO;

import javafx.application.Application;
import javafx.application.Platform;

import java.io.ByteArrayInputStream;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.LockSupport;

public class GoWindowView implements View{

    public static void main(String[] args) {
        GoWindowView view = new GoWindowView();
        view.go();
        OnePlayerManager controller = new OnePlayerManager(1, false, view);
            do {
                controller.inputMove(new Scanner(new ByteArrayInputStream("".getBytes())));
                LockSupport.unpark(view.app);
                controller.updateBoardState();
                if(controller.someoneWon()) break;
                for (int i = 0; i <1; i++) {
                    controller.play();
                }
                System.out.println("next move!");
                controller.updateBoardState();
            } while (!controller.someoneWon() && !controller.hasEnded());
            int[] scores =  controller.model.countPoints();
            String winner = scores[0] > scores[1] ? "Black" : "White";
            view.promptInput(winner, new String[]{"won","0"},null);
    }

    private int[][] board;
    private Thread app;

    GoWindowView() {
        this.board = new int[0][0];
    }

    public void go(){
        app = new Thread(() -> Application.launch(GoWindow.class));
        app.start();
    }

    @Override
    public void printBoard(int[][] asciiBoard) {
        this.board = asciiBoard;
    }

    @Override
    public String promptInput(String playerName) {
        return promptInput(playerName, new String[]{"Unavailable","Unavailable"}, new Scanner(new ByteArrayInputStream("".getBytes())));
    }

    @Override
    public String promptInput(String playerName, String[] scores, Scanner inputBuffer) {
        CompletableFuture<String> question = GoWindow.getQuestion();
        int[][] board = this.board;
        Platform.runLater(() -> {
            GoWindow.refresh(board,scores, playerName);
        });
        String answer;
        try {
            answer = question.get();
            question.cancel(true);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            answer = "-2,-2";
        }
        System.out.println(answer);
        return answer;
    }
}

