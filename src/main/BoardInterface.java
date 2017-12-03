import java.util.ArrayList;

public interface BoardInterface {

    void movePawn(BoardCoordinates oldPawnPosition, BoardCoordinates newPawnPosition, Player player)
            throws NullFieldException, WrongFieldStateException, WrongMoveException, WrongPawnColorException;

    ArrayList<BoardCoordinates> getPawnsCoordinates();

    Color getPawnColor(BoardCoordinates pawnPosition) throws WrongFieldStateException;

}
