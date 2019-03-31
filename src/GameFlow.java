import java.util.Scanner;

public class GameFlow {

	GameBoard gameBoard = new GameBoard();
	Scanner scanner = new Scanner(System.in);
	
	public static int maxPointMulti = 0;
	public static int hintMulti = 2;
	public static int infoMulti = 1;
	public static int lifeMulti = 0;
	public static int pointMulti = 4;

	GameFlow() {

		int playerTurn = 0;

		int endCounter = 0;

		while(playerTurn < 4) { // TODO: Get status of the game
			Player currentPlayer = gameBoard.getPlayers().get(playerTurn);
			//gameBoard.printStatus(currentPlayer);

			if(currentPlayer instanceof Ai){
				//System.out.println("****************************************************'");
//				System.out.println(playerTurn + ":  ( " + currentPlayer.cardInfo() + ")");
				//        		for (Player p : gameBoard.getPlayers()) {
				//					if(p.equals(currentPlayer))
				//						continue;
				//					System.out.println(p.cardInfo());
				//				}
				//        		System.out.println(gameBoard.getPlayers().get(playerTurn));
				//System.out.println(currentPlayer.toString()); //FOR DEBUG
				((Ai) currentPlayer).takeAction(gameBoard, currentPlayer);
				//System.out.println("Player " + playerTurn + " done."); <---- TURN ON AGAIN
			}
			else{
				gameBoard.printStatus(currentPlayer);
				System.out.println("Select action: 1: discard, 2: play, 3 give hint");



				int action = scanner.nextInt();

				//currentPlayer.takeAction(gameBoard, action);

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
						value = scanner.nextInt()-1;
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
				if(endCounter > 4){
					break;
				}
			}


			// Next players turn
			playerTurn = (playerTurn + 1) % 4;
//			if(playerTurn == 0)
//				break;
		}

//		System.out.println("Final score: " + gameBoard.getPoints());
//		gameBoard.printStatus(gameBoard.getPlayers().get(playerTurn)); // TODO: Get the final score of the game
//		System.out.println(gameBoard.getPlayers().get(playerTurn).cardInfo());
		scanner.close();
	}
}
