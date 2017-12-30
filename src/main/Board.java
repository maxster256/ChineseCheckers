import com.sun.javaws.exceptions.InvalidArgumentException;

import java.lang.reflect.Array;
import java.util.*;

public class Board implements BoardInterface {

    private int numberOfPlayers;
    private ArrayList<ArrayList<Field>> gameBoard;
    private ArrayList<Color> usedColorByPlayers; // colors of pawns

    // TODO (for this week):
    // 1. Sprawdź, czy pionek nie jest w bazie przeciwnika, jeśli jest, nie pozwól mu z niej wyjść. - DONE.
    //      źle!!! nie sprawdzasz przeciwnego koloru, a po prostu czy jest inny od pionka!!!
    // 2. Określ, kto wygrywa (obserwator?) - jako tako zrobione, bez obserwatora
    // 3. Stwórz bota.

    // TODO (notki od PN):
    // Chyba miałoby sens stworzyć klas BluePlayer, RedPlayer, GreenPlayer itd...
    // Jakoś wyglądałoby bardziej "elegancko", przynajmniej tak mi się zdaje.

    // MK
    // Chyba nie ma sensu, skoro jedyna różnica to kolor... trochę mało jak na osobną klasę
    //TODO: Poprawić punkt 1 z TODO wyżej (for this week)!!!

    Board(int numberOfPlayers) throws IllegalArgumentException { //TODO: Own exception?

        if (numberOfPlayers != 2 && numberOfPlayers != 3 &&
                numberOfPlayers != 4 && numberOfPlayers != 6) {
            throw new IllegalArgumentException();
        }

        this.numberOfPlayers = numberOfPlayers;
        this.gameBoard = assignFieldsToBoard();
        usedColorByPlayers = setColorsOfPlayers();

        assignPawnsToBaseFields();
    }

    /*
    The gameBoard (rectangular 2D array of size 19x19) is like:
    ["null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null"],
    ["null", "null", "null", "null", "null", "RED_", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null"],
    ["null", "null", "null", "null", "null", "RED_", "RED_", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null"],
    ["null", "null", "null", "null", "null", "RED_", "RED_", "RED_", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null"],
    ["null", "null", "null", "null", "null", "RED_", "RED_", "RED_", "RED_", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null"],
    ["null", "BLAC", "BLAC", "BLAC", "BLAC", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "GREE", "GREE", "GREE", "GREE", "null", "null", "null", "null", "null"],
    ["null", "null", "BLAC", "BLAC", "BLAC", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "GREE", "GREE", "GREE", "null", "null", "null", "null", "null"],
    ["null", "null", "null", "BLAC", "BLAC", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "GREE", "GREE", "null", "null", "null", "null", "null"],
    ["null", "null", "null", "null", "BLAC", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "GREE", "null", "null", "null", "null", "null"],
    ["null", "null", "null", "null", "null", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "null", "null", "null", "null", "null"],
    ["null", "null", "null", "null", "null", "ORAN", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "BLUE", "null", "null", "null", "null"],
    ["null", "null", "null", "null", "null", "ORAN", "ORAN", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "BLUE", "BLUE", "null", "null", "null"],
    ["null", "null", "null", "null", "null", "ORAN", "ORAN", "ORAN", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "BLUE", "BLUE", "BLUE", "null", "null"],
    ["null", "null", "null", "null", "null", "ORAN", "ORAN", "ORAN", "ORAN", "EMPT", "EMPT", "EMPT", "EMPT", "EMPT", "BLUE", "BLUE", "BLUE", "BLUE", "null"],
    ["null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "YELL", "YELL", "YELL", "YELL", "null", "null", "null", "null", "null"],
    ["null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "YELL", "YELL", "YELL", "null", "null", "null", "null", "null"],
    ["null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "YELL", "YELL", "null", "null", "null", "null", "null"],
    ["null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "YELL", "null", "null", "null", "null", "null"],
    ["null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null"],
    RED_, BLAC, GREE, ORAN, YELL and BLUE are colors of BaseFields and EMPT is NeutralField.
    Null means that there is no Field.
     */

