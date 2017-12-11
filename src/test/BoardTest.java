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
        this.board = new Board(6);
        this.map = board.getPawnsCoordinates();
        this.player = new Player(Color.Red, "Stevie");
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

    @Test (expected = WrongMoveException.class)
    public void checkIfPawnDoesNotMoveOutsideOfEnemyBase() throws Exception {

        // Creates new Blue player
        Player bluePlayer = new Player(Color.Blue, "Jackie");
        int blueRow = 11;
        int blueColumn = 14;

        // Moves field from (4, 7) near the enemy's base to (10, 13)
        int row = 4;
        int column = 7;

        do {
            int nextRow = row + 1, nextColumn = column +1;

            board.movePawn(new BoardCoordinates(row, column), new BoardCoordinates(nextRow, nextColumn), player);
            row++;
            column++;
        }
        while (row != 10 && column != 13);

        // Checks if movement has been performed correctly
        assertEquals(Color.Red, board.getPawnColor(new BoardCoordinates(10, 13)));

        // Move blue pawn from (11, 14) near the enemy's base
        board.movePawn(new BoardCoordinates(blueRow, blueColumn), new BoardCoordinates(11, 13), bluePlayer);
        blueColumn = 13;

        do {
            int nextRow = blueRow -1, nextColumn = blueColumn -1;
            //System.out.println("Row: " + blueRow + ", column: " + blueColumn);
            board.movePawn(new BoardCoordinates(blueRow, blueColumn), new BoardCoordinates(nextRow, nextColumn), bluePlayer);
            blueRow--;
            blueColumn--;
        }
        while (blueRow != 5 && blueColumn != 7);

        // Checks if movement has been performed correctly
        assertEquals(Color.Blue, board.getPawnColor(new BoardCoordinates(5, 7)));

        // Let's move two pawns inside each other's bases
        board.movePawn(new BoardCoordinates(blueRow, blueColumn), new BoardCoordinates(4, 7), bluePlayer);
        board.movePawn(new BoardCoordinates(row, column), new BoardCoordinates(11,14), player);

        assertEquals(Color.Blue, board.getPawnColor(new BoardCoordinates(4, 7)));
        assertEquals(Color.Red, board.getPawnColor(new BoardCoordinates(11, 14)));

        // Finally, let's see if we can move the pawn that's already in enemy's base outside of it
        board.movePawn(new BoardCoordinates(4, 7), new BoardCoordinates(5, 7), bluePlayer);
        board.movePawn(new BoardCoordinates(11, 14), new BoardCoordinates(11,13), player);
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

}