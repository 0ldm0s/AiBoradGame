import java.util.Scanner;

public class GameFlow {

    private GameBoard gameBoard;
    private Scanner scanner = new Scanner(System.in);
	
	public static int maxPointMulti = 1;
	public static int hintMulti = 1;
	public static int infoMulti = 1;
	public static int lifeMulti = 10;
	public static int pointMulti = 14;

	GameFlow(boolean playerBool) {
		gameBoard = new GameBoard(playerBool);

		int playerTurn = 0;

		int endCounter = 0;

		while(playerTurn < 4) { 
			Player currentPlayer = gameBoard.getPlayers().get(playerTurn);
			//gameBoard.printStatus(currentPlayer);

			if(currentPlayer instanceof Ai){
				System.out.println("AI: " + playerTurn +": " );
				((Ai) currentPlayer).takeAction(gameBoard, currentPlayer);
			}
			else{
				gameBoard.printStatus(currentPlayer);
				
				String print = "Select action: ";
				if(gameBoard.getHints() != 8)
					print = print + "1: discard,";
				
				print = print +  " 2: play,";
				
				if(gameBoard.getHints() != 0)
				 print = print + " 3 give hint";

				System.out.println(print);

				int action = scanner.nextInt();

				switch (action) {
				case 1:
					System.out.println("Which card?");
					int position = scanner.nextInt()-1;
					currentPlayer.discard(gameBoard, position);
					break;
				case 2:
					System.out.println("Which card?");
					int positionPlay = scanner.nextInt()-1;
					currentPlayer.playCard(gameBoard, positionPlay);
					break;
				case 3:
					System.out.println("Which player?");
					int player = scanner.nextInt();
					System.out.println("Color 1 or number 2?");
					int choice = scanner.nextInt();
					int value;
					if(choice == 1){
						System.out.println("Which color? R: 0, B: 1, W: 2, Y: 3, G: 4");
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
		}
		
		System.out.println("****************************************");
        System.out.println("Final score: " + gameBoard.getPoints());
        
        scanner.close();
    }
}
