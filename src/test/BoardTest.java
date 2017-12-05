import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BoardTest {

    private Board board;
    private ArrayList<BoardCoordinates> map;
    private Player player;

    @Before
    public void setUp() throws Exception {
        this.board = new Board(2);
        this.map = board.getPawnsCoordinates();
        this.player = new Player();
        this.player.setColor(Color.Red);
    }

    @Test (expected = IllegalArgumentException.class)
    public void checkIfGameDoesNotWorkIfInvalidPlayersNumberIsProvided() throws IllegalArgumentException {
        Board board = new Board(10);
    }

    @Test
    public void checkIfPawnMoves() throws Exception {
        board.movePawn(new BoardCoordinates(4, 5), new BoardCoordinates(5, 6), player);
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void checkIfPawnDoesNotMoveToInvalidPosition() throws Exception {
        board.movePawn(new BoardCoordinates(4, 5), new BoardCoordinates(400, 500), player);
    }

    @Test
    public void checkIfPawnMovesToDesiredPosition() throws Exception {
        board.movePawn(new BoardCoordinates(4, 5), new BoardCoordinates(5, 6), player);
        assertEquals(Color.Red, board.getPawnColor(new BoardCoordinates(5, 6)));
    }

    @Test
    public void checkIfPawnMovesOverOtherPawns() throws Exception {
        board.movePawn(new BoardCoordinates(3, 6), new BoardCoordinates(5, 8), player);
        assertEquals(Color.Red, board.getPawnColor(new BoardCoordinates(5, 8)));
    }

    @Test (expected = WrongFieldStateException.class)
    public void checkIfCannotPerformMoveOnFieldWithNoPawn() throws Exception {
        board.movePawn(new BoardCoordinates(7, 5), new BoardCoordinates(5, 8), player);
        assertEquals(Color.Red, board.getPawnColor(new BoardCoordinates(5, 8)));
    }

    @Test (expected = WrongMoveException.class)
    public void checkIfDoesNotMoveOutOfMoveRange() throws Exception {
        board.movePawn(new BoardCoordinates(4, 5), new BoardCoordinates(6, 7), player);
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

}