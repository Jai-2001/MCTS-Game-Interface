package uk.ac.rhul.CS3821_GO;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.LockSupport;


public class GoWindow extends Application {

    private static GridPane grid = new GridPane();
    private static Text info = new Text("TESTING");
    private static VBox playWin = new VBox(grid, info);
    private static String response;
    private static CompletableFuture<String> question;
    private static Scene scene = new Scene(playWin);

    @Override
    public void start(Stage primaryStage){
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(576);
        primaryStage.setMinHeight(640);
        primaryStage.show();
        response = null;
    }

    public static void refresh(int[][] board, String[] score, String playerName){
            if(score[0].equals("won")){
                info.setText(playerName  + " WINS!");
            } else{
                info.setText("Points: Black: " + score[0] + ", White: " + score[1] + "\nIt is " + playerName + "'s turn." );
            }
        response = null;
        grid.getChildren().removeIf((i)-> true);
        for (int x = 0; x < board.length; x++) {
                for (int y = 0; y < board[x].length; y++) {
                    StackPane point = new StackPane();
                    point.addEventHandler(MouseEvent.MOUSE_CLICKED, clickHandler);
                    drawIntersection(point, board[x][y]);
                    grid.add(point, y,x);
                }
        }
    }

    private static void drawIntersection(StackPane point, int type){
        point.getChildren().removeIf(i -> true);
        Rectangle horizontal = new Rectangle(64,64, 64,  16);
        horizontal.setFill(Paint.valueOf("brown"));
        Rectangle vertical = new Rectangle(64,64, 16,  64);
        vertical.setFill(Paint.valueOf("brown"));
        point.getChildren().addAll(horizontal, vertical);
        if(type != 0) {
            Circle stone = new Circle(24);
            Paint colour = null;
            if (type == 1) {
                colour = Paint.valueOf("black");
            } else if (type == 2) {
                colour = Paint.valueOf("grey");
            }
            stone.setFill(colour);
            point.getChildren().add(stone);
        }
    }

    public static CompletableFuture<String> getQuestion() {
        if(question !=null){
            question.cancel(true);
        }
        question = CompletableFuture.supplyAsync(()->{
            while(response==null){
                LockSupport.park();
            }
            return response;
        });
        return question;
    }

    static EventHandler<MouseEvent> clickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            response = (1 + (int) e.getSceneX()/64)+ "," + (1 +  (int)e.getSceneY()/64);
            question.complete(response);
        }
    };



}
