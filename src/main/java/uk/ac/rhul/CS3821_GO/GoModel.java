package uk.ac.rhul.CS3821_GO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoModel {

    final static int BOARD_SIZE_X = 9;
    final static int BOARD_SIZE_Y = 9;

    private TurnState currentPlayerTurn;
    private StoneMap board;
    boolean moveWasValid;
    private int lastGroup;
    private int lastX;
    private int lastY;

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
    }

    public TurnState getCurrentTurn() {
        return this.currentPlayerTurn;
    }

    public void nextTurn() {
        PlayerModel currentPlayer = this.currentPlayerTurn.getCurrentPlayer();
        PlayerModel previousPlayer = this.currentPlayerTurn.getPreviousPlayer();
        Intersection wagered = getWagered();
            if (moveWasValid){
                    if(wagered.getX() != this.lastX || wagered.getY() != this.lastY){
                        placeStone(currentPlayer);
                        this.lastX = wagered.getX();
                        this.lastY = wagered.getY();
                    }
                updateLiberties(currentPlayer);
                updateLiberties(previousPlayer);
                this.currentPlayerTurn.changePlayer();
            }
    }

    private void placeStone(PlayerModel player){
        StoneGroups groups = player.getGroups();
        Intersection wagered = this.board.getWagered();
        if(player.equals(TurnState.PLAYER_WHITE)){
            wagered.setWhite();
        } else{
            wagered.setBlack();
        }
        int thisGroup;
        try{
            thisGroup = groups.getGroup(wagered);
        } catch (IllegalArgumentException e){
            thisGroup = ++lastGroup;
            groups.addStone(thisGroup, wagered);
        }
        int xPos = wagered.getX();
        int yPos = wagered.getY();
        int playerIndex = player.getType().ordinal();
        for(int[] offset : StoneMap.prepareOffsets(xPos, yPos)){
            Intersection adjacent = this.board.getStone(xPos + offset[0], yPos + offset[1]);
            if(adjacent.getRepresentation() == playerIndex){
                int child = groups.getGroup(adjacent);
                if(child != thisGroup){
                    groups.combineGroups(thisGroup, child);
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
                for(int[] offset: StoneMap.prepareOffsets(xPos, yPos)){
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
        for (int i = 0; i < toRemove.size(); i++) {
            int groupIndex = toRemove.get(i);
            player.incrementConcededPoints(groups.getGroupStones(groupIndex).length);
            groups.clearGroup(groupIndex);
        }
    }

    public Intersection[][] getBoard() {
        return this.board.copy();
    }

    public boolean tryMove(int xPos, int yPos){
        if (this.board.checkMove(xPos, yPos, currentPlayerTurn)){
            this.moveWasValid = true;
            return true;
        }
        this.moveWasValid = false;
        return false;
    }

    public int[] countPoints() {
        TurnState turn = this.getCurrentTurn();
        return new int[]{turn.getWhite().getConcededPoints(), turn.getBlack().getConcededPoints()};
    }


    public Intersection getWagered(){
        return this.board.getWagered();
    }
}