    // MEDIATOR - OBSERVER

    // creates an array of fields, pattern is given up

    /**
     * Creates an array of fields,
     *
     * @return
     */
    private ArrayList<ArrayList<Field>> assignFieldsToBoard() {
        ArrayList<ArrayList<Field>> newGameBoard = new ArrayList<>();
        ArrayList<Field> newRow;
        for (int i = 0; i < 19; i++) {
            newRow = new ArrayList<>();
            for (int j = 0; j < 19; j++) {
                if (i < 5 && j > 4 && j < i + 5) {
                    newRow.add(j, new GameField(Color.Red));
                } else if (i >= 5 && i < 9) {
                    if (j > i - 5 && j < 5) {
                        newRow.add(j, new GameField(Color.Black));
                    } else if (j >= 5 && j < i + 5) {
                        newRow.add(j, new GameField());
                    } else if (j >= i + 5 && j < 14) {
                        newRow.add(j, new GameField(Color.Green));
                    } else {
                        newRow.add(j, null);
                    }
                } else if (i == 9 && j > 4 && j < 14) {
                    newRow.add(j, new GameField());
                } else if (i > 9 && i < 14) {
                    if (j > 4 && j < i - 4) {
                        newRow.add(j, new GameField(Color.Orange));
                    } else if (j >= i - 4 && j < 14) {
                        newRow.add(j, new GameField());
                    } else if (j >= 14 && j < i + 5) {
                        newRow.add(j, new GameField(Color.Blue));
                    } else {
                        newRow.add(j, null);
                    }
                } else if (i >= 14 && j > i - 5 && j < 14) {
                    newRow.add(j, new GameField(Color.Yellow));
                } else {
                    newRow.add(j, null);
                }
            }
            newGameBoard.add(i, newRow);
        }
        return newGameBoard;
    }

    // creates new pawns and places it on appropriate field
    private void assignPawnsToBaseFields() {
        ArrayList<Field> list;
        for (int i = 0; i < 19; i++) {
            list = gameBoard.get(i);
            for (Field f : list) {
                if (f != null && f.getColor() != null && usedColorByPlayers.contains(f.getColor())) {
                    f.setPawn(new Pawn(f.getColor()));
                }
            }
        }
    }

    private ArrayList<Color> setColorsOfPlayers() {
        ArrayList<Color> colors = new ArrayList<>();

        switch (numberOfPlayers) {
            case 2:
                colors.add(Color.Red);
                colors.add(Color.Yellow);
                break;
            case 3:
                colors.add(Color.Red);
                colors.add(Color.Blue);
                colors.add(Color.Orange);
                break;
            case 4:
                colors.add(Color.Green);
                colors.add(Color.Black);
                colors.add(Color.Orange);
                colors.add(Color.Blue);
                break;
            case 6:
                colors.add(Color.Green);
                colors.add(Color.Black);
                colors.add(Color.Orange);
                colors.add(Color.Blue);
                colors.add(Color.Red);
                colors.add(Color.Yellow);
        }

        return colors;
    }

