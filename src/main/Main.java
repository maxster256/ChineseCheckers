import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Board board = new Board(2);
        ArrayList<BoardCoordinates> map = board.getPawnsCoordinates();
        try {
            for (BoardCoordinates bc : map) {
                System.out.println(bc.getRow() + ", " + bc.getColumn() + ": " + board.getPawnColor(bc));
            }
        } catch(WrongFieldStateException ex) {
            ex.printStackTrace();
        }

        Player player = new Player();
        player.setColor(Color.Red);

        try {
            board.movePawn(new BoardCoordinates(4, 5), new BoardCoordinates(5, 6), player);
            board.movePawn(new BoardCoordinates(4, 6), new BoardCoordinates(6, 6), player);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

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
