import java.util.ArrayList;
import java.util.Arrays;

public class GameBoard {

	public static int LIFE_MAX = 3;
	public static int HINT_MAX = 8;
	
	private int life, hints, points;
	//private ArrayList<Player> players; // TODO player class missing // should be in game flow? #GRASP
	private ArrayList<Card1> discardedCards; // TODO card class missing
	private Deck1 deck; // TODO deck class missing
	private int[] table;
	
	public GameBoard getStatus() {
		GameBoard gb = null;
		try {
			gb = (GameBoard) this.clone();
			//gb.players.remove(currentPlayer);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return gb;
	}
	
	// initialize board
	public GameBoard() {
		super();
		life = LIFE_MAX;
		hints = HINT_MAX;
//		players = new ArrayList<Player>(
//				Arrays.asList(new Player(), 
//						new AI(), 
//						new AI(), 
//						new AI())
//				);
		discardedCards = new ArrayList<Card1>();
		deck = new Deck1();
		table = new int[4];
	}
	
	// action 1
	public Card1 discardCard(Card1 card) {
		if (hints < HINT_MAX) hints++;
		discardedCards.add(card);
		return deck.drawCard();
	}
	
	// action 2
	public boolean useHintIfPossible() {
		if (hints > 0) {
			hints--;
			return true;
		} else {
			return false;
		}
	}
	
	// action 3
	public Card1 putCardOnTable(Card1 card) {
		// determine if success or not
		if (true) { // TODO
			points++;
		} else {
			life--;
		}
		return deck.drawCard();
		
	}
	
	// print board for current player POV
//	public void printStatus(Player[] players) {
//		System.out.println("------------------------------------------");
//		System.out.println(String.format("Life: %d \t Hints: %d Points: %d", life, hints, points));
//		for (Player player : players) {
//			System.out.println(player.toString()); // TODO override player.toString
//		}
//	}	
}

class Player1 {
//	gets ref to gb when performing action
//	1: discardCard: return discarded card
//	drawCard: ask gb for new card
//	2: updateWithHint: update players card info
//	3: put card ontable: replace card with new one
}
class AI1 extends Player1{
//	perform best possible action, also updates board accordingly
}
class Deck1 {
	public Card1 drawCard() { return new Card1(); }
//	drawCard return card from deck
}
class Card1 {
}
class GameFlow1 {
//	have the players
//	have the "print board" uses getStatus
//	get players card info excluding self
	
}