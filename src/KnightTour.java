import Helpers.ArrayHelper;
import java.util.ArrayList;

public class KnightTour {
    private final int[] ROW_MOVES = { 1, 1, 2, 2, -1, -1, -2, -2 };

    private final int[] COL_MOVES = { 2, -2, 1, -1, 2, -2, 1, -1 };

    private final int EMPTY_BOARD[][] = {
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
    };

    private ArrayList<Move> moves = new ArrayList<>();
    private int startRow;
    private int startCol;

    public int getStartRow() {
        return startRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public void setStartCol(int startCol) {
        this.startCol = startCol;
    }

    public void Reset() {
        moves.clear();
        board = ArrayHelper.cloneArray(EMPTY_BOARD);
    }

    private int board[][] = {
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
    };

    public int[][] getBoard() {
        return board;
    }
    public ArrayList<Move> getMoves() {
        return moves;
    }

    public void startTour(int startRow, int startCol) {
        setStartRow(startRow);
        setStartCol(startCol);

        int moveCounter = 0;
        int row = startRow;
        int col = startCol;

        // Set the starting point
        board[row][col] = ++moveCounter;
        moves.add(new Move(row, col, moveCounter));

        while(true) {
            ArrayList<ValidMove> validMoves = new ArrayList<>();

            int currentMoveScore = 0;

            // Get all possible, valid moves
            for (int moveIndex = 0; moveIndex < 8; moveIndex++) {
                int rowNew = row + ROW_MOVES[moveIndex];
                int colNew = col + COL_MOVES[moveIndex];

                if (ifValidMove(board, rowNew, colNew)) {
                    validMoves.add(new ValidMove(rowNew, colNew, moveCounter));
                    currentMoveScore++;
                }
            }

            if (currentMoveScore == 0 || moveCounter == 64) {
                break;
            }

            // Select the lowest accessibility score from the possible moves of each valid move
            int currentScore = 999;
            for (ValidMove validMove : validMoves) {
                if (validMove.score < currentScore) {
                    currentScore = validMove.score;
                    row = validMove.row;
                    col = validMove.col;
                }
            }

            board[row][col] = ++moveCounter;
            moves.add(new Move(row, col, moveCounter));
        }

        displayBoard(board);
        System.out.println("Is Closed Tour? " + isClosedTour(row, col, startRow, startCol));
        System.out.println("Game is over after " + moveCounter + " moves!");
        System.out.println();
    }

    public boolean isClosedTour(int row, int col, int startRow, int startCol) {
        for (int moveIndex = 0; moveIndex < 8; moveIndex++) {
            int rowNew = row + ROW_MOVES[moveIndex];
            int colNew = col + COL_MOVES[moveIndex];

            if (rowNew == startRow && startCol == colNew) {
                return true;
            }
        }

        return false;
    }

    private boolean ifValidMove(int[][] visited, int rowNew, int colNew)
    {
        if ((rowNew >= 0) &&
                (rowNew < 8) &&
                (colNew >= 0) &&
                (colNew < 8) &&
                (visited[rowNew][colNew] == 0))
        {
            return true;
        }

        return false;
    }

    private void displayBoard(int[][] visited) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.printf("%4s", visited[i][j]);
            }
            System.out.println();
        }
    }

    private class ValidMove {
        private int row;
        private int col;
        private int score;

        private ValidMove(int row, int col, int moveCounter) {
            this.row = row;
            this.col = col;

            this.score = computeScore(moveCounter);
        }

        private int computeScore(int moveCounter) {
            int[][] tempBoard = ArrayHelper.cloneArray(board);

            tempBoard[row][col] = moveCounter + 1;

            int score = 0;
            for (int moveIndex = 0; moveIndex < 8; moveIndex++) {
                int rowNew = row + ROW_MOVES[moveIndex];
                int colNew = col + COL_MOVES[moveIndex];

                if (ifValidMove(tempBoard, rowNew, colNew)) {
                    score += 1;
                }
            }

            return score;
        }
    }
}
