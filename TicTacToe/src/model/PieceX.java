package model;

public class PieceX extends PlayingPiece {
    public PieceX() {
        super(PieceType.X);
    }

    @Override
    public char getSymbol() {
        return 'X';
    }
}
