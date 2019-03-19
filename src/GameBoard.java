import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class GameBoard implements Cloneable{

	public static int LIFE_MAX = 3;
	public static int HINT_MAX = 8;
	
	private int life, hints, points;
	private ArrayList<Player> players; // TODO player class missing // should be in game flow? #GRASP
	private ArrayList<Card> discardedCards; // TODO card class missing
	private Deck deck; // TODO deck class missing
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
	
	
	
	public ArrayList<Player> getPlayers() {
		return players;
	}



	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}



	// initialize board
	public GameBoard() {
		super();
		life = LIFE_MAX;
		hints = HINT_MAX;
		deck = new Deck();
		players = new ArrayList<Player>(
				Arrays.asList(new Player(deck.initialseHand()), 
						new Ai(deck.initialseHand()), 
						new Ai(deck.initialseHand()), 
						new Ai(deck.initialseHand()))
				);
		discardedCards = new ArrayList<Card>();
		
		table = new int[5];
	}
	
	// action 1
	public Card discardCard(Card card) {
		if (hints < HINT_MAX) hints++;
		discardedCards.add(card);
		return deck.draw();
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
	public Card putCardOnTable(Card card) {
		// determine if success or not
		if (true) { // TODO
			points++;
		} else {
			life--;
		}
		return deck.draw();
		
	}
	
	public int getPoints(){
		return points;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getHints() {
		return hints;
	}

	public void setHints(int hints) {
		this.hints = hints;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
	
	public GameBoard getClone(){
		try {
			GameBoard gb = (GameBoard) super.clone();
			gb.deck = deck.clone();
			gb.discardedCards = (ArrayList<Card>) discardedCards.clone();
			gb.hints = hints;
			gb.life = life;
			gb.players = (ArrayList<Player>) players.clone();
			gb.table = table.clone();
			return gb;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		

	}
	
	// print board for current player POV
	public void printStatus(Player player) {
		System.out.println("------------------------------------------");
		System.out.println(String.format("Life: %d \t Hints: %d Points: %d cards:  %d", life, hints, points, deck.cardsLeftInDeck()));
		//Print stak på bordet
		//Pritn discard bunke
		
		String[] colors = {"R", "G", "B", "Y", "W"};
		
		for (int i = 0; i < table.length; i++) {
			System.out.println(colors[i] + ": " + table[i]);
		}
		
		String discard = "";
		for (int i = 0; i < discardedCards.size(); i++) {
			discard += discardedCards.get(i).toString() + " " ;
		}
		System.out.println("Discard: " + discard);
		for (Player p : players) {
			if(player.equals(p)){
				System.out.println(p.toString());
				continue;
			}
			System.out.println(p.cardInfo()); // TODO override player.toString
		}
	}	
	
	@Override
	public GameBoard clone() throws CloneNotSupportedException{
		return (GameBoard) super.clone();
	}
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