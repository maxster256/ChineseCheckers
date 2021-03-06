import java.util.ArrayList;

public class Board implements BoardInterface {

    private int numberOfPlayers;
    private ArrayList<ArrayList<Field>> gameBoard;
    private ArrayList<Color> usedColorByPlayers; // colors of pawns

    Board(int numberOfPlayers) throws IllegalArgumentException {

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

    RED_, BLAC, GREE, ORAN, YELL and BLUE are colors of BaseFields and EMPT is Neutral.
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
        else if (checkIfPawnIsInEnemyBase(oldPawnPosition)) {

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

    private boolean checkIfPawnIsInEnemyBase(BoardCoordinates oldField) {

        Color oldFieldColor = gameBoard.get(oldField.getRow()).get(oldField.getColumn()).getColor(),
                pawnColor = gameBoard.get(oldField.getRow()).get(oldField.getColumn()).getPawn().getColor();

        if (oldFieldColor != null) {
            return oldFieldColor == getOppositeColor(pawnColor);
        }
        else {
            // We are not in a base (meaning: we're on a neutral field)
            return false;
        }
    }

    // Checks whether the pawn is in the enemy's base.
    // If so,
    private boolean checkIfPawnInEnemyBaseCanMoveToNewField(BoardCoordinates oldField, BoardCoordinates newField) {

        Color oldFieldColor = gameBoard.get(oldField.getRow()).get(oldField.getColumn()).getColor(),
                newFieldColor = gameBoard.get(newField.getRow()).get(newField.getColumn()).getColor();

        return newFieldColor == oldFieldColor;
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

    // Bot behavior
    // Assumption: Bot always moves towards the base of the enemy that's in front of him

    public ArrayList<Pawn> getPawnsClosestToTheEnemyBaseByColor(Color playerColor) {

        ArrayList<BoardCoordinates> playerPawnsCoordinates = getCoordinatesOfAllPawnsOfColor(playerColor);
        ArrayList<BoardCoordinates> enemyPawnsCoordinates;


        // I couldn't find any mathematical scheme due to irregular shape of the board.
        // Hence, there will be a few if statements...

        if (playerColor == Color.Red) {
            // Yellow is the enemy we're concerned with, since the bot does not attack others

            enemyPawnsCoordinates = getCoordinatesOfAllPawnsOfColor(Color.Yellow);

            // Find the red pawn that's the closest to the yellow pawn

            int column = 0, row = 0;

            for (BoardCoordinates coordinate: playerPawnsCoordinates) {
                int coordinateColumn = coordinate.getColumn(), coordinateRow = coordinate.getRow();

                if (coordinateColumn > column || coordinateRow > row) { // TODO: Consult that one!
                    column = coordinateColumn;
                    row = coordinateRow;
                }
            }

            System.out.println("Max row: " + row + ", column: " + column);

            ArrayList<BoardCoordinates> candidatesToMove = new ArrayList<>();
            //candidatesToMove.add(new BoardCoordinates(row, column));

            // Find the pawns which are close to the pawn we've just found

            // How to determine which pawns are close to each other (alghorithm)?
            // Idea: check the "neighbourhood" of the pawn closest to the enemy's base
            // If no pawns are found in the neighbourhood, increase the "radius" by 1
            // Finish when there are at least four (or maybe three?) pawns chosen by this method

            int radius = 1;

            // If the first pawn is very far from the base, and there are no more pawns in radius of 2,
            // there's probably no need to look for another pawns to move?

            // But, on the other way, the game will just keep moving the pawn until it reaches the enemy's base,
            // so that will be dull.

            // !
            // Can do this by sorting, but:
            // TODO: MAKE COORDINATES COMPARABLE! And that's rather difficult.

            for (BoardCoordinates coordinate: playerPawnsCoordinates) {
                if (checkDistanceIsOneField(coordinate, new BoardCoordinates(row, column))) { // That should work!
                    candidatesToMove.add(coordinate);
                    System.out.println("Added pawn with row: " + row + ", column: " + column + ", using one field distance.");
                }
//                else if (checkMultiMoveIsCorrect(new BoardCoordinates(row, column), coordinate)) {
//                    candidatesToMove.add(coordinate);
//                    System.out.println("Added pawn with row: " + row + ", column: " + column + ", using multi-move distance.");
//                }
            }


            return null;
        }



        return null;
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
