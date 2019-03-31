import java.util.ArrayList;

public class Ai extends Player{

	private int playerID;
	public boolean original;
	public Ai(Card[] cards, int ID){
		super(cards);
		playerID = ID;
		original = true;
	}

	//max max - start regler

	//Givet spillebrættet tages en beslutning og den givne handling udføres.
	public void takeAction(GameBoard gb, Player p){
		int temp = totalInfo;
		//String temp2 = this.toString();
		if(original)
			autoUpdate(gb);
		if(totalInfo != temp) {
			//System.out.println("Auto update did something " + (totalInfo - temp) + " bits of informationen added");
			//			System.out.println("Changed from ");
			//			System.out.println(temp2);
			//			gb.printStatus(this);
		}
		double[] play = new double[4];
		double[] discard = new double[4];
		//Udregn forventede gennemsnitlige værdi for at spille/kassere kort for hver kort position. Gem som 2x4 tal i arrays.
		for (int i = 0; i < hand.length; i++) {
			ArrayList<Card> belief = beliefStates(i, gb.deck, p);
			play[i] = simulatePlay(gb, belief);
			discard[i] = simulateDiscard(gb, belief);
		}
		double maxHint = -1;
		//Udregn forventede værdi for at give et hint
		if(original)
			maxHint = simulateGiveHint(gb); // skal rettes til bedre evalf

		//Sammenlign de udregnede tal og vælg bedste kandidat at spille/kassere.
		int cardPlay = -1;							//Kort position
		int cardDiscard = -1;																		//FIXED MISTAKE
		double maxPlay = 0;						//Bedste forventede værdi for at spille et kort
		double maxDiscard = 0;					//Bedste forventede værdi for at kassere et kort

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
		//System.out.println("Discard:" + discard[0] + discard[1] + discard[2] + discard[3]);
		//System.out.println("Max hint: " + maxHint + " Max play: " + maxPlay + " Max discard: " + maxDiscard); //FOR DEBUG

		//Sammenlign de bedste forventede værdier for at spille/kassere/hinte og vælg den bedste handling.
		if(maxHint > maxDiscard && maxHint > maxPlay && gb.getHints() > 0){
			int[] hint = maxHints2(gb);
			System.out.println("Gives hint: Player: " + hint[0] +" Type: " + hint[1] + " Value: " + hint[2]);
			this.giveHint(gb, hint[0], hint[1], hint[2]);
			//System.out.println("There are now " + gb.getHints() + " hints left");
		}
		else if (maxDiscard >= maxPlay){
			System.out.println("Discard: " + hand[cardDiscard].toString());
			this.discard(gb, cardDiscard);
			//System.out.println("There are now " + gb.getHints() + " hints left");
		}
		else{
			System.out.println("Plays: " + hand[cardPlay].toString());
			System.out.println("Belief State was: " + beliefStates(cardPlay, gb.deck, p)); //FOR DEBUG
			//System.out.println("Max hint: " + maxHint + " Max play: " + maxPlay + " Max discard: " + maxDiscard);
			//System.out.println(play[0] + "   " + play[1] + "   " +play[2] + "   "+ play[3]);		
			this.playCard(gb, cardPlay);
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
			//Simuler at give hint omkring alle fem tal. Opdater hvis nyt max fundet.
			for (int i = 4; i > -1; i--) {
				GameBoard clone = gb.getClone();
				this.giveHint(clone, player, 2, i);
				int difference = clone.getPlayers().get(player).totalInfo - gb.getPlayers().get(player).totalInfo;
				if(difference > maxDifference || (difference == maxDifference && i < value)) {

					//System.out.println(p.cardInfo());
					//System.out.println(maxDifference + ", " + difference + ", " + i);
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
			//Simuler at give hint omkring alle fem tal. Opdater hvis nyt max fundet.
			for (int i = 0; i < 5; i++) {
				GameBoard clone = gb.getClone();
				this.giveHint(clone, player, 2, i);
				predict(clone);
				if(evalf(clone) > maxEvalf ) {
					maxPlayer = player;
					value = i;
					type = 2;
				}
			}

			//Simuler at give hint omkring alle fem farver. Opdater hvis nyt max fundet.
			for (int i = 0; i < 5; i++) {
				GameBoard clone = gb.getClone();
				this.giveHint(clone, player, 1, i);
				predict(clone);
				if(evalf(clone) > maxEvalf) {
					maxPlayer = player;
					value =i;
					type = 1;
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
		int[] hint = maxHints3(gb);
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
	public ArrayList<Card> beliefStates (int cardNumber, Deck deck, Player p){
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
		//Hvis brugt på en dummy tilfæj original spillerens hånd også
		if(!original) {
			for (int i = 0; i < 4; i++) {
				if(cardInformation[cardNumber][1][p.hand[i].getNumber()-1] == 0 && cardInformation[cardNumber][0][p.hand[i].getNumericalColour()] == 0)
					belief.add(p.hand[i]);
			}
		}
		//System.out.println("Belief state: " + belief.size());  //FOR DEBUG
		System.out.println(hand[cardNumber].toString() + belief);
		return belief;
	}

	//Løb kortene igennem og sammenhold med kort på bordet (spillede/kasserede) og kort på de andres hænder
	//Eksempel: Er den røde femmer blevet kasseret, og du ved dit kort er rødt - så kan det ikke være en femmer.
	public void autoUpdate(GameBoard gb) {	

		for (int c = 0; c < 4; c++) {							//c er kortpositionen
			//Opdater talinfo baseret på info om farve
			boolean[] edit = {true, true, true, true, true};
			for (int i = 0; i < 5; i++) {						//i er farven
				if(cardInformation[c][0][i] == 1)
					continue;
				for (int j = 0; j < 5; j++) {					//j er tallet (j+1) er det faktiske tal
					if(cardInformation[c][1][j] == 1) {
						edit[j] = false;
						continue;
					}			
					//Kig på bordet og de andre spilleres hånd
					int left = gb.cardsNotDiscarded[i][j];
					if(gb.table[i] > j)
						left--;
					for (Player p : gb.getPlayers()) {
						//Spring over mig selv
						if(this.equals(p)){
							continue;
						}
						//Løb igennem deres hånd
						for (int k = 0; k < 4; k++) {
							if(p.hand[k] == null)
								continue;
							if(p.hand[k].getNumber() == (j+1) && p.hand[k].getNumericalColour() == i)
								left--;
						}
					}
					if(left == 0)
						continue;
					edit[j] = false;
				}
			}
			for (int j = 0; j < edit.length; j++) {
				if(edit[j]) {
					cardInformation[c][1][j] =1;
					totalInfo++;
				}
			}
			//Opdater farveinfo baseret på info om tal
			boolean[] editColour = {true, true, true, true, true};
			for (int j = 0; j < 5; j++) {
				if(cardInformation[c][1][j] == 1)
					continue;
				for (int i = 0; i < 5; i++) {
					if(cardInformation[c][0][i] == 1) {
						editColour[i] = false;
						continue;
					}
					//Kig på bordet og de andre spilleres hånd
					int left = gb.cardsNotDiscarded[i][j];
					if(gb.table[i] > j)
						left--;
					for (Player p : gb.getPlayers()) {
						//Spring over mig selv
						if(this.equals(p)){
							continue;
						}
						//Løb igennem deres hånd
						for (int k = 0; k < 4; k++) {
							if(p.hand[k] == null)
								continue;
							if(p.hand[k].getNumber() == (j+1) && p.hand[k].getNumericalColour() == i)
								left--;
						}
					}
					if(left == 0)
						continue;
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

	public void predict(GameBoard gb) {
		Ai[] dummies = new Ai[3]; //Indeholder cloner af de andre spillere som Ais. Placering 0 er næste spiller.
		for (int i = 1; i < 4; i++) {
			dummies[i-1] = createDummy(gb.getPlayers().get((playerID + i)%4), (playerID + i)%4);
		}
		for (Ai dummy: dummies) {
			dummy.takeAction(gb, this);
		}
	}

	public Ai createDummy(Player p, int ID) {
		Player temp=null;
		try {
			temp = p.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Ai dummy = new Ai(temp.hand, ID);
		dummy.cardInformation = temp.cardInformation;
		dummy.totalInfo = temp.totalInfo;
		dummy.original = false;
		return dummy;

	}


	//Evalueringsfunktion der giver spillebrættet en værdi som er vores bud på hvor favorabel denne status er.
	public int evalf(GameBoard gb){
		int totalInfo = 0;
		for (Player p : gb.getPlayers()) {
			totalInfo = totalInfo + p.totalInfo;
		}
		int maxPoints=0;												//<---- Added this (H)
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				//System.out.println("There are " + gb.cardsNotDiscarded[i][j] + " of colour " + i  + " and number " + (j+1));
				if(gb.cardsNotDiscarded[i][j] == 0) {
					maxPoints = maxPoints - (5-j);
					break;
				}		
			}
		}		
		return (gb.getPoints() * 10) + (gb.getLife() * 7) + (2*gb.getHints()) + (1*totalInfo) +(1*maxPoints);
	}

}
