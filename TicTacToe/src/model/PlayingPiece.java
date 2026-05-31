package model;

public abstract class PlayingPiece {
    private final PieceType type;

    public PlayingPiece(PieceType type) {
        this.type = type;
    }

    public PieceType getType() {
        return type;
    }

    public abstract char getSymbol();
}
