import java.util.ArrayList;
import java.util.Arrays;

public class GameBoard implements Cloneable{

	public static int LIFE_MAX = 3;
	public static int HINT_MAX = 8;
	
	private int life, hints, points;
	private ArrayList<Player> players; 
	private ArrayList<Card> discardedCards;
	public Deck deck;
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
				Arrays.asList(new Ai(deck.initialseHand()), 
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
	public boolean useHintIfPossible(int player, int type, int value) {
		if (hints > 0) {
			Player p = players.get(player);
			//int plusValue = 0;
			//if(type == 2)
				//plusValue = -1;
			p.updateInfo(type, value);
			hints--;
			return true;
		} else {
			return false;
		}
	}
	
	// action 3
	public Card putCardOnTable(Card card) {
		// determine if success or not
		if (table[card.getNumericalColour()] + 1 == card.getNumber()) {
			table[card.getNumericalColour()] += 1;
			points++;
		} else {
			discardedCards.add(card);
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
	
	
	@SuppressWarnings("unchecked")
	public GameBoard getClone(){
		try {
			GameBoard gb = (GameBoard) super.clone();
			gb.deck = deck.clone();
			gb.discardedCards = (ArrayList<Card>) discardedCards.clone();
			gb.hints = hints;
			gb.life = life;
			gb.players = new ArrayList<Player>();
			for (int i = 0; i < this.players.size(); i++) {
				gb.players.add(this.players.get(i).clone());				
			}
			gb.table = table.clone();
			return gb;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
		

	}
	
	// print board for current player POV
	public void printStatus(Player player) {
		System.out.println("------------------------------------------");
		System.out.println(String.format("Life: %d \t Hints: %d Points: %d cards:  %d", life, hints, points, deck.cardsLeftInDeck()));
		String[] colors = {"R", "G", "B", "Y", "W"};
		
		for (int i = 0; i < table.length; i++) {
			System.out.println(colors[i] + ": " + table[i]);
		}
		
		String discard = "";
		for (int i = 0; i < discardedCards.size(); i++) {
			discard += discardedCards.get(i).toString() + " " ;
		}
		System.out.println("Discard: " + discard);
		int i = 0;
		for (Player p : players) {
			if(player.equals(p)){
				System.out.println("Player " + i + ": " + p.toString());
				i++;
				continue;
			}
			System.out.println("Player " + i + ": " + p.cardInfo());
			i++;
		}
	}	
	
	@Override
	public GameBoard clone() throws CloneNotSupportedException{
		return (GameBoard) super.clone();
	}	
}