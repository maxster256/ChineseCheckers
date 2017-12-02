import java.lang.reflect.Array;
import java.util.ArrayList;

public class Board implements BoardInterface {

    private int numberOfPlayers;
    private int boardHeight;

    private ArrayList<ArrayList<BaseField>> gameBoard;

    public Board(int numberOfPlayers) {

        this.numberOfPlayers = numberOfPlayers;
        // this.boardHeight = boardHeight;

        this.gameBoard = new ArrayList<>();

        // Fills the board with values
        // Board columns by rows: 13x17

        // We will prepare an rectangular array of size 19x19

    }

    private ArrayList assignFieldsToBoard() {

        return new ArrayList();
    }

    private void assignPawnsToBaseFields() {

    }

    public void movePawn(BoardCoordinates oldPawnPosition, BoardCoordinates newPawnPosition, Player player) {

        // TODO: Should throw an exception

        // Remember to check if the player moves his own pawns!!!

//        if (distance == 1)
//            movePawnByOneField();
//        else if (distance > 1)
//            movePawnByMultipleFields();

    }

    private void movePawnByOneField() {

        Field field = new BaseField(Color.Green);

        if (field.getClass().equals(BaseField.class)) {

        }

        // Regular method
        // If the field is only one field away, check if it's empty or not
        // If empty, move the pawn there
        // If not: TODO: Create own exception

    }

    private void movePawnByMultipleFields() {

        // TODO: implement

    }

    public int calculateDistanceBetweenFields(BoardCoordinates oldField, BoardCoordinates newField) {

        // TODO: Implement
        return 0;
    }



}
