
public class Player implements Cloneable{
	
	protected Card[] hand = new Card[4];
	public int[][][] cardInformation = new int[4][2][5];

	
	public Player(Card[] cards){
		hand = cards;
	}
	
	public void discard(GameBoard gb, int card){
		hand[card] = gb.discardCard(hand[card]);
	}
	
	public void playCard(GameBoard gb, int card){
		hand[card] = gb.putCardOnTable(hand[card]);
	}
	
	public void giveHint(GameBoard gb, int player, int type, int value){
		gb.useHintIfPossible();
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

	@Override
	public Player clone() throws CloneNotSupportedException{
		return (Player) super.clone();
	}
	
}
