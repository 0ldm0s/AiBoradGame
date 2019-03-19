
public class Card implements Cloneable{
	private int cardNumber;												//Hvad er bedst? Public Vs private + get-funktioner
	private String cardColour;		
	private int numericalColour;
	private String[] colours = {"R", "G", "B", "Y", "W"};				//Ens for alle! Så den skal vel ikke være private?
	
	public Card(int n, int c) {
		cardNumber = n;
		cardColour = colours[c];
		numericalColour = c;
	  }
	
	public String toString() {
        return cardColour + cardNumber;
    }
	
	//String or int most useful?
	public int getNumericalColour() {
		return numericalColour;
	}
	
	public String getColour() {
		return cardColour;
	}
	
	@Override
	public Card clone() throws CloneNotSupportedException{
		return (Card) super.clone();
	}

}
