package model;

public class Player {
    private final String name;
    private final PieceType piece;

    public Player(String name, PieceType piece) {
        this.name = name;
        this.piece = piece;
    }

    public String getName() {
        return name;
    }

    public PieceType getPiece() {
        return piece;
    }
}
