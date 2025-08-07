package TicTacToe;


import TicTacToe.Types.GameStatus;
import TicTacToe.Types.Symbol;
import TicTacToe.WinningStrategy.RowWinningStrategy;
import TicTacToe.WinningStrategy.ColumnWinningStrategy;
import TicTacToe.WinningStrategy.DiagonalWinningStrategy;
import java.util.*;

public class TicTacToeGameDemo {
    public static void run() {
        // Setup players
        Player p1 = new Player("Alice", new Symbol("X"));
        Player p2 = new Player("Bob", new Symbol("O"));
        List<Player> players = Arrays.asList(p1, p2);

        // Setup strategies
        List<TicTacToe.WinningStrategy.WinningStrategy> strategies = new ArrayList<>();
        strategies.add(new RowWinningStrategy());
        strategies.add(new ColumnWinningStrategy());
        strategies.add(new DiagonalWinningStrategy());

        // Get game instance and set strategies
        TicTacToeGame gameManager = TicTacToeGame.getInstance();
        gameManager.setStrategies(strategies);

        // 1. Test: Player wins by row
        String gameId1 = gameManager.startGame(players, 3);
        System.out.println("Test 1: Player wins by row");
        gameManager.playMove(gameId1, 0, 0); // X
        gameManager.playMove(gameId1, 1, 0); // O
        gameManager.playMove(gameId1, 0, 1); // X
        gameManager.playMove(gameId1, 1, 1); // O
        gameManager.playMove(gameId1, 0, 2); // X wins
        gameManager.playMove(gameId1, 0, 2); // X wins
        gameManager.printGame(gameId1);
        System.out.println("Status: " + gameManager.getStatus(gameId1));

        // 2. Test: Player wins by column
        String gameId2 = gameManager.startGame(players, 3);
        System.out.println("\nTest 2: Player wins by column");
        gameManager.playMove(gameId2, 0, 0); // X
        gameManager.playMove(gameId2, 0, 1); // O
        gameManager.playMove(gameId2, 1, 0); // X
        gameManager.playMove(gameId2, 1, 1); // O
        gameManager.playMove(gameId2, 2, 0); // X wins
        gameManager.printGame(gameId2);
        System.out.println("Status: " + gameManager.getStatus(gameId2));

        // 3. Test: Player wins by diagonal
        String gameId3 = gameManager.startGame(players, 3);
        System.out.println("\nTest 3: Player wins by diagonal");
        gameManager.playMove(gameId3, 0, 0); // X
        gameManager.playMove(gameId3, 0, 1); // O
        gameManager.playMove(gameId3, 1, 1); // X
        gameManager.playMove(gameId3, 0, 2); // O
        gameManager.playMove(gameId3, 2, 2); // X wins
        gameManager.printGame(gameId3);
        System.out.println("Status: " + gameManager.getStatus(gameId3));

        // 4. Test: Draw game
        String gameId4 = gameManager.startGame(players, 3);
        System.out.println("\nTest 4: Draw game");
        gameManager.playMove(gameId4, 0, 0); // X
        gameManager.playMove(gameId4, 0, 1); // O
        gameManager.playMove(gameId4, 0, 2); // X
        gameManager.playMove(gameId4, 1, 1); // O
        gameManager.playMove(gameId4, 1, 0); // X
        gameManager.playMove(gameId4, 1, 2); // O
        gameManager.playMove(gameId4, 2, 1); // X
        gameManager.playMove(gameId4, 2, 0); // O
        gameManager.playMove(gameId4, 2, 2); // X
        gameManager.printGame(gameId4);
        System.out.println("Status: " + gameManager.getStatus(gameId4));

        // 5. Test: Invalid move (cell occupied)
        String gameId5 = gameManager.startGame(players, 3);
        System.out.println("\nTest 5: Invalid move (cell occupied)");
        gameManager.playMove(gameId5, 0, 0); // X
        boolean valid = true;
        try {
            gameManager.playMove(gameId5, 0, 0); // O tries same cell
        } catch (Exception e) {
            valid = false;
            System.out.println("Caught expected error: " + e.getMessage());
        }
        System.out.println("Move valid: " + valid);
        gameManager.printGame(gameId5);

        // 6. Test: Invalid move (out of bounds)
        String gameId6 = gameManager.startGame(players, 3);
        System.out.println("\nTest 6: Invalid move (out of bounds)");
        valid = true;
        try {
            gameManager.playMove(gameId6, 3, 3); // Out of bounds
        } catch (Exception e) {
            valid = false;
            System.out.println("Caught expected error: " + e.getMessage());
        }
        System.out.println("Move valid: " + valid);
        gameManager.printGame(gameId6);
    }
}
