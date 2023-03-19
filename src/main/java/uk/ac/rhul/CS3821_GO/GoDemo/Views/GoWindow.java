package uk.ac.rhul.CS3821_GO.GoDemo.Views;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import uk.ac.rhul.CS3821_GO.GoDemo.GoMCTSInterface;
import uk.ac.rhul.CS3821_GO.MCTSNode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.LockSupport;


public class GoWindow extends Application {
    private static final TreeItem<MCTSNode> rootNode = new TreeItem<>();
    private static final TreeView<MCTSNode> tree = new TreeView<>(rootNode);
    private final static GridPane[] grids = new GridPane[]{new GridPane(), new GridPane()};
    private final static Text info = new Text();
    private final static Button pass = new Button("Pass");
    private final static VBox playWin = new VBox(new HBox(grids),new HBox(pass,info), tree);
    private final static Scene scene = new Scene(playWin);
    private final static Circle hoverProcessStone = new Circle(24, Color.rgb(128,0, 0, 0.5));
    private final static Circle hoverStone = new Circle(24, Color.rgb(0,0, 128, 0.5));
    private final static Circle processStone = new Circle(24, Color.rgb(0,0, 128, 0.5));
    private static Circle currentHover = hoverStone;
    private final static Paint LINE_COL = Paint.valueOf("brown");
    private final static Paint EMPTY_COL = Color.rgb(203,168,116);
    private final static String[] infoLines = new String[5];
    private static String response;
    private static CompletableFuture<String> question;
    public static boolean userProjected = false;
    private static byte[][] lastGrid = new byte[0][];
    private final static Paint[] PLACED_COLORS = new Paint[]{
            Color.valueOf("black"),
            Color.LIGHTGRAY,
            Color.rgb(128,0, 0, 1),
            Color.rgb(0,0, 128, 1),
    };

    @Override
    public void start(Stage primaryStage){
        Arrays.fill(infoLines,"");
        GoWindow.info.setFont(Font.font("Arial"));
        for (GridPane grid: grids) {
            grid.setBackground(new Background(new BackgroundFill(EMPTY_COL, new CornerRadii(64),new Insets(0.1))));
            grid.setMaxWidth(592);
            grid.setMaxHeight(672);
        }
        pass.addEventHandler(MouseEvent.MOUSE_CLICKED, passHandler);
        tree.getSelectionModel().selectedItemProperty().addListener(showProjection);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(592);
        primaryStage.setMinHeight(672);
        primaryStage.show();
        response = null;
    }

    private static void sendToInfo(String message, int index){
        infoLines[index]= message;
        info.setText(String.join("\n",infoLines));
    }
    public static void refresh(byte[][] board, String[] score, String playerName, MCTSNode root){
        currentHover = hoverStone;
        if(score[0].equals("won")){
           sendToInfo(playerName+" WINS!", 1);
        } else{
            sendToInfo("Points: Black: " + score[0] + ", White: " + score[1],0);
            sendToInfo("It is " + playerName + "'s turn.", 1);
        }
        response = null;
        for (int j = 1, gridsLength = grids.length; j < gridsLength; j++) {
            GridPane g = grids[j];
            g.getChildren().removeIf(i -> true);
        }
        refresh(board, 0);
        lastGrid = board;
        performInsanity(root);
    }

        private static void performInsanity(MCTSNode root){
                tree.edit(rootNode);
                rootNode.setExpanded(false);
                tree.getSelectionModel().clearSelection();
                rootNode.getChildren().removeIf(i -> true);
                rootNode.setValue(root);

        }

    private static void populateTree(TreeItem<MCTSNode> treeNode){
        if (treeNode==null) return;
        treeNode.getChildren().removeIf(i -> true);
        treeNode.getValue()
                .getChildren()
                .values()
                .stream()
                .sorted(Comparator.comparing(MCTSNode::hashCode))
                .map(TreeItem::new)
                .forEachOrdered(treeNode.getChildren()::add);
    }

