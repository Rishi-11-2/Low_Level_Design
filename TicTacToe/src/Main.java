import model.PieceType;
import model.Player;
import service.GameService;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("==============================================================");
        System.out.println("          TICTACTOE LOW LEVEL DESIGN SYSTEM BOOT              ");
        System.out.println("==============================================================");

        // Setup Players
        Player rishi = new Player("Rishi", PieceType.X);
        Player bozz = new Player("Bozz", PieceType.O);
        List<Player> playerList = new ArrayList<>();
        playerList.add(rishi);
        playerList.add(bozz);

        // ==============================================================
        // GAME 1: Winning Game (Rishi Wins by Row 0)
        // ==============================================================
        System.out.println("\n==============================================================");
        System.out.println("   GAME 1: Rishi (X) vs Bozz (O) - Rishi Wins by Row 0        ");
        System.out.println("==============================================================");

        GameService game1 = new GameService(playerList, 3);
        List<int[]> moves1 = new ArrayList<>();
        // Rishi places X, Bozz places O
        moves1.add(new int[]{0, 0}); // Rishi (X)
        moves1.add(new int[]{1, 0}); // Bozz (O)
        moves1.add(new int[]{0, 1}); // Rishi (X)
        moves1.add(new int[]{1, 1}); // Bozz (O)
        moves1.add(new int[]{0, 2}); // Rishi (X) -> Winner!

        String winner1 = game1.runMockGame(moves1);
        System.out.println("Game 1 Result: Winner is '" + winner1 + "'");

        // ==============================================================
        // GAME 2: Tie Game Simulation
        // ==============================================================
        System.out.println("\n==============================================================");
        System.out.println("   GAME 2: Rishi (X) vs Bozz (O) - Tie Game                   ");
        System.out.println("==============================================================");

        GameService game2 = new GameService(playerList, 3);
        List<int[]> moves2 = new ArrayList<>();
        /*
           Board matrix placement for a tie:
           X | O | X
           ---------
           X | O | O
           ---------
           O | X | X
        */
        moves2.add(new int[]{0, 0}); // Rishi (X)
        moves2.add(new int[]{0, 1}); // Bozz (O)
        moves2.add(new int[]{0, 2}); // Rishi (X)
        moves2.add(new int[]{1, 1}); // Bozz (O)
        moves2.add(new int[]{1, 0}); // Rishi (X)
        moves2.add(new int[]{1, 2}); // Bozz (O)
        moves2.add(new int[]{2, 1}); // Rishi (X)
        moves2.add(new int[]{2, 0}); // Bozz (O)
        moves2.add(new int[]{2, 2}); // Rishi (X)

        String winner2 = game2.runMockGame(moves2);
        System.out.println("Game 2 Result: Winner is '" + winner2 + "'");

        System.out.println("\n==============================================================");
        System.out.println("          TICTACTOE SYSTEM SIMULATION COMPLETE                ");
        System.out.println("==============================================================");
    }
}