    /**
     * The method moves the pawn into its new field.
     *
     * @param oldPawnPosition previous position of the pawn
     * @param newPawnPosition new position of the pawn
     * @param player          reference to player who wants to move the pawn
     * @throws NullFieldException       throws if one or both of coordinates point to null
     * @throws WrongFieldStateException throws if there is a problem with pawn
     * @throws WrongMoveException       throws if move is incorrect
     * @throws WrongPawnColorException  throws if players wants to move somebody's else pawn
     */
    @Override
    public void movePawn(BoardCoordinates oldPawnPosition, BoardCoordinates newPawnPosition, Player player)
            throws NullFieldException, WrongFieldStateException, WrongMoveException, WrongPawnColorException {

        // TODO: Handle ArrayIndexOutOfBounds exception
        // TODO: Make this if statement less... ugly.
        Field newFieldForPawn = gameBoard.get(newPawnPosition.getRow()).get(newPawnPosition.getColumn());
        Field oldFieldForPawn = gameBoard.get(oldPawnPosition.getRow()).get(oldPawnPosition.getColumn());

        if (newFieldForPawn == null || oldFieldForPawn == null) {
            throw new NullFieldException();
        }
        else if (newFieldForPawn.isOccupied() || !oldFieldForPawn.isOccupied()) {
            throw new WrongFieldStateException();
        }
        else if (!oldFieldForPawn.getPawn().getColor().equals(player.getColor())) {
            throw new WrongPawnColorException();
        }
        else if (checkIfPawnIsInEnemyBase(oldPawnPosition, newPawnPosition)) {

            if (checkIfPawnInEnemyBaseCanMoveToNewField(oldPawnPosition, newPawnPosition)) {

                if (checkDistanceIsOneField(oldPawnPosition, newPawnPosition))
                    movePawnToNewField(oldPawnPosition, newPawnPosition);
                else if (checkMultiMoveIsCorrect(oldPawnPosition, newPawnPosition))
                    movePawnToNewField(oldPawnPosition, newPawnPosition);
            }
            else
                throw new WrongMoveException();
        }
        else {
            if (checkDistanceIsOneField(oldPawnPosition, newPawnPosition)) {
                movePawnToNewField(oldPawnPosition, newPawnPosition);
            }
            else if (checkMultiMoveIsCorrect(oldPawnPosition, newPawnPosition)) {
                movePawnToNewField(oldPawnPosition, newPawnPosition);
            }
            else
                throw new WrongMoveException();
        }
    }

    // can be use when we know the move is correct
    private void movePawnToNewField(BoardCoordinates oldField, BoardCoordinates newField) {
        gameBoard.get(newField.getRow()).get(newField.getColumn())
                .setPawn(gameBoard.get(oldField.getRow()).get(oldField.getColumn()).getPawn());
        gameBoard.get(oldField.getRow()).get(oldField.getColumn()).setPawn(null);
    }

    // checks whether we move the pawn by one field
    private boolean checkDistanceIsOneField(BoardCoordinates oldField, BoardCoordinates newField) {
        return (Math.abs(oldField.getRow() - newField.getRow()) + Math.abs(oldField.getColumn() - newField.getColumn())) == 1 ||
                ((oldField.getColumn() - newField.getColumn()) == 1 && (oldField.getRow() - newField.getRow()) == 1) ||
                ((oldField.getColumn() - newField.getColumn()) == -1 && (oldField.getRow() - newField.getRow()) == -1);
    }

    private boolean checkIfPawnIsInEnemyBase(BoardCoordinates oldField, BoardCoordinates newField) {

        Color oldFieldColor = gameBoard.get(oldField.getRow()).get(oldField.getColumn()).getColor(),
                newFieldColor = gameBoard.get(newField.getRow()).get(newField.getColumn()).getColor(),
                pawnColor = gameBoard.get(oldField.getRow()).get(oldField.getColumn()).getPawn().getColor();

        if (oldFieldColor != null) {
            // We are in a base
            // We are in an enemy base
            // We are at a home base
            return oldFieldColor != pawnColor;
        }
        else {
            // We are not in a base (meaning: we're on a neutral field)
            return false;
        }
    }

    // Checks whether the pawn is in the enemy's base.
    // If so,
    private boolean checkIfPawnInEnemyBaseCanMoveToNewField(BoardCoordinates oldField, BoardCoordinates newField) {

        // TODO: Implement
        // Sprawdź kolor pionka, powiązać kolor przeciwny, sprawdzić, czy jest w bazie, jak nie, to wywalić
        // TODO: MK - dodana metoda na pobieranie przeciwnego koloru - Color getOppositeColor(Color col)
        // mozna wykorzystać

        Color oldFieldColor = gameBoard.get(oldField.getRow()).get(oldField.getColumn()).getColor(),
                newFieldColor = gameBoard.get(newField.getRow()).get(newField.getColumn()).getColor(),
                pawnColor = gameBoard.get(oldField.getRow()).get(oldField.getColumn()).getPawn().getColor();

        if (oldFieldColor != null) {
            // If we are in a base

            if (oldFieldColor != pawnColor) {
                // If we are in an enemy's base (not ours)

                return newFieldColor == oldFieldColor;
            }
            else {
                // We're in our base
                return false;
            }
        }
        else {
            // Pawn is not in a base
            return false;
        }
    }

