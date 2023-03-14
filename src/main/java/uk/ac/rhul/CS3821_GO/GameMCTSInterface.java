package uk.ac.rhul.CS3821_GO;

import java.util.ArrayList;
import java.util.List;

public interface GameMCTSInterface {
    GameModel presimulate(List<byte[]> moveList, int limit);
    GameModel presimulate(List<byte[]> moveList);

    byte[] randomMove(GameModel simModel);

    int[] indices();

    EndStates someoneWon(GameModel model);

    byte[][] getRepresentation(List<byte[]> moveList);
}
