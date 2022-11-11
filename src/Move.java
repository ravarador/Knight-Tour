public class Move {
    private int row;
    private int col;
    private int moveNo;

    public Move(Move src) {
        this.row = src.row;
        this.col = src.col;
        this.moveNo = src.moveNo;
    }
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getMoveNo() {
        return moveNo;
    }

    public void setMoveNo(int moveNo) {
        this.moveNo = moveNo;
    }

    public Move(int row, int col, int moveNo) {
        setRow(row);
        setCol(col);
        setMoveNo(moveNo);
    }
}