    /* The method finds possible ways for the pawn over other pawns,
        there is 6 ways to check:
                       xx
                       xPx
                        xx
        where P - pawn, x - fields where pawn can move to.
        There are two free spaces - top right corner and bottom left corner.
        These two spaces cannot be use, because gameBoard is imagination of real board in different way.
     */
    private ArrayList<BoardCoordinates> checkPossibleWaysForPawn(BoardCoordinates coordinates) {
        ArrayList<BoardCoordinates> possibleWays = new ArrayList<>();
        int X = coordinates.getColumn();
        int Y = coordinates.getRow();

        if (gameBoard.get(Y - 1).get(X) != null && gameBoard.get(Y - 1).get(X).isOccupied() &&
                gameBoard.get(Y - 2).get(X) != null && !gameBoard.get(Y - 2).get(X).isOccupied()) {
            possibleWays.add(new BoardCoordinates(Y - 2, X));
        }

        if (gameBoard.get(Y).get(X + 1) != null && gameBoard.get(Y).get(X + 1).isOccupied() &&
                gameBoard.get(Y).get(X + 2) != null && !gameBoard.get(Y).get(X + 2).isOccupied()) {
            possibleWays.add(new BoardCoordinates(Y, X + 2));
        }

        if (gameBoard.get(Y + 1).get(X + 1) != null && gameBoard.get(Y + 1).get(X + 1).isOccupied() &&
                gameBoard.get(Y + 2).get(X + 2) != null && !gameBoard.get(Y + 2).get(X + 2).isOccupied()) {
            possibleWays.add(new BoardCoordinates(Y + 2, X + 2));
        }

        if (gameBoard.get(Y + 1).get(X) != null && gameBoard.get(Y + 1).get(X).isOccupied() &&
                gameBoard.get(Y + 2).get(X) != null && !gameBoard.get(Y + 2).get(X).isOccupied()) {
            possibleWays.add(new BoardCoordinates(Y + 2, X));
        }

        if (gameBoard.get(Y).get(X - 1) != null && gameBoard.get(Y).get(X - 1).isOccupied() &&
                gameBoard.get(Y).get(X - 2) != null && !gameBoard.get(Y).get(X - 2).isOccupied()) {
            possibleWays.add(new BoardCoordinates(Y, X - 2));
        }

        if (gameBoard.get(Y - 1).get(X - 1) != null && gameBoard.get(Y - 1).get(X - 1).isOccupied() &&
                gameBoard.get(Y - 2).get(X - 2) != null && !gameBoard.get(Y - 2).get(X - 2).isOccupied()) {
            possibleWays.add(new BoardCoordinates(Y - 2, X - 2));
        }

        return possibleWays;
    }

    // checks that the move over other pawns is correct - recursive method
    private boolean checkMultiMoveIsCorrect(BoardCoordinates oldField, BoardCoordinates newField) {
        if (oldField.getRow() == newField.getRow() && oldField.getColumn() == newField.getColumn()) {
            return true;
        }

        ArrayList<BoardCoordinates> ways = checkPossibleWaysForPawn(oldField.getBoardCoordinates());
        boolean isMoveCorrect = false;

        for (BoardCoordinates bc : ways) {
            if (newField.getRow() == bc.getRow() && newField.getColumn() == bc.getColumn() || isMoveCorrect) {
                isMoveCorrect = true;
                break;
            } else {
                isMoveCorrect = checkMultiMoveIsCorrect(new BoardCoordinates(bc.getRow(), bc.getColumn()), newField);
            }
        }

        return isMoveCorrect;
    }

