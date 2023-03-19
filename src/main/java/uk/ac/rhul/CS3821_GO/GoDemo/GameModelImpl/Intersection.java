package uk.ac.rhul.CS3821_GO.GoDemo.GameModelImpl;

public class Intersection {
    private StoneTypes stone;
    private final byte x;
    private final byte y;

    Intersection() {
        this.x = (byte) (System.nanoTime() % Integer.MAX_VALUE);
        this.y = (byte) (System.nanoTime() % Integer.MAX_VALUE);
        stone = StoneTypes.NONE;
    }

    Intersection(byte xPos, byte yPos){
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

    public byte getRepresentation(){
        return (byte) this.stone.ordinal();
    }

    public byte getX() {
        return x;
    }

    public byte getY() {
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
