package service;

import model.Board;
import model.Player;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class GameService {
    private final Deque<Player> players;
    private final Board board;

    public GameService(List<Player> playerList, int boardSize) {
        this.players = new LinkedList<>(playerList);
        this.board = new Board(boardSize);
    }

    public Board getBoard() {
        return board;
    }

    public String runMockGame(List<int[]> simulatedMoves) {
        System.out.println("[TicTacToe] Starting simulated mock game turns.");
        int moveIdx = 0;

        while (true) {
            board.displayBoard();

            List<int[]> freeSpaces = board.getFreeSpaces();
            if (freeSpaces.isEmpty()) {
                System.out.println("[Game Complete] Tie. No free spaces left.");
                return "tie";
            }

            if (moveIdx >= simulatedMoves.size()) {
                System.out.println("[Simulation End] Exceeded simulated moves queue. Tie.");
                return "tie";
            }

            Player activePlayer = players.pollFirst();
            int[] move = simulatedMoves.get(moveIdx++);
            int row = move[0];
            int col = move[1];

            System.out.println("[Turn] Player '" + activePlayer.getName() + "' placing " + activePlayer.getPiece().name() + " on (" + row + ", " + col + ")");

            boolean success = board.addPiece(row, col, activePlayer.getPiece());
            if (!success) {
                System.out.println("[Invalid Move] Coordinates (" + row + ", " + col + ") are occupied or out of bounds. Retry simulated.");
                players.addFirst(activePlayer);
                continue;
            }

            boolean winner = board.isThereWinner(row, col, activePlayer.getPiece());
            if (winner) {
                board.displayBoard();
                System.out.println("[Game Complete] Winner: " + activePlayer.getName());
                return activePlayer.getName();
            }

            players.addLast(activePlayer);
        }
    }
}
