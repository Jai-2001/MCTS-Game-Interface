package uk.ac.rhul.CS3821_GO;

import java.util.ArrayList;
import java.util.List;

public class GoModel implements GameModel {

    final static int BOARD_SIZE_X = 9;
    final static int BOARD_SIZE_Y = 9;

    private final TurnState currentPlayerTurn;
    private final StoneMap board;
    boolean moveWasValid;
    private int lastGroup;
    private int lastX;
    private int lastY;
    private boolean passedOnce;
    private boolean hasEnded;

    GoModel(){
        this(new StoneMap(BOARD_SIZE_X,BOARD_SIZE_Y), new TurnState());
    }

    GoModel(StoneMap board, TurnState turn){
        this.currentPlayerTurn = turn;
        this.moveWasValid = false;
        this.board = board;
        this.lastGroup = -1;
        this.lastX  = -1;
        this.lastY  = -1;
        this.passedOnce = false;
        this.hasEnded = false;
    }

    public TurnState getCurrentTurn() {
        return this.currentPlayerTurn;
    }

    @Override
    public void nextTurn() {
        PlayerModel currentPlayer = this.currentPlayerTurn.getCurrentPlayer();
        PlayerModel previousPlayer = this.currentPlayerTurn.getPreviousPlayer();
        Intersection wagered = getWagered();
            if (moveWasValid){
                    if(wagered!=null && (wagered.getX() != this.lastX || wagered.getY() != this.lastY)){
                        placeStone(currentPlayer);
                        this.lastX = wagered.getX();
                        this.lastY = wagered.getY();
                    }
                updateLiberties(currentPlayer);
                updateLiberties(previousPlayer);
                this.currentPlayerTurn.changePlayer();
            }
    }

    protected void placeStone(PlayerModel player){
            if(!passedOnce) {
                StoneGroups groups = player.getGroups();
                Intersection wagered = this.board.getWagered();
                if (player.equals(this.currentPlayerTurn.getWhite())) {
                    wagered.setWhite();
                } else {
                    wagered.setBlack();
                }
                int thisGroup = groups.getGroup(wagered);
                if (thisGroup == -1) {
                    thisGroup = ++lastGroup;
                    groups.addStone(thisGroup, wagered);
                }
                int xPos = wagered.getX();
                int yPos = wagered.getY();
                int playerIndex = player.getType().ordinal();
                for (int[] offset : OffsetFactory.prepareOffsets(xPos, yPos)) {
                    Intersection adjacent = this.board.getStone(xPos + offset[0], yPos + offset[1]);
                    if (adjacent.getRepresentation() == playerIndex) {
                        int child = groups.getGroup(adjacent);
                        if (child != thisGroup) {
                            groups.combineGroups(thisGroup, child);
                        }
                    }
                }
            }
    }

    protected void updateLiberties(PlayerModel player){
        StoneGroups groups = player.getGroups();
        groups.clearLiberties();
        List<Integer> toRemove = new ArrayList<>();
        for (int index : groups.getAllGroups()){
            Intersection[] stoneString = groups.getGroupStones(index);
            for (Intersection stone: stoneString) {
                int xPos = stone.getX();
                int yPos = stone.getY();
                for(int[] offset: OffsetFactory.prepareOffsets(xPos, yPos)){
                    Intersection adjacent = this.board.getStone(xPos + offset[0], yPos + offset[1]);
                    if (adjacent.isCleared()){
                        groups.addLiberty(index, adjacent);
                    }
                 }
            }
            if(groups.getLiberties(index) != null && groups.getLiberties(index).size() == 0) {
               toRemove.add(index);
            }
        }
        for (int groupIndex : toRemove) {
            player.incrementConcededPoints(groups.getGroupStones(groupIndex).length);
            groups.clearGroup(groupIndex);
        }
    }

    public Intersection[][] getBoard() {
        return this.board.copy();
    }

    @Override
    public boolean tryMove(int xPos, int yPos){
        if (xPos == -1){
            this.hasEnded = this.currentPlayerTurn.getCurrentPlayer() == this.currentPlayerTurn.getWhite() && this.passedOnce;
            this.passedOnce = true;
            this.moveWasValid = true;
            return true;
        } else if(this.board.checkMove(xPos, yPos, currentPlayerTurn)){
            this.passedOnce = false;
            this.moveWasValid = true;
            return true;
        }
        this.moveWasValid = false;
        return false;
    }

    @Override
    public int[] countPoints() {
        TurnState turn = this.getCurrentTurn();
        return new int[]{turn.getWhite().getConcededPoints(), turn.getBlack().getConcededPoints()};
    }

    @Override
    public boolean hasEnded() {
        return this.hasEnded;
    }

    public Intersection getWagered(){
        return this.board.getWagered();
    }
}

