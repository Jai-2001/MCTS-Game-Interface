package uk.ac.rhul.CS3821_GO;

public class TurnState {
    private PlayerModel current;
    private PlayerModel previous;
    final static PlayerModel PLAYER_BLACK = new PlayerModel(StoneTypes.BLACK);
    final static PlayerModel PLAYER_WHITE = new PlayerModel(StoneTypes.WHITE);

    TurnState(){
        this.current = PLAYER_BLACK;
        this.previous = PLAYER_WHITE;
    }

    public PlayerModel getCurrentPlayer() {
        return current;
    }
    public PlayerModel getPreviousPlayer(){
        return previous;
    }

    public void changePlayer() {
            if(getCurrentPlayer().equals(PLAYER_WHITE)){
                this.current = PLAYER_BLACK;
                this.previous = PLAYER_WHITE;
            } else {
                this.current = PLAYER_WHITE;
                this.previous = PLAYER_BLACK;
            }
    }
}
