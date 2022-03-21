package uk.ac.rhul.CS3821_GO;

public class TurnState {
    private PlayerModel current;
    private PlayerModel previous;
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

    public PlayerModel getBlack() {
        return black;
    }

    public PlayerModel getWhite() {
        return white;
    }
}
