
public class Player implements Cloneable{
	
	protected Card[] hand = new Card[4];
	public int[][][] cardInformation = new int[4][2][5];
	public int totalInfo = 0;

	
	public Player(Card[] cards){
		hand = cards;
	}
	
	public void discard(GameBoard gb, int card){
		hand[card] = gb.discardCard(hand[card]);
		for (int i = 0; i < cardInformation[0][0].length; i++) {
			totalInfo = totalInfo - cardInformation[card][0][i];
			totalInfo = totalInfo - cardInformation[card][1][i];
		}
		cardInformation[card][0] = new int[5];
		cardInformation[card][1] = new int[5];
	}
	
	public void playCard(GameBoard gb, int card){
		hand[card] = gb.putCardOnTable(hand[card]);
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
			if(hand[i] == null){
				info = info + "null";
			}
			else{
				info = info + hand[i].toString() + " ";
			}
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
						totalInfo++;
					}
				}
				else{
					cardInformation[i][type-1][value] = 1;
					totalInfo++;
				}
			}
		}
		else{
			for (int i = 0; i < hand.length; i++) {
				if(hand[i].getNumber()-1 == value){
					for (int j = 0; j < cardInformation[0][0].length; j++) {
						if(j != value)
						cardInformation[i][type-1][j] = 1;
						totalInfo++;
					}
				}
				else{
					cardInformation[i][type-1][value] = 1;
					totalInfo++;
				}
			}
		}
	}

	//Laver en deep clone af player-objektet.
	@Override
	public Player clone() throws CloneNotSupportedException{
		Card[] tempHand = this.hand;
		Player temp = new Player(tempHand);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; j++) {
				for (int j2 = 0; j2 < 5; j2++) {
					temp.cardInformation[i][j][j2] = this.cardInformation[i][j][j2];
				}
				
			}	
		}
		temp.totalInfo = this.totalInfo;
		return temp;
	}
	
}
