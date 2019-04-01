
public class Player implements Cloneable{
	
	protected Card[] hand;
	public int[][][] cardInformation = new int[4][2][5];
	public int totalInfo = 0;

	
	public Player(Card[] cards){
		hand = cards;
	}
	
	public void discard(GameBoard gb, int card){
		System.out.println("Discard: " + hand[card].toString());
		gb.discardCard(hand[card]);
        hand[card] = gb.deck.draw();
		recalculateTotalInfo(card);
	}
	
	public void playCard(GameBoard gb, int card){
		System.out.println("Plays: " + hand[card].toString());
		gb.putCardOnTable(hand[card]);
		hand[card] = gb.deck.draw();
		recalculateTotalInfo(card);
	}

	private void recalculateTotalInfo(int card) {
		for (int i = 0; i < cardInformation[0][0].length; i++) {
			totalInfo = totalInfo - cardInformation[card][0][i];
			totalInfo = totalInfo - cardInformation[card][1][i];
		}
		cardInformation[card][0] = new int[5];
		cardInformation[card][1] = new int[5];
	}
	
	public void giveHint(GameBoard gb, int player, int type, int value){
		gb.useHintIfPossible(player, type, value);
	}
	
	@Override
	public String toString(){

		StringBuilder info = new StringBuilder("Player information: ");
		
		for (int i = 0; i < hand.length; i++) {
			info.append("\n Card ").append(i + 1).append(": Color: ");

			appendCardInformation(info, 0, i);

			info.append(" number: ");

			appendCardInformation(info, 1, i);
		}
		return info.toString();
	}

	// Append information of the at the given position
	private void appendCardInformation(StringBuilder info, int type, int cardPosition) {
		for (int j = 0; j < cardInformation[0][0].length; j++) {
			info.append(" ").append(cardInformation[cardPosition][type][j]);
		}
	}

	// Get information of cards in hand
	public String cardInfo(){
		StringBuilder info = new StringBuilder();
		for (Card card : hand) {
			if (card == null) {
				info.append("null ");
			} else {
				info.append(card.toString()).append(" ");
			}
		}
		return info.toString();
	}
	
	public void updateInfo(int type, int value){
		// Update information of colours if type is 1
		if(type == 1){
			// Go through cards in hand
			checkForMatch(type, value);
		}
		// Update information of numbers if type is 2
		else{
			checkForMatch(type, value);
		}
	}

	private void checkForMatch(int type, int value) {
		for (int i = 0; i < hand.length; i++) {
			if(hand[i] == null) {
				continue;
			}
			// If card has correct number then set all other positions to 1
			if((type == 1 && hand[i].getNumericalColour() == value) || (type==2 && hand[i].getNumber()-1 == value)){
				updateMatch(type, value, i);
			}
			// If card has different number then set this position to 1
			else{
				if(cardInformation[i][type-1][value] == 0) {
					// Increment total info if information is new
					totalInfo++;
				}
				cardInformation[i][type-1][value] = 1;
			}
		}
	}

	// Update the information when a number or colour matches
	private void updateMatch(int type, int value, int cardPosition) {
		for (int j = 0; j < cardInformation[0][0].length; j++) {
			if(j != value) {
				if(cardInformation[cardPosition][type-1][j] == 0) {
					// Increment total info if information is new
					totalInfo++;
				}
				cardInformation[cardPosition][type-1][j] = 1;
			}
		}
	}

	//Creates a deep copy of the Player object
	public Player getClone() {
		Card[] tempHand = this.hand;
		Player temp = new Player(tempHand);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; j++) {
				System.arraycopy(this.cardInformation[i][j], 0, temp.cardInformation[i][j], 0, 5);
			}
		}
		temp.totalInfo = this.totalInfo;
		return temp;
	}
	
}
