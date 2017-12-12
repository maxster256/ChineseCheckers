import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        Board board = new Board(4);
        ArrayList<BoardCoordinates> map = board.getPawnsCoordinates();
        try {
            for (BoardCoordinates bc : map) {
                System.out.println(bc.getRow() + ", " + bc.getColumn() + ": " + board.getPawnColor(bc));
            }
        } catch(WrongFieldStateException ex) {
            ex.printStackTrace();
        }

        Player player = new Player(Color.Red, "Foo Bar");

        System.out.println("\nAfter moves: \n");

        map = board.getPawnsCoordinates();
        try {
            for (BoardCoordinates bc : map) {
                System.out.println(bc.getRow() + ", " + bc.getColumn() + ": " + board.getPawnColor(bc));
            }
        } catch(WrongFieldStateException ex) {
            ex.printStackTrace();
        }
//        list.add(null);
//        list.add(2);
//
//        list.add(null);
//
//        System.out.print(list);

    }
}
