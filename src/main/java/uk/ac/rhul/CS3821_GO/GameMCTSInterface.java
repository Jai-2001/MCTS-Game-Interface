package uk.ac.rhul.CS3821_GO;

import java.util.ArrayList;

public interface GameMCTSInterface {
    GameModel presimulate(ArrayList<int[]> moveList, int limit);

    int[] randomMove(GameModel simModel);

    int[] indices();

    EndStates someoneWon(GameModel model);
}