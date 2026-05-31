package model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int size;
    private final PlayingPiece[][] board;

    public Board(int size) {
        this.size = size;
        this.board = new PlayingPiece[size][size];
    }

    public int getSize() {
        return size;
    }

    public PlayingPiece[][] getBoardMatrix() {
        return board;
    }

    public boolean addPiece(int row, int col, PieceType type) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return false;
        }
        if (board[row][col] != null) {
            return false;
        }
        if (type == PieceType.X) {
            board[row][col] = new PieceX();
        } else {
            board[row][col] = new PieceO();
        }
        return true;
    }

    public List<int[]> getFreeSpaces() {
        List<int[]> freeSpaces = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == null) {
                    freeSpaces.add(new int[]{i, j});
                }
            }
        }
        return freeSpaces;
    }

    public void displayBoard() {
        int width = size * 4 + 1;
        StringBuilder border = new StringBuilder();
        for (int b = 0; b < width; b++) border.append("-");
        System.out.println(border.toString());

        for (int i = 0; i < size; i++) {
            System.out.print("|");
            for (int j = 0; j < size; j++) {
                if (board[i][j] == null) {
                    System.out.print(" . |");
                } else {
                    System.out.print(" " + board[i][j].getSymbol() + " |");
                }
            }
            System.out.println();
            System.out.println(border.toString());
        }
    }

    public boolean isThereWinner(int row, int col, PieceType type) {
        boolean rowEqual = true;
        boolean colEqual = true;
        boolean diag1 = true;
        boolean diag2 = true;

        for (int i = 0; i < size; i++) {
            if (board[row][i] == null || board[row][i].getType() != type) {
                rowEqual = false;
                break;
            }
        }

        for (int i = 0; i < size; i++) {
            if (board[i][col] == null || board[i][col].getType() != type) {
                colEqual = false;
                break;
            }
        }

        for (int i = 0; i < size; i++) {
            if (board[i][i] == null || board[i][i].getType() != type) {
                diag1 = false;
                break;
            }
        }

        for (int i = 0; i < size; i++) {
            if (board[i][size - i - 1] == null || board[i][size - i - 1].getType() != type) {
                diag2 = false;
                break;
            }
        }

        return rowEqual || colEqual || diag1 || diag2;
    }
}
