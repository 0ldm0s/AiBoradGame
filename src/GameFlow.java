public class GameFlow {

    GameBoard gameBoard = new GameBoard();

    GameFlow() {

        int playerTurn = 0;

        while(playerTurn < 3) { // TODO: Get status of the game

            // TODO: Get the card information known to the player with the turn

            // TODO: Print game board for player with the turn

            // TODO: Choose action to perform

            // TODO: Perform the action

            // Next players turn
            playerTurn = (playerTurn + 1) % 4;
        }

        System.out.println("Final score: "); // TODO: Get the final score of the game
    }
}
