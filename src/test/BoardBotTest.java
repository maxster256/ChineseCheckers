import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BoardBotTest {

    private Board board;
    private ArrayList<BoardCoordinates> map;
    private Player player;

    @Before
    public void setUp() throws Exception {
        this.board = new Board(6);
        this.map = board.getPawnsCoordinates();
        this.player = new Player(Color.Red, "Stevie");
    }


    @Test
    public void checkIfClosestPawnsAreFoundCorrectlyForTheRedPlayer() throws Exception {
        board.movePawn(new BoardCoordinates(4, 7), new BoardCoordinates(5, 7), player);
        board.movePawn(new BoardCoordinates(5, 7), new BoardCoordinates(5, 8), player);
        board.movePawn(new BoardCoordinates(5, 8), new BoardCoordinates(6, 8), player);
        assertTrue(!board.getCoordinatesOfPawnsClosestToTheEnemyBaseByColor(Color.Red).isEmpty());
    }

    @Test
    public void checkIfClosestPawnsAreFoundCorrectlyForTheBlackPlayer() throws Exception {

        Player player = new Player(Color.Black, "Julie");

        board.movePawn(new BoardCoordinates(7, 4), new BoardCoordinates(7, 5), player);
        //board.movePawn(new BoardCoordinates(7, 5), new BoardCoordinates(7, 6), player);
        //board.movePawn(new BoardCoordinates(7, 6), new BoardCoordinates(7, 7), player);

        // Note that it won't get a pawn that's very far from the others, but this might not be a problem at all
        assertTrue(!board.getCoordinatesOfPawnsClosestToTheEnemyBaseByColor(Color.Black).isEmpty());
    }

}