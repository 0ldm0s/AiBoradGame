import java.util.ArrayList;

public class Ai extends Player{

	public Ai(Card[] cards){
		super(cards);
	}
	
	//max max - start rules
	

	// Given a GameBoard the ai makes a decision and executes it
	public void takeAction(GameBoard gb){
		int temp = totalInfo;

		// Update known information
		autoUpdate(gb);
		if(totalInfo != temp) {
			System.out.println("Auto update did something " + (totalInfo - temp) + " bits of informationen added");

		}
		double[] play = new double[4];
		double[] discard = new double[4];

		// Calculate expected mean value to play/discard each card
		for (int i = 0; i < hand.length; i++) {
			ArrayList<Card> belief = beliefStates(i, gb.deck);
			play[i] = simulatePlay(gb, belief);
			discard[i] = simulateDiscard(gb, belief);
		}

		// Calculate max value for giving a hint
		double maxHint = simulateGiveHint(gb);

		// Choose best card to play/discard from calculated values
		int cardPlay = -1;
		int cardDiscard = -1;
		double maxPlay = 0;
		double maxDiscard = 0;
		
		for (int i = 0; i < discard.length; i++) {
			if(discard[i] >  maxDiscard){
				maxDiscard = discard[i];
				cardDiscard = i;
			}
			if(play[i] > maxPlay){
				maxPlay = play[i];
				cardPlay = i;
			}
		}

		// Choose best action from the best expected values
		if(maxHint > maxDiscard && maxHint > maxPlay && gb.getHints() > 0){
			int[] hint = maxHints2(gb);
			//System.out.println("Gives hint: Player: " + hint[0] +" Type: " + hint[1] + " Value: " + hint[2]);
			this.giveHint(gb, hint[0], hint[1], hint[2]);
			//System.out.println("There are now " + gb.getHints() + " hints left");
		}
		else if (maxDiscard >= maxPlay){
			//System.out.println("Discard: " + hand[cardDiscard].toString());
			this.discard(gb, cardDiscard);
			//System.out.println("There are now " + gb.getHints() + " hints left");
		}
		else{
			//System.out.println("Plays: " + hand[cardPlay].toString());
			//System.out.println("Belief State was: " + beliefStates(cardPlay, gb.deck)); //FOR DEBUG
			this.playCard(gb, cardPlay);
		}
		
		
	}

//	// Returns the expected best hint to give
//	public int[] maxHints(GameBoard gb){
//
//		int[] number = new int[5];
//		int[] colours = new int[5];
//		int max = 0;
//		int type = 0;
//		int value = -1;
//		int Maxplayer = -1;
//		int player = -1;
//
//		// Choose the player who you can give the most information
//		// Calculated as number of 1's in their carInformation
//		for (Player p : gb.getPlayers()) {
//			player++;
//			if(this.equals(p)){
//				continue;
//			}
//			for (int i = 0; i < p.hand.length; i++) {
//				if(p.hand[i] == null)
//					continue;
//				//Tjek info om tal og opdater hvis nyt max fundet
//				if(p.cardInformation[i][1][p.hand[i].getNumber()-1] == 0){
//					number[p.hand[i].getNumber()-1] ++;
//				}
//				if(number[p.hand[i].getNumber()-1] > max){
//					max = number[p.hand[i].getNumber()-1];
//					type = 2;
//					value = p.hand[i].getNumber()-1;
//					Maxplayer = player;
//				}
//				//Tjek info om farve og opdater hvis nyt max fundet
//				colours[p.hand[i].getNumericalColour()] ++;
//				if(number[p.hand[i].getNumericalColour()] > max){
//					max = number[p.hand[i].getNumericalColour()];
//					type = 1;
//					value = p.hand[i].getNumericalColour();
//					Maxplayer = player;
//				}
//			}
//
//		}
//		int[] hint = {Maxplayer, type, value};
//		return hint;
//	}

	// Simulate all possible hints to give and return the hint that results in the greatest increase in totalInfo
	private int[] maxHints2(GameBoard gb) {
		int maxDifference = 0;
		int type = 0;
		int value = -1;
		int maxPlayer = -1;
		int player = -1;
		
		for (Player p :gb.getPlayers()) {
			player++;
			// Skip the current player
			if(this.equals(p)){
				continue;
			}
			// Simulate giving a hint about all colours and update max
			for (int i = 0; i < 5; i++) {
				GameBoard clone = gb.getClone();
				this.giveHint(clone, player, 1, i);
				int difference = clone.getPlayers().get(player).totalInfo - gb.getPlayers().get(player).totalInfo;
				if(difference > maxDifference) {
					maxDifference = difference;
					maxPlayer = player;
					value = i;
					type = 1;
				}
			}
			// Simulate giving hint about all numbers and update max
			for (int i = 0; i < 5; i++) {
				GameBoard clone = gb.getClone();
				this.giveHint(clone, player, 2, i);
				int difference = clone.getPlayers().get(player).totalInfo - gb.getPlayers().get(player).totalInfo;
				if(difference > maxDifference) {
					maxDifference = difference;
					maxPlayer = player;
					value = i;
					type = 2;
				}
			}
		}
		// Return the max hint value
		return new int[]{maxPlayer, type, value};
	}

