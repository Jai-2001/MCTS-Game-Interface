package uk.ac.rhul.CS3821_GO;

public class TurnState {
    private PlayerModel current;
    final static PlayerModel playerBlack = new PlayerModel(StoneTypes.BLACK);
    final static PlayerModel playerWhite = new PlayerModel(StoneTypes.WHITE);

    TurnState(){
        this.current = playerWhite;
    }

    public PlayerModel getCurrentPlayer() {
        return current;
    }
}
