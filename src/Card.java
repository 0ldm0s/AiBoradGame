
public class Card implements Cloneable{
	private int cardNumber;
	private String cardColour;		
	private int numericalColour;
	private String[] colours = {"R", "G", "B", "Y", "W"};
	
	public Card(int n, int c) {
		cardNumber = n;
		cardColour = colours[c];
		numericalColour = c;
	  }
	
	public String toString() {
        return cardColour + cardNumber;
    }

	public int getNumericalColour() {
		return numericalColour;
	}
	
	public String getColour() {
		return cardColour;
	}
	
	public int getNumber() {
		return cardNumber;
	}
	
	@Override
	public Card clone() throws CloneNotSupportedException{
		return (Card) super.clone();
	}

}
