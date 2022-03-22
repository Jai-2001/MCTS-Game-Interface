package uk.ac.rhul.CS3821_GO;

public interface GameModel {
    void nextTurn();

    boolean tryMove(int xPos, int yPos);

    int[] countPoints();
}
