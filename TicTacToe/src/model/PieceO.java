package model;

public class PieceO extends PlayingPiece {
    public PieceO() {
        super(PieceType.O);
    }

    @Override
    public char getSymbol() {
        return 'O';
    }
}
