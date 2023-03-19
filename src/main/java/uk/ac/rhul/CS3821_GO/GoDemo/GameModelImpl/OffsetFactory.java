package uk.ac.rhul.CS3821_GO.GoDemo.GameModelImpl;

public class OffsetFactory {

    public static final int[][] STANDARD = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
    public static final int[][] TOP_LEFT = {{1, 0}, {0, 1}};
    public static final int[][] TOP_RIGHT = {{-1, 0}, {0, 1}};
    public static final int[][] BOT_LEFT =  {{1, 0},{0, -1}};
    public static final int[][] BOT_RIGHT =  {{-1, 0}, {0, -1}};
    public static final int[][] LEFT_WALL =  {{0, -1}, {0, 1}, {1, 0}};
    public static final int[][] RIGHT_WALL = {{0, -1}, {0, 1}, {-1, 0}};
    public static final int[][] TOP_WALL = {{-1,0}, {0, 1}, {1,0}};
    public static final int[][] BOT_WALL = {{0, -1}, {-1, 0}, {1, 0}};

    public static int[][] prepareOffsets(int xPos, int yPos) {
        return switch (yPos * 10 + xPos) {
            case 0 -> TOP_LEFT;
            case 8 -> TOP_RIGHT;
            case 80 -> BOT_LEFT;
            case 88 -> BOT_RIGHT;
            case 1, 2, 3, 4, 5, 6, 7 -> TOP_WALL;
            case 10, 20, 30, 40, 50, 60, 70 -> LEFT_WALL;
            case 18, 28, 38, 48, 58, 68, 78 -> RIGHT_WALL;
            case 81, 82, 83, 84, 85, 86, 87 -> BOT_WALL;
            default -> STANDARD;
        };
    }
}