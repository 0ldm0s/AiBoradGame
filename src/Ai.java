import java.util.ArrayList;

public class Ai extends Player{

	public Ai(Card[] cards){
		super(cards);
	}
	
	//max max - start regler
	

	public void takeAction(GameBoard gb){
		double[] play = new double[4];
		double[] discard = new double[4];
		for (int i = 0; i < hand.length; i++) {
			ArrayList<Card> belief = beliefStates(i, gb.deck);
			play[i] = simulatePlay(gb, belief);
			discard[i] = simulateDiscard(gb, belief);
		}
		
		double maxHint = simulateGiveHint(gb); // skal rettes til bedre evalf
		
		int card = -1;
		double maxPlay = 0;
		double maxDiscard = 0;
		
		for (int i = 0; i < discard.length; i++) {
			if(discard[i] > maxDiscard){
				maxDiscard = discard[i];
				card = i;
			}
			if(play[i] > maxPlay){
				maxPlay = play[i];
				card = i;
			}
		}
		
		//System.out.println("Max hint: " + maxHint + " Max play: " + maxPlay + " Max discard: " + maxDiscard);
		
		if(maxHint > maxDiscard && maxHint > maxPlay){
			
			int[] hint = maxHints(gb);
			System.out.println("Gives hint: Player: " + hint[0] +" Type: " + hint[1] + "Value: " + hint[2]);
			this.giveHint(gb, hint[0], hint[1], hint[2]);
		}
		else if (maxDiscard >= maxPlay){
			System.out.println("Discard: " + hand[card].toString());
			this.discard(gb, card);
		}
		else{
			System.out.println("Plays: " + hand[card].toString());
			this.playCard(gb, card);
		}
		
		
	}
	
	//Løb hånd igennem - find bedste hint
	public int[] maxHints(GameBoard gb){

		int[] number = new int[5];
		int[] colours = new int[5];
		int max = 0;
		int type = 0;
		int value = -1;
		int player = -1;
		
		for (Player p : gb.getPlayers()) {
			
			if(this.equals(p)){
				continue;
			}
			//andre spillere
			for (int i = 0; i < p.hand.length; i++) {
				if(p.hand[i] == null)
					continue;
				number[p.hand[i].getNumber()-1] ++;
				if(number[p.hand[i].getNumber()-1] > max){
					max = number[p.hand[i].getNumber()-1];
					type = 2;
					value = p.hand[i].getNumber()-1;
					player = i;
				}
				colours[p.hand[i].getNumericalColour()] ++;
				if(number[p.hand[i].getNumericalColour()] > max){
					max = number[p.hand[i].getNumericalColour()];
					type = 1;
					value = p.hand[i].getNumericalColour();
					player = i;
				}
			}
			
		}
		int[] hint = {player, type, value};
		return hint;
	}
	
	public double simulatePlay(GameBoard gb, ArrayList<Card> card){
		int total = 0;
		for (int i = 0; i < card.size(); i++) {
			GameBoard clone;
			clone = gb.getClone();
			clone.putCardOnTable(card.get(i));
			//System.out.println(evalf(clone));
			total = total + evalf(clone);

		}
		//System.out.println("Total play: "+ total);
		return (double) total/(double) card.size();
	}
	
	public double simulateGiveHint(GameBoard gb){
		GameBoard clone = gb.getClone();
		int[] hint = maxHints(gb);
		//System.out.println("Gives hint: Player: " + hint[0] +" Type: " + hint[1] + "Value: " + hint[2]);
		this.giveHint(clone, hint[0], hint[1], hint[2]);
		
		return evalf(clone);
	}
	
	public double simulateDiscard(GameBoard gb, ArrayList<Card> card){
		int total = 0;
		for (int i = 0; i < card.size(); i++) {
			GameBoard clone;
			clone = gb.getClone();
			clone.discardCard(card.get(i));
			//System.out.println(evalf(clone));
			total = total + evalf(clone);

		}
		//System.out.println("Total discard: " + total);
		return (double) total/ (double) card.size();
	}
	
	public ArrayList<Card> beliefStates (int cardNumber, Deck deck){
		ArrayList<Card> belief = new ArrayList<>();
		Deck d;
		try {
			d = deck.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			d = null;
		}
		int cardsLeft = d.cardsLeftInDeck();
		for (int i = 0; i < cardsLeft; i++) {
			Card card = d.draw();
			if(cardInformation[cardNumber][0][card.getNumber()-1] == 0 && cardInformation[cardNumber][1][card.getNumericalColour()] == 0)
				belief.add(card);
		}
		
		for (int i = 0; i < 4; i++) {
			if(cardInformation[cardNumber][0][hand[i].getNumber()-1] == 0 && cardInformation[cardNumber][1][hand[i].getNumericalColour()] == 0)
				belief.add(hand[i]);
		}
		
		System.out.println("Belief state: " + belief.size());
		return belief;
	}
	
	public int evalf(GameBoard gb){
		int totalInfo = 0;
		for (Player p : gb.getPlayers()) {
		totalInfo = totalInfo + p.totalInfo;
		}
		return (gb.getPoints() * 4) + (gb.getLife() * 2) + gb.getHints() + totalInfo;
	}
	
}