    static ChangeListener<TreeItem<MCTSNode>> showProjection = (obs, root, node) -> {
        System.out.println("CLICKED");
        if(node==null) return;
        populateTree(node);
        project(node.getValue(), true);
    };
    public static void project(MCTSNode node, boolean isUser){
        if(isUser || !userProjected){
            List<byte[]> moves = node.buildMoveList();;
            byte[][] board = GoMCTSInterface.represent(moves);
            sendToInfo("["+node.getEndState()+"]"+Arrays.deepToString(moves.toArray()),4);
            refresh(board,1);
            userProjected=isUser;
        };

    }

    public static void refresh(byte[][] board, int index){
        grids[index].getChildren().removeIf((i)-> true);
        for (int x = 0; x < board.length; x++) {
                for (int y = 0; y < board[x].length; y++) {
                    StackPane point = new StackPane();
                    if(index==0){
                        point.addEventHandler(MouseEvent.MOUSE_CLICKED, clickHandler);
                        point.addEventHandler(MouseEvent.MOUSE_ENTERED, hoverEnter);
                        point.addEventHandler(MouseEvent.MOUSE_EXITED, hoverExit);
                    }
                    int type =  board[x][y] - 1;
                    if(index!=0){
                        if(board[x][y]!=lastGrid[x][y]){
                            type = board[x][y]+1;
                        }
                    }
                    drawIntersection(point, type);
                    grids[index].add(point, y,x);
                }
        }
        //grid.setBackground(new Background(new BackgroundFill(EMPTY_COL, new CornerRadii(32),new Insets(-32.0))));

    }

    private static void drawIntersection(StackPane point, int type){
        point.getChildren().removeIf(i -> true);
        Rectangle horizontal = new Rectangle(64,64,64, 16);
        horizontal.setFill(LINE_COL);
        horizontal.setBlendMode(BlendMode.SOFT_LIGHT);
        Rectangle vertical = new Rectangle(64,64,16, 64);
        vertical.setFill(LINE_COL);
        vertical.setBlendMode(BlendMode.SOFT_LIGHT);
        point.getChildren().addAll(horizontal, vertical);
//        horizontal.setFill(LINE_COL);
//        Rectangle vertical = new Rectangle(64,64, 16,  64);
//        vertical.setFill(LINE_COL);
//        point.getChildren().addAll(vertical);
        if(type > -1) {
            Circle stone = new Circle(24);
            Paint colour = PLACED_COLORS[type];
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
    static EventHandler<MouseEvent> hoverEnter = e -> {
        StackPane point = (StackPane) e.getSource();
        point.getChildren().add(currentHover);
        String pos = ((int) e.getSceneY()/64)+ "," + ((int)e.getSceneX()/64);
        sendToInfo("User is hovering over " + pos,3);
    };


    static EventHandler<MouseEvent> hoverExit = e -> {
        StackPane point = (StackPane) e.getSource();
        point.getChildren().remove(currentHover);
    };
    static EventHandler<MouseEvent> clickHandler = new EventHandler<>() {
        @Override
        public void handle(MouseEvent e) {
            if(currentHover!=hoverProcessStone){
                response = (1 + (int) e.getSceneX()/64)+ "," + (1 +  (int)e.getSceneY()/64);
                StackPane point = (StackPane) e.getSource();
                point.getChildren().remove(currentHover);
                point.getChildren().add(processStone);
                userProjected = false;
                currentHover = hoverProcessStone;
                loader.start();
                question.complete(response);
            }
        }
    };

    static EventHandler<MouseEvent> passHandler = new EventHandler<>() {
        @Override
        public void handle(MouseEvent e) {
            response = "0,0";
            loader.start();
            question.complete(response);
        }
    };

    private static final AnimationTimer loader  = new AnimationTimer() {
        private long prev = System.nanoTime();
        @Override
        public void handle(long now) {
            long time  = now - this.prev;
            if (response == null){
                sendToInfo("Awaiting Move:"+ (time/1000000) + "ms elapsed.",2);
            } else{
                sendToInfo("Processing Move (" + response + "):"+ (time/1000000) + "ms elapsed.",2);
            }
        }
    };
}
