package uk.ac.rhul.CS3821_GO;

public class Intersection {
    private StoneTypes stone;

    Intersection() {
        stone = StoneTypes.NONE;
    }

    public void setBlack(){
        this.stone = StoneTypes.BLACK;
    }

    public boolean isBlack(){
        return this.stone == StoneTypes.BLACK;
    }

    public void setWhite(){
        this.stone = StoneTypes.WHITE;
    }

    public boolean isWhite(){
        return this.stone == StoneTypes.WHITE;
    }
    public void clear(){
        this.stone = StoneTypes.NONE;
    }

    public boolean isCleared(){
        return this.stone == StoneTypes.NONE;
    }

}
