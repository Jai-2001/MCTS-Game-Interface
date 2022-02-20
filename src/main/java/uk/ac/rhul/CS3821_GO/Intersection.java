package uk.ac.rhul.CS3821_GO;

public class Intersection {
    private StoneTypes stone;
    private int x;
    private int y;

    Intersection() {
        this.x = (int) (System.nanoTime() % Integer.MAX_VALUE);
        this.y = (int) (System.nanoTime() % Integer.MAX_VALUE);
        stone = StoneTypes.NONE;
    }

    Intersection(int xPos, int yPos){
        this.x = xPos;
        this.y = yPos;
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

    public int getRepresentation(){
        return this.stone.ordinal();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        return (100 * x) + (10  * y);
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Intersection){
            return this.hashCode() == o.hashCode();
        }
        return false;
    }
}
