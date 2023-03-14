package uk.ac.rhul.CS3821_GO;

import javafx.application.Application;
import javafx.application.Platform;

import java.io.ByteArrayInputStream;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.LockSupport;

public class GoWindowView implements View{

    private MonteCarloTreeSearch optional = null;
    private final static long REFRESH_RATE = 10;
    private final static long INTERVAL = 1000/REFRESH_RATE;

    boolean onePlayer;
    public static void main(String[] args) {

        int scoreLimit = args.length >= 1? Integer.parseInt(args[0]) : 1;
        boolean onePlayer = (args.length < 1 || args[1].equals("1")) && true;
        GoWindowView view = new GoWindowView(onePlayer);
        OnePlayerManager controller = new OnePlayerManager(scoreLimit, onePlayer, view);
        view.go();
        do {
            controller.inputMove(new Scanner(new ByteArrayInputStream("".getBytes())));
            LockSupport.unpark(view.app);
            controller.updateBoardState();
            if(controller.someoneWon()) break;
            if(onePlayer)  {
                controller.play();
                System.out.println("next move!");
                controller.updateBoardState();
            }
        } while (!controller.someoneWon());
        String winner = controller.getWinner();
        view.promptInput(winner, new String[]{"won","0"},null);
    }

    private byte[][] board;
    private Thread app;

    GoWindowView(boolean onePlayer) {
        this.board = new byte[0][0];
        this.onePlayer = onePlayer;
    }

    GoWindowView(MonteCarloTreeSearch optional, boolean onePlayer){
        this(onePlayer);
        if(onePlayer){
            this.setOptional(optional);
        }
    }
    public void go(){
        app = new Thread(() -> Application.launch(GoWindow.class));
        app.start();
    }

    @Override
    public void printBoard(byte[][] asciiBoard) {
        this.board = asciiBoard;
    }

    @Override
    public String promptInput(String playerName) {
        return promptInput(playerName, new String[]{"Unavailable","Unavailable"}, new Scanner(new ByteArrayInputStream("".getBytes())));
    }

    @Override
    public String promptInput(String playerName, String[] scores, Scanner inputBuffer) {
        CompletableFuture<String> question = GoWindow.getQuestion();
        byte[][] board = this.board;
        Platform.runLater(() -> GoWindow.refresh(board,scores, playerName, optional.getShiftingRoot()));
        String answer;
        try {
            if(onePlayer && this.getOptional() !=null){
                long start = System.currentTimeMillis()+ INTERVAL;

                while (!question.isDone()){
                    MCTSNode currentBest = getOptional().backgroundPath(2).bestLeaf();
                    if(System.currentTimeMillis()>start){
                        Platform.runLater(()->{
                            GoWindow.project(currentBest, false);
                        });
                        start = System.currentTimeMillis()+ INTERVAL;
                    }
                }
            }
            System.out.println("In background: "+ getOptional().checkIterations());
            answer = question.get();
            question.cancel(true);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            answer = "-2,-2";
        }
        System.out.println("USER HAS TAKEN MOVE -> "+ answer);

        return answer;
    }

    public MonteCarloTreeSearch getOptional() {
        return optional;
    }

    public void setOptional(MonteCarloTreeSearch optional) {
        this.optional = optional;
    }

}

