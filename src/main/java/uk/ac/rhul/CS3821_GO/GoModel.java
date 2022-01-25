package uk.ac.rhul.CS3821_GO;

import java.util.ArrayList;
import java.util.List;

public class GoModel {

    final static int BOARD_SIZE_X = 9;
    final static int BOARD_SIZE_Y = 9;

    private TurnState currentPlayerTurn;
    private StoneMap board;
    boolean moveWasValid;
    private int lastGroup;

    GoModel(){
        this(new StoneMap(BOARD_SIZE_X,BOARD_SIZE_Y), new TurnState());
    }

    GoModel(StoneMap board, TurnState turn){
        this.currentPlayerTurn = turn;
        moveWasValid = false;
        this.board = board;
        lastGroup = -1;
    }

    public TurnState getCurrentTurn() {
        return this.currentPlayerTurn;
    }

    public void nextTurn() {
        PlayerModel currentPlayer = this.currentPlayerTurn.getCurrentPlayer();
        PlayerModel previousPlayer = this.currentPlayerTurn.getPreviousPlayer();
        if (moveWasValid){
            placeStone(currentPlayer);
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

    private void updateLiberties(PlayerModel player){
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
            groups.clearGroup(toRemove.get(i));
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
        return new int[]{2, 1};
    }
}