	// Returns the mean expected value for playing a card from given list
	private double simulatePlay(GameBoard gb, ArrayList<Card> cards){
		int total = 0;
		for (Card card : cards) {
			GameBoard clone;
			clone = gb.getClone();
			clone.putCardOnTable(card);
			total = total + evalf(clone);

		}
		return (double) total/(double) cards.size();
	}

	// Returns the mean expected value for giving a hint
	private double simulateGiveHint(GameBoard gb){
		GameBoard clone = gb.getClone();
		int[] hint = maxHints2(gb);
		this.giveHint(clone, hint[0], hint[1], hint[2]);
		
		return evalf(clone);
	}

	// Returns the mean value for discarding a card from given list
	private double simulateDiscard(GameBoard gb, ArrayList<Card> cards){
		int total = 0;
		for (Card card : cards) {
			GameBoard clone;
			clone = gb.getClone();
			clone.discardCard(card);
			total = total + evalf(clone);
		}
		return (double) total/ (double) cards.size();
	}

	// Generates a list of cards which are not yet in play and matches the known information about given card
	private ArrayList<Card> beliefStates(int cardNumber, Deck deck){
		ArrayList<Card> belief = new ArrayList<>();
		Deck d;
		try {
			d = deck.clone();
		} catch (CloneNotSupportedException e) {
			d = null;
		}

		// Go through remaining cards in the deck and check if they fit the known information
		assert d != null;
		for (int i = 0; i < d.cardsLeftInDeck(); i++) {
			Card card = d.draw();
			if(cardInformation[cardNumber][1][card.getNumber()-1] == 0 && cardInformation[cardNumber][0][card.getNumericalColour()] == 0)
				// Add card to belief state
				belief.add(card);
		}

		// Add the cards in hand if they fit the known information
		for (int i = 0; i < 4; i++) {
			if(cardInformation[cardNumber][1][hand[i].getNumber()-1] == 0 && cardInformation[cardNumber][0][hand[i].getNumericalColour()] == 0)
				belief.add(hand[i]);
		}
		return belief;
	}

	// Go through the cards and compare to cards played, discarded and in other players hands
	private void autoUpdate(GameBoard gb) {
		
		for (int c = 0; c < 4; c++) {							//c is card position
			// Update number information based on information of colours
			boolean[] edit = {true, true, true, true, true};
			for (int i = 0; i < 5; i++) {						//i is colour
				if(cardInformation[c][0][i] == 1)
					continue;
				for (int j = 0; j < 5; j++) {					//j is the number (j+1) is the actual number
					if(cardInformation[c][1][j] == 1) {
						edit[j] = false;
						continue;
					}
					// Look at table and other players hand
					if (cardsLeft(gb, i, j)) {
						continue;
					}
					edit[j] = false;
				}
			}
			for (int j = 0; j < edit.length; j++) {
				if(edit[j]) {
					cardInformation[c][1][j] =1;
					totalInfo++;
				}
			}
			// Update colour info based on information about number
			boolean[] editColour = {true, true, true, true, true};
			for (int j = 0; j < 5; j++) {
				if(cardInformation[c][1][j] == 1)
					continue;
				for (int i = 0; i < 5; i++) {
					if(cardInformation[c][0][i] == 1) {
						editColour[i] = false;
						continue;
					}
					// Look at table and other players hand
					if (cardsLeft(gb, i, j)) {
						continue;
					}
					editColour[i] = false;
				}
			}
			for (int i = 0; i < 5; i++) {
				if(editColour[i]) {
					cardInformation[c][0][i] =1;
					totalInfo++;
				}
			}
		}
	}

	// Is there any change in the cards
	private boolean cardsLeft(GameBoard gb, int i, int j) {
		int left = gb.cardsNotDiscarded[i][j];
		if(gb.table[i] > j)
			left--;
		for (Player p : gb.getPlayers()) {
			// Skip current player
			if(this.equals(p)){
				continue;
			}
			// Go through their hand
			for (int k = 0; k < 4; k++) {
				if(p.hand[k] == null)
					continue;
				if(p.hand[k].getNumber() == (j+1) && p.hand[k].getNumericalColour() == i)
					left--;
			}
		}
		return left == 0;
	}

	// Evaluation function which gives a value of how favorable the state is
	private int evalf(GameBoard gb){
		int totalInfo = 0;
		for (Player p : gb.getPlayers()) {
			totalInfo = totalInfo + p.totalInfo;
		}
		int maxPoints = 0;
        for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {

				if(gb.cardsNotDiscarded[i][j] == 0) {
					maxPoints = maxPoints - (5-j);
					break;
				}		
			}
		}		
		return (gb.getPoints() * 10) + (gb.getLife() * 7) + (2*gb.getHints()) + (totalInfo) +(maxPoints);
	}
}
