
public class Player implements Cloneable{
	
	protected Card[] hand = new Card[4];
	public int[][][] cardInformation = new int[4][2][5];

	
	public Player(Card[] cards){
		hand = cards;
	}
	
	public void discard(GameBoard gb, int card){
		hand[card] = gb.discardCard(hand[card]);
		cardInformation[card][0] = new int[5];
		cardInformation[card][1] = new int[5];
	}
	
	public void playCard(GameBoard gb, int card){
		hand[card] = gb.putCardOnTable(hand[card]);
		cardInformation[card][0] = new int[5];
		cardInformation[card][1] = new int[5];
	}
	
	public void giveHint(GameBoard gb, int player, int type, int value){
		gb.useHintIfPossible(player, type, value);
	}
	
	@Override
	public String toString(){

		String info = "Player information: "; 
		
		for (int i = 0; i < hand.length; i++) {
			info = info + "\n Card " + (i+1) + ": Color: " ;
			
			for (int j = 0; j < cardInformation[0][0].length; j++) {
				info = info + " " + cardInformation[i][0][j];
			}
			info = info + " number: ";
			
			for (int j = 0; j < cardInformation[0][0].length; j++) {
				info = info + " " + cardInformation[i][1][j];
			}
			
		}
		return info;
	}
	
	public String cardInfo(){
		String info = "";
		for (int i = 0; i < hand.length; i++) {
			info = info + hand[i].toString() + "\n";
		}
		return info;
	}
	
	public void updateInfo(int type, int value){
		if(type == 1){
			for (int i = 0; i < hand.length; i++) {
				if(hand[i].getNumericalColour() == value){
					for (int j = 0; j < cardInformation[0][0].length; j++) {
						if(j != value)
						cardInformation[i][type-1][j] = 1;
					}
				}
				else{
					cardInformation[i][type-1][value] = 1;
				}
			}
		}
		else{
			for (int i = 0; i < hand.length; i++) {
				if(hand[i].getNumber() == value){
					for (int j = 0; j < cardInformation[0][0].length; j++) {
						if(j != value)
						cardInformation[i][type-1][j] = 1;
					}
				}
				else{
					cardInformation[i][type-1][value-1] = 1;
				}
			}
		}
	}

	@Override
	public Player clone() throws CloneNotSupportedException{
		return (Player) super.clone();
	}
	
}
