package uk.ac.rhul.CS3821_GO;

public class TurnState {
    private PlayerModel current;
    private PlayerModel previous;
    static PlayerModel PLAYER_BLACK = new PlayerModel(StoneTypes.BLACK);
    static PlayerModel PLAYER_WHITE = new PlayerModel(StoneTypes.WHITE);
    private PlayerModel black;
    private PlayerModel white;

    TurnState(){
        this.current = new PlayerModel(StoneTypes.BLACK);
        this.black = this.current;
        this.previous = new PlayerModel(StoneTypes.WHITE);
        this.white = this.previous;
    }

    public PlayerModel getCurrentPlayer() {
        return current;
    }
    public PlayerModel getPreviousPlayer(){
        return previous;
    }

    public void changePlayer() {
            if(this.current.equals(this.white)){
                this.current = this.black;
                this.previous = this.white;
            } else {
                this.current = this.white;
                this.previous = this.black;
            }
    }

    public static void flush(){
        PLAYER_BLACK = new PlayerModel(StoneTypes.BLACK);
        PLAYER_WHITE = new PlayerModel(StoneTypes.WHITE);
    }

    public PlayerModel getBlack() {
        return black;
    }

    public PlayerModel getWhite() {
        return white;
    }
}
