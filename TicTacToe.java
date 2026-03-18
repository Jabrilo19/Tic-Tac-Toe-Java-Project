import java.util.Scanner;

public class TicTacToe {

    public static void main(String[] args) {

        // Scanner for player input
        Scanner scanner = new Scanner(System.in);

        // 3x3 game board initialized with spaces
        char[][] gameBoard = {
            {' ', ' ', ' '},
            {' ', ' ', ' '},
            {' ', ' ', ' '}
        };

        printBoard(gameBoard);

        // Main game loop: player (X) moves, then AI (O) moves
        while (true) {

            playerTurn(gameBoard, scanner);
            if (isGameFinished(gameBoard)) break;
            printBoard(gameBoard);

            computerTurn(gameBoard); // now uses minimax instead of random
            if (isGameFinished(gameBoard)) break;
            printBoard(gameBoard);
        }

        scanner.close();
    }

    // Determines if the game is over (winner or tie)
    private static boolean isGameFinished(char[][] gameBoard) {

        // Player win
        if (winCondition(gameBoard, 'X')) {
            printBoard(gameBoard);
            System.out.println("The player has achieved the impossible!");
            return true;
        }

        // AI win
        if (winCondition(gameBoard, 'O')) {
            printBoard(gameBoard);
            System.out.println("The Computer remains unbeatable!");
            return true;
        }

        // Check for empty spaces — if none, it's a tie
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j] == ' ') {
                    return false; // game still going
                }
            }
        }
        
        printBoard(gameBoard);
        System.out.println("The game has ended with no winner in sight...");
        return true;
    }

    // Checks all win conditions for a given symbol
    private static boolean winCondition(char[][] gameBoard, char symbol) {
        return
            // Rows
            (gameBoard[0][0] == symbol && gameBoard[0][1] == symbol && gameBoard[0][2] == symbol) ||
            (gameBoard[1][0] == symbol && gameBoard[1][1] == symbol && gameBoard[1][2] == symbol) ||
            (gameBoard[2][0] == symbol && gameBoard[2][1] == symbol && gameBoard[2][2] == symbol) ||

            // Columns
            (gameBoard[0][0] == symbol && gameBoard[1][0] == symbol && gameBoard[2][0] == symbol) ||
            (gameBoard[0][1] == symbol && gameBoard[1][1] == symbol && gameBoard[2][1] == symbol) ||
            (gameBoard[0][2] == symbol && gameBoard[1][2] == symbol && gameBoard[2][2] == symbol) ||

            // Diagonals
            (gameBoard[0][0] == symbol && gameBoard[1][1] == symbol && gameBoard[2][2] == symbol) ||
            (gameBoard[0][2] == symbol && gameBoard[1][1] == symbol && gameBoard[2][0] == symbol);
    }

    private static void playerTurn(char[][] gameBoard, Scanner scanner) {
        String userInput;

        while (true) {
            System.out.println("Where would you like to play? (1-9)");
            userInput = scanner.nextLine();

            if (isValidMove(gameBoard, userInput)) {
                break;
            } else {
                System.out.println(userInput + " is not a valid move.");
            }
        }

        placeMove(gameBoard, userInput, 'X');
    }


    //The minimax AI evaluates every possible move and chooses the move that guarantees the best outcome
    private static void computerTurn(char[][] gameBoard) {

        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        // Try every possible move (1–9)
        for (int i = 1; i <= 9; i++) {

            if (isValidMove(gameBoard, Integer.toString(i))) {

                // Simulate placing 'O'
                placeMove(gameBoard, Integer.toString(i), 'O');

                // Evaluate the board using minimax
                int score = minimax(gameBoard, false);

                // Undo the move
                placeMove(gameBoard, Integer.toString(i), ' ');

                // Choose the move with the highest score
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }

        System.out.println("Computer chose " + bestMove);
        placeMove(gameBoard, Integer.toString(bestMove), 'O');
    }

    // minimax algorithem
    /*
       Minimax recursively simulates every possible game state - essentially looking into the future.
       - If AI wins → +1
       - If player wins → -1
       - If tie → 0

       isMaximizing = true  → AI's turn (tries to maximize score)
       isMaximizing = false → Player's turn (tries to minimize score)

       the comupterTurn method uses the score to pick the move with the best outcome
    */
    private static int minimax(char[][] gameBoard, boolean isMaximizing) {

        // Terminal states
        if (winCondition(gameBoard, 'O')) return 1;   // AI wins
        if (winCondition(gameBoard, 'X')) return -1;  // Player wins

        // Check for tie
        boolean movesLeft = false;
        for (int i = 0; i < gameBoard.length; i++) {          // loop through rows
            for (int j = 0; j < gameBoard[i].length; j++) {   // loop through columns
                if (gameBoard[i][j] == ' ') {
            movesLeft = true;
        }
    }
}
        if (!movesLeft) {
            return 0;
        } 

        // AI's turn (maximize score)
        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;

            for (int i = 1; i <= 9; i++) {
                if (isValidMove(gameBoard, Integer.toString(i))) {
                    placeMove(gameBoard, Integer.toString(i), 'O');
                    int score = minimax(gameBoard, false);
                    placeMove(gameBoard, Integer.toString(i), ' ');
                    bestScore = Math.max(score, bestScore);
                }
            }
            return bestScore;

        } else {
            // Player's turn (minimize score)
            // simulation of a pefect player
            
            int bestScore = Integer.MAX_VALUE;
            for (int i = 1; i <= 9; i++) {
                if (isValidMove(gameBoard, Integer.toString(i))) {
                    placeMove(gameBoard, Integer.toString(i), 'X');
                    int score = minimax(gameBoard, true);
                    placeMove(gameBoard, Integer.toString(i), ' ');
                    bestScore = Math.min(score, bestScore);
                }
            }
            return bestScore;
        }
    }

    // move validation
    private static boolean isValidMove(char[][] gameBoard, String position) {
        switch (position) {
            case "1": return gameBoard[0][0] == ' ';
            case "2": return gameBoard[0][1] == ' ';
            case "3": return gameBoard[0][2] == ' ';
            case "4": return gameBoard[1][0] == ' ';
            case "5": return gameBoard[1][1] == ' ';
            case "6": return gameBoard[1][2] == ' ';
            case "7": return gameBoard[2][0] == ' ';
            case "8": return gameBoard[2][1] == ' ';
            case "9": return gameBoard[2][2] == ' ';
            default: return false;
        }
    }
    
    // updating the board
    // Places a symbol ('X', 'O', or ' ') on the board
    private static void placeMove(char[][] gameBoard, String position, char symbol) {
        switch (position) {
            case "1": gameBoard[0][0] = symbol; break;
            case "2": gameBoard[0][1] = symbol; break;
            case "3": gameBoard[0][2] = symbol; break;
            case "4": gameBoard[1][0] = symbol; break;
            case "5": gameBoard[1][1] = symbol; break;
            case "6": gameBoard[1][2] = symbol; break;
            case "7": gameBoard[2][0] = symbol; break;
            case "8": gameBoard[2][1] = symbol; break;
            case "9": gameBoard[2][2] = symbol; break;
        }
    }

    // printing the board
    private static void printBoard(char[][] gameBoard) {
        System.out.println(gameBoard[0][0] + "|" + gameBoard[0][1] + "|" + gameBoard[0][2]);
        System.out.println("-+-+-");
        System.out.println(gameBoard[1][0] + "|" + gameBoard[1][1] + "|" + gameBoard[1][2]);
        System.out.println("-+-+-");
        System.out.println(gameBoard[2][0] + "|" + gameBoard[2][1] + "|" + gameBoard[2][2]);
    }
}
