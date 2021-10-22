package uk.ac.rhul.CS3821_GO;

public class GoASCIIView {
    public void printBoard() {
        System.out.println(
                "+++++++++\n" +
                "+++++++++\n" +
                "+++++++++\n" +
                "+++++++++\n" +
                "+++++++++\n" +
                "+++++++++\n" +
                "+++++++++\n" +
                "+++++++++\n" +
                "+++++++++"

        );
    }

    public void promptInput(String playerName) {
        System.out.printf("%s, please input the intersection to place onto in the format 'x,y':\n", playerName);
    }
}