    /**
     * Looks for the pawns which are located nearby the pawn that is chosen to be the closest to the enemy's base.
     * This ensures that the bot behaviour is more random, and thus less predictable for the player.
     * @param playerPawnsCoordinates
     * @param candidatesToMove
     * @param column
     * @param row
     */
    private void findNearbyPawns(ArrayList<BoardCoordinates> playerPawnsCoordinates, ArrayList<BoardCoordinates> candidatesToMove, int column, int row) {
        int radius = 1;

        do {
            for (BoardCoordinates coordinate : playerPawnsCoordinates) {
                if (coordinate.getColumn() == column - radius || coordinate.getColumn() == column + radius || coordinate.getColumn() == column) {
                    if (coordinate.getRow() == row - radius || ( coordinate.getRow() == row && coordinate.getColumn() != column)) {
                        candidatesToMove.add(coordinate);
                        //System.out.println("Candidate row: " + coordinate.getRow() + ", column: " + coordinate.getColumn() + ", radius: " + radius);
                    }
                }
            }
            radius++;

        } while (candidatesToMove.size() < 4);
    }

    /**
     * Checks if pawns which are the candidates for our move are not blocked by other pawns, meaning that they would be unable to move.
     * @param candidatesToMove
     */
    private Map<BoardCoordinates, ArrayList<BoardCoordinates>>
        getCandidatesWhichCanMoveInDesiredDirectionOnly(ArrayList<BoardCoordinates> candidatesToMove, Color playerColor)
        throws WrongPawnColorException {

        // Bots with these colors will increase their row and/or column on move.
        // Hence, we'll check if the moves where these values change are correct.

        // Note that we assume here that the bot's pawn will move no more than once using the multi-move trick.
        // TODO: We don't want to be too ambitious or something here, and most games don't even allow multi-multi move.

        int single_dxdy[][], multi_dxdy[][];

        //ArrayList<BoardCoordinates> desiredCoorinatesToMoveInMultiMoveOnly = new ArrayList<BoardCoordinates>();
        //ArrayList<BoardCoordinates> desiredCoordinatesToMoveInSingleMoveOnly = new ArrayList<BoardCoordinates>();

        Map<BoardCoordinates, ArrayList<BoardCoordinates>> coordinatesWithPositionsToMoveTo = new HashMap<>();

        if (playerColor == Color.Red || playerColor == Color.Black) {
            single_dxdy = new int[][] {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
            multi_dxdy = new int[][] {{2, 0}, {0, 2}, {2, 1}, {1, 2}};
        }
        else if (playerColor == Color.Yellow || playerColor == Color.Blue) {
            single_dxdy = new int[][] {{0, -1}, {-1, 0}, {-1, -1}};
            multi_dxdy = new int[][] {{-2, 0}, {0, -2}, {-2, -1}, {-1, -2}};
        }
        else if (playerColor == Color.Orange) {
            single_dxdy = new int[][] {{0, 1}, {-1, 0}, {-1, 1}};
            multi_dxdy = new int[][] {{-2, 0}, {0, 2}, {-2, 2}};
        }
        else if (playerColor == Color.Green) {
            single_dxdy = new int[][] {{0, -1}, {1, 0}, {1, -1}};
            multi_dxdy = new int[][] {{2, 0}, {0, -2}, {2, -2}};
        }
        else
            throw new WrongPawnColorException();

        System.out.println("multi dx array:");
        for (int [] delta: multi_dxdy) {
            System.out.println("dx = " + delta[0] + ", dy = " + delta[1]);
        }

        for (BoardCoordinates candidate: candidatesToMove) {

            ArrayList<BoardCoordinates> everyPossibleWayToMoveInSingleMove = checkPossibleWaysForPawn(candidate);
            ArrayList<BoardCoordinates> currentPawnWaysForMultiMove = new ArrayList<>();
            ArrayList<BoardCoordinates> currentPawnWaysForSingleMove = new ArrayList<>();

            if (everyPossibleWayToMoveInSingleMove.size() == 0) {
                // If there are no possibilities for this pawn to move in a single move.

                System.out.println("Pawn row: " + candidate.getRow() + ", column: " + candidate.getColumn() + ", single moves: " + everyPossibleWayToMoveInSingleMove.size() + " checking multi-move");

                for (int[] delta: multi_dxdy) {
                    // Let's check if the multi move can be performed in the direction where it is desired.
                    int dx = delta[0], dy = delta[1];
                    BoardCoordinates toCheck = new BoardCoordinates(candidate.getRow() + dx, candidate.getColumn() + dy);

                    System.out.println("Checking multi-move to row: " + toCheck.getRow() + ", column: " + toCheck.getColumn());

                    if (checkMultiMoveIsCorrect(candidate, toCheck)) {
                        System.out.println("Adding to currentPawnWaysForMultiMove: " + toCheck.toString());
                        currentPawnWaysForMultiMove.add(toCheck);
                    }
                }

                if (currentPawnWaysForMultiMove.size() != 0) {
                    // Add all of the possibilities for the movement to the multi-moves array.
                    coordinatesWithPositionsToMoveTo.put(candidate, currentPawnWaysForMultiMove);
                }

            }
            else {
                // If it's possible for the pawn to move in a single move.

                System.out.println("Pawn row: " + candidate.getRow() + ", column: " + candidate.getColumn() + ", single moves: " + everyPossibleWayToMoveInSingleMove.size() + " checking single-move");

                for (int[] delta: single_dxdy) {
                    // Let's check if the single move can be performed in a desired direction.
                    int dx = delta[0], dy = delta[1];
                    BoardCoordinates toCheck = new BoardCoordinates(candidate.getRow() + dx, candidate.getColumn() + dy);

                    if (everyPossibleWayToMoveInSingleMove.contains(toCheck)) {
                        // If this move is determined to be valid, add the pawn to the array.
                        System.out.println("Adding to currentPawnWaysForSingleMove: " + toCheck.toString());
                        currentPawnWaysForSingleMove.add(toCheck);
                    }
                }

                if (currentPawnWaysForSingleMove.size() != 0) {
                    // Add all of the possibilities for the movement to the multi-moves array.
                    coordinatesWithPositionsToMoveTo.put(candidate, currentPawnWaysForSingleMove);
                }
            }
        }

        System.out.println("Current hashmap:");
        System.out.println(coordinatesWithPositionsToMoveTo);

        return coordinatesWithPositionsToMoveTo;
    }

    public ArrayList<BoardCoordinates> getCoordinatesOfPawnsClosestToTheEnemyBaseByColor(Color playerColor) {

        ArrayList<BoardCoordinates> playerPawnsCoordinates = getCoordinatesOfAllPawnsOfColor(playerColor),
                candidatesToMove = new ArrayList<>();
        int column = 0, row = 0;

        if (playerColor == Color.Red || playerColor == Color.Black) {
            // Yellow/blue is the enemy we're concerned with, since the bot does not attack others

            // First, find the red/black pawn that's the closest to the yellow pawn

            for (BoardCoordinates coordinate: playerPawnsCoordinates) {
                int coordinateColumn = coordinate.getColumn(),
                        coordinateRow = coordinate.getRow();

                if (coordinateRow > row || coordinateColumn > column) {
                    // It will be random in choosing whether it will be the max from column or max from row,
                    // depending on what order the pawn will be chosen.
                    column = coordinateColumn;
                    row = coordinateRow;
                }
            }

            //System.out.println("Max pawn row: " + row + ", column: " + column);
            candidatesToMove.add(new BoardCoordinates(row, column));

            // Find some pawns nearby to the furthest-placed one
            findNearbyPawns(playerPawnsCoordinates, candidatesToMove, column, row);

            // Return the pawns found
            return candidatesToMove;
        }
        else if (playerColor == Color.Yellow || playerColor == Color.Blue) {
            // Red/black is the enemy we're concerned with.
            // Start by finding the yellow/blue pawn closest to the red base.

            // Get a first player pawn
            row = playerPawnsCoordinates.get(0).getRow();
            column = playerPawnsCoordinates.get(0).getColumn();

            // If a pawn is found with lesser coordinates, set it as a maximum

            for (BoardCoordinates coordinate: playerPawnsCoordinates) {
                int coordinateColumn = coordinate.getColumn(),
                        coordinateRow = coordinate.getRow();

                if (coordinateRow < row || coordinateColumn < column) {
                    row = coordinateRow;
                    column = coordinateColumn;
                }
            }

            //System.out.println("Max pawn row: " + row + ", column: " + column);
            candidatesToMove.add(new BoardCoordinates(row, column));

            // Find some pawns nearby to the furthest-placed one
            findNearbyPawns(playerPawnsCoordinates, candidatesToMove, column, row);

            // Return the pawns found
            return candidatesToMove;
        }
        else if (playerColor == Color.Orange) {
            // Green is the enemy we're concerned with
            // Firstly, let's find the orange pawn that's closest to the enemy's base

            row = playerPawnsCoordinates.get(0).getRow();
            column = playerPawnsCoordinates.get(0).getColumn();

            for (BoardCoordinates coordinate: playerPawnsCoordinates) {
                int coordinateColumn = coordinate.getColumn(),
                        coordinateRow = coordinate.getRow();

                if (coordinateColumn > column) {
                    row = coordinateRow;
                    column = coordinateColumn;
                }
            }

            //System.out.println("Max pawn row: " + row + ", column: " + column);
            candidatesToMove.add(new BoardCoordinates(row, column));

            // Find some pawns nearby to the furthest-placed one
            findNearbyPawns(playerPawnsCoordinates, candidatesToMove, column, row);

            // Return the pawns found
            return candidatesToMove;
        }
        else if (playerColor == Color.Green) {
            // Orange is the enemy we're concerned with
            // Firstly, let's find the green pawn that's closest to the enemy's base

            row = playerPawnsCoordinates.get(0).getRow();
            column = playerPawnsCoordinates.get(0).getColumn();

            for (BoardCoordinates coordinate: playerPawnsCoordinates) {
                int coordinateColumn = coordinate.getColumn(),
                        coordinateRow = coordinate.getRow();

                if (coordinateColumn < column) {
                    row = coordinateRow;
                    column = coordinateColumn;
                }
            }

            //System.out.println("Max pawn row: " + row + ", column: " + column);
            candidatesToMove.add(new BoardCoordinates(row, column));

            // Find some pawns nearby to the furthest-placed one
            findNearbyPawns(playerPawnsCoordinates, candidatesToMove, column, row);

            // Return the pawns found
            return candidatesToMove;
        }
        else
            return null;
    }

    /**
     * Performs the movement of the randomly chosen pawn owned by the player which is handled by the bot.
     * @param bot
     */
    public void performBotMove(Player bot) {

        try {
            // Color of the bot
            Color botColor = bot.getColor();

            // Get the list of pawns we'll be chosing from.
            ArrayList<BoardCoordinates> pawnsToChooseFrom = getCoordinatesOfPawnsClosestToTheEnemyBaseByColor(botColor);
            System.out.println("Pawns we're chosing from:\n" + pawnsToChooseFrom);

            // Get the hashmap of pawns associated with the best possible directions to move to for a given color.
            Map<BoardCoordinates, ArrayList<BoardCoordinates>> mapOfPawnsToMove = getCandidatesWhichCanMoveInDesiredDirectionOnly(pawnsToChooseFrom, botColor);

            // Randomly chose the pawn that will be moved from the generated hashmap.
            Random generator = new Random();
            List<BoardCoordinates> pawns = new ArrayList<BoardCoordinates>(mapOfPawnsToMove.keySet());

            //System.out.println(mapOfPawnsToMove);

            BoardCoordinates pawnChosen = pawns.get(generator.nextInt(pawns.size() - 1));

            // Get the coordinates to which it's possible to move the chosen pawn.
            ArrayList<BoardCoordinates> possibileCoordinatesToMoveChosenPawn = mapOfPawnsToMove.get(pawnChosen);

            // Now, yet again randomly choose where shall we move it!
            BoardCoordinates newChosenPawnPosition = possibileCoordinatesToMoveChosenPawn.get(generator.nextInt(possibileCoordinatesToMoveChosenPawn.size() - 1));

            // Finally(!) move the pawn there.
            movePawnToNewField(pawnChosen, newChosenPawnPosition);

            // Jupijajej maderfaker.

        }
        catch (WrongPawnColorException ex) {
            System.out.printf("Provided color: " + bot.getColor().toString() + "is wrong.");
        }
    }

    public ArrayList<BoardCoordinates> getCoordinatesOfAllPawnsOfColor(Color playerColor) {

        ArrayList<BoardCoordinates> allPawns = getPawnsCoordinates();
        ArrayList<BoardCoordinates> pawnsOfChosenColor = new ArrayList<>();

        for (BoardCoordinates bc: allPawns) {
            Pawn pawn = gameBoard.get(bc.getRow()).get(bc.getColumn()).getPawn();

            if (pawn.getColor() == playerColor)
                pawnsOfChosenColor.add(bc);
        }

        return pawnsOfChosenColor;
    }

    /**
     * The method returns current coordinates of pawns.
     *
     * @return array of coordinates of pawns
     */
    @Override
    public ArrayList<BoardCoordinates> getPawnsCoordinates() {
        ArrayList<BoardCoordinates> pawnesCoordinates = new ArrayList<>();
        Field field;
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                field = gameBoard.get(i).get(j);
                if (field != null && field.isOccupied()) {
                    pawnesCoordinates.add(new BoardCoordinates(i, j));
                }
            }
        }

