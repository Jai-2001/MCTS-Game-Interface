package uk.ac.rhul.CS3821_GO;
import java.lang.IllegalArgumentException;
public class PlayerModel {
    private StoneTypes type;

    PlayerModel(StoneTypes type){
        if(type == StoneTypes.NONE){
            throw new IllegalArgumentException("Player must either use white or black stones.");
        }
    }
}
