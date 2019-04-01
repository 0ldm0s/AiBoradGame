import java.util.ArrayList;
import java.util.Arrays;

public class GameBoard implements Cloneable{

	private static int LIFE_MAX = 3;
	private static int HINT_MAX = 8;
	
	private int life, hints, points;
	private ArrayList<Player> players; 
	private ArrayList<Card> discardedCards;
	public Deck deck;
	public int[] table;
	public int[][] cardsNotDiscarded;
	
	public GameBoard getStatus() {
		GameBoard gb = null;
		try {
			gb = this.clone();
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
	public GameBoard(boolean player) {
		super();
		life = LIFE_MAX;
		hints = HINT_MAX;
		deck = new Deck();
		if(player){
		players = new ArrayList<Player>(
				Arrays.asList(new Ai(deck.initialseHand(),0),
						new Ai(deck.initialseHand(),1),
						new Player(deck.initialseHand()),
						new Ai(deck.initialseHand(),3))
				);
		}
		else{
			players = new ArrayList<Player>(
					Arrays.asList(new Ai(deck.initialseHand(),0),
							new Ai(deck.initialseHand(),1),
							new Ai(deck.initialseHand(),2),
							new Ai(deck.initialseHand(),3))
					);
		}
		discardedCards = new ArrayList<Card>();
		
		table = new int[5];
		
		int[] temp = {3,2,2,2,1};
		cardsNotDiscarded = new int[5][5];
		for (int i = 0; i < 5; i++) {
			System.arraycopy(temp, 0, cardsNotDiscarded[i], 0, 5);
		}
	}
	
	// action 1
	public void discardCard(Card card) {
		if (hints < HINT_MAX) hints++;
		discardedCards.add(card);
		cardsNotDiscarded[card.getNumericalColour()][card.getNumber()-1]--;
	}
	
	// action 2
	public void useHintIfPossible(int player, int type, int value) {
		if (hints > 0) {
			Player p = players.get(player);
			p.updateInfo(type, value);
			hints--;
		}
	}
	
	// action 3
	public void putCardOnTable(Card card) {
		// determine if success or not
		if (table[card.getNumericalColour()] + 1 == card.getNumber()) {
			table[card.getNumericalColour()] += 1;
			points++;
		} else {
			discardedCards.add(card);
			cardsNotDiscarded[card.getNumericalColour()][card.getNumber()-1]--;
			life--;
		}
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
			// Clone all information
			GameBoard gb = (GameBoard) super.clone();
			gb.deck = deck.clone();
			gb.discardedCards = (ArrayList<Card>) discardedCards.clone();
			gb.hints = hints;
			gb.life = life;
			gb.players = new ArrayList<Player>();
			for (int i = 0; i < this.players.size(); i++) {
				gb.players.add(this.players.get(i).getClone());
			}
			gb.table = table.clone();
			gb.cardsNotDiscarded = new int[5][5];
			for (int i = 0; i < 5; i++) {
				System.arraycopy(this.cardsNotDiscarded[i], 0, gb.cardsNotDiscarded[i], 0, i);
			}
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
		
		StringBuilder discard = new StringBuilder();
		for (Card discardedCard : discardedCards) {
			discard.append(discardedCard.toString()).append(" ");
		}
		System.out.println("Discarded cards: " + discard);
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