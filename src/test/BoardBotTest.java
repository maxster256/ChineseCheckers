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
    public void checkIfClosestPawnsAreFoundCorrectly() throws Exception {
        board.movePawn(new BoardCoordinates(4, 7), new BoardCoordinates(5, 7), player);
        assertEquals(board.getPawnsClosestToTheEnemyBaseByColor(Color.Red), null);
    }

}