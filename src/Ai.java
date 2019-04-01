import java.util.ArrayList;

public class Ai extends Player{

	private int playerID;
	public boolean original;
	public Ai(Card[] cards, int ID){
		super(cards);
		playerID = ID;
		original = true;
	}

	// Given a GameBoard the ai makes a decision and executes it
	public void takeAction(GameBoard gb, Player p){
		int[] hint = null;
		// Update known information
        if(original)
            autoUpdate(gb);
		double[] play = new double[4];
		double[] discard = new double[4];

		// Calculate expected mean value to play/discard each card
		for (int i = 0; i < hand.length; i++) {
			ArrayList<Card> belief = beliefStates(i, gb.deck, p);
			play[i] = simulatePlay(gb, belief);
			discard[i] = simulateDiscard(gb, belief);
		}
        // Calculate max value for giving a hint
		double maxHint = -1;
		if(original){
			hint = simulateGiveHint(gb);
			maxHint = hint[0];
		}
        // Choose best card to play/discard from calculated values
		int cardPlay = -1;
		int cardDiscard = -1;																		//FIXED MISTAKE
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
		if (maxDiscard >= maxPlay && maxDiscard > maxHint && gb.getHints() < 8){
			if(original){
				this.discard(gb, cardDiscard);
			}
			else {
					gb.discardCard(hand[cardDiscard]);
			}
		}
		else if(maxHint > maxPlay && gb.getHints() > 0){
			String[] colours = {"R", "G", "B", "Y", "W"};
			if(hint[2] == 1)
				System.out.println("Gives hint: Player: " + hint[1] +" Type: color Value: " + colours[hint[3]]);
			else
				System.out.println("Gives hint: Player: " + hint[1] +" Type: number Value: " + (hint[3]+1));
			this.giveHint(gb, hint[1], hint[2], hint[3]);
		}
		else{
			if(original) {
				this.playCard(gb, cardPlay);
			}
			else
				gb.putCardOnTable(hand[cardPlay]);
		}
	}

	public int[] maxHints(GameBoard gb){

		int[] number = new int[5];
		int[] colours = new int[5];
		int max = 0;
		int type = 0;
		int value = -1;
		int Maxplayer = -1;
		int player = -1;

		for (Player p : gb.getPlayers()) {
			player++;
			if(this.equals(p)){
				continue;
			}
			//For hver anden spiller - vælg den spiller hvor du kan give mest information på gang.
			//Her talt som antallet af 1-taller i deres cardInformation array.
			//Tager ikke højde for om de allerede ved det du siger.
			for (int i = 0; i < p.hand.length; i++) {
				if(p.hand[i] == null)
					continue;
				//Tjek info om tal og opdater hvis nyt max fundet
				if(p.cardInformation[i][1][p.hand[i].getNumber()-1] == 0){
					number[p.hand[i].getNumber()-1] ++;
				}
				if(number[p.hand[i].getNumber()-1] > max){
					max = number[p.hand[i].getNumber()-1];
					type = 2;
					value = p.hand[i].getNumber()-1;
					Maxplayer = player;
				}
				//Tjek info om farve og opdater hvis nyt max fundet
				colours[p.hand[i].getNumericalColour()] ++;
				if(number[p.hand[i].getNumericalColour()] > max){
					max = number[p.hand[i].getNumericalColour()];
					type = 1;
					value = p.hand[i].getNumericalColour();
					Maxplayer = player;
				}
			}

		}
		int[] hint = {Maxplayer, type, value};
		return hint;
	}

