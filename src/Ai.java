import java.util.ArrayList;

public class Ai extends Player{

	public Ai(Card[] cards){
		super(cards);
	}
	
	//max max - start regler
	
	//Givet spillebrættet tages en beslutning og den givne handling udføres.
	public void takeAction(GameBoard gb){
		double[] play = new double[4];
		double[] discard = new double[4];
		//Udregn forventede gennemsnitlige værdi for at spille/kassere kort for hver kort position. Gem som 2x4 tal i arrays.
		for (int i = 0; i < hand.length; i++) {
			ArrayList<Card> belief = beliefStates(i, gb.deck);
			play[i] = simulatePlay(gb, belief);
			discard[i] = simulateDiscard(gb, belief);
		}
		
		//Udregn forventede værdi for at give et hint
		double maxHint = simulateGiveHint(gb); // skal rettes til bedre evalf
		
		//Sammenlign de udregnede tal og vælg bedste kandidat at spille/kassere.
		int card = -1;							//Kort position
		double maxPlay = 0;						//Bedste forventede værdi for at spille et kort
		double maxDiscard = 0;					//Bedste forventede værdi for at kassere et kort
		
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
		
		//System.out.println("Max hint: " + maxHint + " Max play: " + maxPlay + " Max discard: " + maxDiscard); //FOR DEBUG
		
		//Sammenlign de bedste forventede værdier for at spille/kassere/hinte og vælg den bedste handling.
		if(maxHint > maxDiscard && maxHint > maxPlay){
			int[] hint = maxHints2(gb);
			System.out.println("Gives hint: Player: " + hint[0] +" Type: " + hint[1] + " Value: " + hint[2]);
			this.giveHint(gb, hint[0], hint[1], hint[2]);
		}
		else if (maxDiscard >= maxPlay){
			System.out.println("Discard: " + hand[card].toString());
			this.discard(gb, card);
		}
		else{
			System.out.println("Plays: " + hand[card].toString());
			System.out.println("Belief State was: " + beliefStates(card, gb.deck)); //FOR DEBUG
			this.playCard(gb, card);
		}
		
		
	}
	
	//Løb hånd igennem - retunerer det bedste hint. 
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
			//For hver anden spiller - vælg den spiller hvor du kan give mest information på én gang.
			//Her talt som antallet af 1-taller i deres cardInformation array.
			//Tager ikke højde for om de allerede ved det du siger. (Endnu!) <----------- DO THIS
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
	
	//Nyt forsøg på at finde bedste hint
	//Simuler alle mulige hint og gem det der resulterer i den største stigning i totalInfo.
	public int[] maxHints2(GameBoard gb) {
		int maxDifference = 0;
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
			//Simuler at give hint omkring alle fem tal. Opdater hvis nyt max fundet.
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
		//Retuner det fundne max
		int[] hint = {maxPlayer, type, value};
		return hint;
	}
	
	//Returnerer den gennemsnitlige forventede værdi for at spille et kort i listen card på spillebrættet gb.
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
	
	//Returnerer den gennemsnitlige forventede værdi for at give et hint. 
	public double simulateGiveHint(GameBoard gb){
		GameBoard clone = gb.getClone();
		//int[] hint = maxHints(gb);
		int[] hint = maxHints2(gb);
		//System.out.println("Gives hint: Player: " + hint[0] +" Type: " + hint[1] + "Value: " + hint[2]);
		this.giveHint(clone, hint[0], hint[1], hint[2]);
		
		return evalf(clone);
	}
	
	//Returnerer den gennemsnitlige forventede værdi for at kassere et kort i listen card på spillebrættet gb.
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
	
	//Generere en liste af kort som endnu ikke er i spil og som passer på den information man har om kortet på position cardNumber.
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
		//løb igennem kortene der er tilbage i decket og se om de passer på informationen. Tilføj dem til listen hvis de gør.
		for (int i = 0; i < cardsLeft; i++) {
			Card card = d.draw();
			if(cardInformation[cardNumber][1][card.getNumber()-1] == 0 && cardInformation[cardNumber][0][card.getNumericalColour()] == 0)
				belief.add(card);
		}
		//Tilføj de fire kort vi har på hånden hvis de passer på informationen. 
		//[OBS!: semi-snyd da A.I. ikke burde have adgang til sine egne kort - men det er information der kunne genereres alligevel.]
		for (int i = 0; i < 4; i++) {
			if(cardInformation[cardNumber][1][hand[i].getNumber()-1] == 0 && cardInformation[cardNumber][0][hand[i].getNumericalColour()] == 0)
				belief.add(hand[i]);
		}
		
		//System.out.println("Belief state: " + belief.size());  //FOR DEBUG
		return belief;
	}
	//Evalueringsfunktion der giver spillebrættet en værdi som er vores bud på hvor favorabel denne status er.
	public int evalf(GameBoard gb){
		int totalInfo = 0;
		for (Player p : gb.getPlayers()) {
		totalInfo = totalInfo + p.totalInfo;
		}
		return (gb.getPoints() * 10) + (gb.getLife() * 4) + (5*gb.getHints()) + totalInfo;
	}
	
}