        return pawnesCoordinates;
    }

    /**
     * The method returns color of particular pawn.
     *
     * @param pawnPosition coordinates of pawn
     * @return color of pawn
     * @throws WrongFieldStateException throws if there is no pawn on the field (coordinates are wrong)
     */
    @Override
    public Color getPawnColor(BoardCoordinates pawnPosition) throws WrongFieldStateException {
        Field field = gameBoard.get(pawnPosition.getRow()).get(pawnPosition.getColumn());

        if (field == null) {
            throw new WrongFieldStateException();
        }
        else if  (!field.isOccupied()) {
            return null;
        }
        else {
            return field.getPawn().getColor();
        }
    }

    /**
     * The method checks if the player has all pawns in the opposite base.
     *
     * @param player the player whose result we want to check
     * @return true if all pawns of player are in the opposite base
     */
    @Override
    public boolean checkThePlayerIsAWinner(Player player) {
        Color colorOfPlayer = player.getColor();
        Field field;
        int baseSize = 10;
        int numberOfPawnsInBase = 0;

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                field = gameBoard.get(i).get(j);
                if (field != null && field.getColor().equals(getOppositeColor(colorOfPlayer))
                        && field.isOccupied() && field.getPawn().getColor().equals(colorOfPlayer)) {
                    numberOfPawnsInBase++;
                }
            }
        }

        return numberOfPawnsInBase == baseSize;
    }

    private Color getOppositeColor(Color col) {
        if(col.equals(Color.Red)) {
            return Color.Yellow;
        } else if(col.equals(Color.Yellow)) {
            return Color.Red;
        } else if(col.equals(Color.Blue)) {
            return Color.Black;
        } else if(col.equals(Color.Black)) {
            return Color.Blue;
        } else if(col.equals(Color.Green)) {
            return Color.Orange;
        } else if(col.equals(Color.Orange)) {
            return Color.Green;
        } else {
            return null;
        }
    }
}