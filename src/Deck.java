import java.util.Random;

public class Deck {
	private Card[] deckOfCards;
	private int cardPointer;
	
	public Deck() {                                      
		deckOfCards = new Card[50];  
		cardPointer= 0;
		int j = 0;
		for(int i=0; i <= 4; i++) {
			// Add three ones
			deckOfCards[j] = new Card(1, i);
			deckOfCards[j+1] = new Card(1, i);
			deckOfCards[j+2] = new Card(1, i);
			j=j+3;
			// Add two twos, threes and fours
			for(int k=2; k<=4; k++) {
				deckOfCards[j] = new Card(k, i);
				deckOfCards[j+1] = new Card(k, i);
				j= j+2;
			}
			// Add one five
			deckOfCards[j] = new Card(5, i);
			j++;
		}
		this.shuffleDeck();
	  }
	
	public Card draw() {																	//Should the test be here??
		if ( cardPointer < deckOfCards.length )
	   	 {
	   	    return ( deckOfCards[ cardPointer++ ] ); //Updates pointer after return
	   	 }
	   	 else
	   	 {
	   	    System.out.println("Out of cards error"); //No more cards in the deck
	   	    return ( null );  
	   	 }
	}
	
	public int cardsLeftInDeck() {
		return deckOfCards.length - cardPointer;
	}
	
	public void shuffleDeck()
	{
																							/* ====================================   
																					        Q: Is this shuffle good enough?                                             
																					        (I Just Googled java array shuffle) 
																					        ==================================== */
	    int index;
	    Card temp;
	    Random random = new Random();
	    for (int i = deckOfCards.length - 1; i > 0; i--)
	    {
	        index = random.nextInt(i + 1);
	        temp = deckOfCards[index];
	        deckOfCards[index] = deckOfCards[i];
	        deckOfCards[i] = temp;
	    }
	}
	
	public String toString()
	   {
		//Print Deck 10 cards at a time                                             
	      String s = "";
	      int k= 0;
	      for ( int i = 0; i < 5; i++ ) 			// 5 rows of 10 cards
	      {
	         for ( int j = 1; j <= 10; j++ )		
	             s += ( deckOfCards[k++] + " " );                

	         s += "\n";   							// Add NEWLINE after 10 cards
	      }
	      return ( s );
	   }
	
	
}
