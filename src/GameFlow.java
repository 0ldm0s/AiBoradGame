import java.util.Scanner;

public class GameFlow {

    private GameBoard gameBoard = new GameBoard();
    private Scanner scanner = new Scanner(System.in);
	
    GameFlow() {

        int playerTurn = 0;

        int endCounter = 0;
        
        while(playerTurn < 4) {
        	Player currentPlayer = gameBoard.getPlayers().get(playerTurn);

        	
        	if(currentPlayer instanceof Ai){
        		System.out.println(playerTurn + ":  ( " + currentPlayer.cardInfo() + ")");
        		((Ai) currentPlayer).takeAction(gameBoard);
        		//System.out.println("Player " + playerTurn + " done."); <---- TURN ON AGAIN
        	}
        	else{
        		gameBoard.printStatus(currentPlayer);
        		System.out.println("Select action: 1: discard, 2: play, 3 give hint");
        		
        		
        		
        		int action = scanner.nextInt();

        		
        		switch (action) {
				case 1:
					System.out.println("Which card?");
					int position = scanner.nextInt();
					currentPlayer.discard(gameBoard, position);
					break;
				case 2:
					System.out.println("Which card?");
					int positionPlay = scanner.nextInt();
					currentPlayer.playCard(gameBoard, positionPlay);
					break;
				case 3:
					System.out.println("Which player?");
					int player = scanner.nextInt();
					System.out.println("Color 1 or number 2?");
					int choice = scanner.nextInt();
					int value;
					if(choice == 1){
						System.out.println("Which color? R, B, W, Y, G");
						value = scanner.nextInt();
					}
					else{
						System.out.println("Which number?");
						value = scanner.nextInt();
					}
					currentPlayer.giveHint(gameBoard, player, choice, value);
					break;
				default:
					break;
				}
        	}
        	
            if(gameBoard.getLife() < 1){
            	break;
            }
            
            if(gameBoard.deck.cardsLeftInDeck() < 1){
            	endCounter++;
            	if(endCounter > 3){
            		break;
            	}
            }

            // Next players turn
            playerTurn = (playerTurn + 1) % 4;
        }

        System.out.println("Final score: " + gameBoard.getPoints());
        gameBoard.printStatus(gameBoard.getPlayers().get(playerTurn));
        
        scanner.close();
    }
}