    // Simulate all possible hints to give and return the hint that results in the greatest increase in totalInfo
	public int[] maxHints2(GameBoard gb) {
		int maxDifference = -1;
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
			//Simuler at give hint omkring alle fem tal. Opdater hvis nyt max fundet.
			for (int i = 4; i > -1; i--) {
				GameBoard clone = gb.getClone();
				this.giveHint(clone, player, 2, i);
				int difference = clone.getPlayers().get(player).totalInfo - gb.getPlayers().get(player).totalInfo;
				if(difference > maxDifference || (difference == maxDifference && i < value)) {
					maxDifference = difference;
					maxPlayer = player;
					value = i;
					type = 2;
				}
			}
			//Simuler at give hint omkring alle fem farver. Opdater hvis nyt max fundet.
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
		}
		//Retuner det fundne max
		int[] hint = {maxPlayer, type, value};
		return hint;
	}
	public int[] maxHints3(GameBoard gb) {
		int maxEvalf = 0;
		int type = 0;
		int value = -1;
		int maxPlayer = -1;
		int player = -1;

		for (Player p :gb.getPlayers()) {
			player++;
			//Spring over mig selv
			if(this.equals(p)){
				continue;
			}
			// Simulate giving hint about all numbers and update max
			for (int i = 0; i < 5; i++) {
				GameBoard clone = gb.getClone();
				this.giveHint(clone, player, 2, i);
				predict(clone);
				if(evalf(clone) > maxEvalf ) {
					maxEvalf = evalf(clone);
					maxPlayer = player;
					value = i;
					type = 2;
				}
			}

			//Simulate giving hint about all colours and update max
			for (int i = 0; i < 5; i++) {
				GameBoard clone = gb.getClone();
				this.giveHint(clone, player, 1, i);
				predict(clone);
				if(evalf(clone) > maxEvalf) {
					maxEvalf = evalf(clone);
					maxPlayer = player;
					value =i;
					type = 1;
				}
			}
		}
		// Return the max hint value
		return new int[]{maxPlayer, type, value};
	}
    // Returns the mean expected value for playing a card from given list
	public double simulatePlay(GameBoard gb, ArrayList<Card> cards){
		int total = 0;
		for (Card card : cards) {
			GameBoard clone;
			clone = gb.getClone();
			clone.putCardOnTable(card);
			total = total + evalf(clone);

		}
		if(cards.size() != 0)
			return (double) total/(double) cards.size();
		return 0;
	}

    // Returns the mean expected value for giving a hint
	public int[] simulateGiveHint(GameBoard gb){
		if(gb.getHints() == 0) {
			int[] nope = {0,0,0,0};
			return nope;
		}
		GameBoard clone = gb.getClone();

		int[] hint = maxHints3(gb);
		giveHint(clone, hint[0], hint[1], hint[2]);
		int ev = evalf(clone);
		int[] temp = {ev, hint[0], hint[1], hint[2]};
		return temp;
	}

    // Returns the mean value for discarding a card from given list
	public double simulateDiscard(GameBoard gb, ArrayList<Card> cards){
		int total = 0;
		for (Card card : cards) {
			GameBoard clone;
			clone = gb.getClone();
			clone.discardCard(card);
			total = total + evalf(clone);
		}
		if(cards.size() != 0)
			return (double) total/ (double) cards.size();
		return 0;
	}

    // Generates a list of cards which are not yet in play and matches the known information about given card
	public ArrayList<Card> beliefStates (int cardNumber, Deck deck, Player p){
		ArrayList<Card> belief = new ArrayList<>();
		if(hand[cardNumber] == null)
			return belief;
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
			if(hand[i] == null)
				continue;
			if(cardInformation[cardNumber][1][hand[i].getNumber()-1] == 0 && cardInformation[cardNumber][0][hand[i].getNumericalColour()] == 0)
				belief.add(hand[i]);
		}

		// If used on a dummy then add original player's hand
		if(!original) {
			for (int i = 0; i < 4; i++) {
				if(p.hand[i] == null)
					continue;
				if(cardInformation[cardNumber][1][p.hand[i].getNumber()-1] == 0 && cardInformation[cardNumber][0][p.hand[i].getNumericalColour()] == 0)
					belief.add(p.hand[i]);
			}
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

	public void predict(GameBoard gb) {
        // Contains clones of other players as Ai's.
		Ai[] dummies = new Ai[3];
		for (int i = 1; i < 4; i++) {
			dummies[i-1] = createDummy(gb.getPlayers().get((playerID + i)%4), (playerID + i)%4);
		}
		for (Ai dummy: dummies) {
			dummy.takeAction(gb, this);
			if(gb.getLife() == 0)
				break;
		}
	}

	public Ai createDummy(Player p, int ID) {
		Player temp=null;
        temp = p.getClone();
		Ai dummy = new Ai(temp.hand, ID);
		dummy.cardInformation = temp.cardInformation;
		dummy.totalInfo = temp.totalInfo;
		dummy.original = false;
		return dummy;

	}


    // Evaluation function which gives a value of how favorable the state is
	public int evalf(GameBoard gb){
		int totalInfo = 0;
		for (Player p : gb.getPlayers()) {
		totalInfo = totalInfo + p.totalInfo;
		}
		int maxPoints=0;												//<---- Added this (H)
        for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {

				if(gb.cardsNotDiscarded[i][j] == 0) {
					maxPoints = maxPoints - (5-j);
					break;
				}		
			}
		}
		return (gb.getPoints() * GameFlow.pointMulti) + (GameFlow.lifeMulti*gb.getLife()) + (GameFlow.hintMulti*gb.getHints()) + (GameFlow.infoMulti*totalInfo) +(GameFlow.maxPointMulti*maxPoints) + 1;
	}
	
}
