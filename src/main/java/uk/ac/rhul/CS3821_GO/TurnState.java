package uk.ac.rhul.CS3821_GO;

public class TurnState {
    private PlayerModel current;
    final static PlayerModel PLAYER_BLACK = new PlayerModel(StoneTypes.BLACK);
    final static PlayerModel PLAYER_WHITE = new PlayerModel(StoneTypes.WHITE);

    TurnState(){
        this.current = PLAYER_WHITE;
    }

    public PlayerModel getCurrentPlayer() {
        return current;
    }

    public void changePlayer() {
            if(getCurrentPlayer()== PLAYER_WHITE){
                this.current = PLAYER_BLACK;
            } else {
                this.current = PLAYER_WHITE;
            }
    }
}
