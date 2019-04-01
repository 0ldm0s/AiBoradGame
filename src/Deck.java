import java.util.Random;

public class Deck implements Cloneable{

	private Card[] deckOfCards;
	private int cardPointer;
	private int initPointer;
	
	public Deck() {

		// Creating a new deck of cards
		deckOfCards = new Card[50];  
		cardPointer= 0;
		initPointer = 0;

		for(int i = 0; i <= 4; i++) {
			// Add three ones
			addCard(1, i);
			addCard(1, i);
			addCard(1, i);

			// Add two twos, threes and fours
			for(int k=2; k<=4; k++) {
				addCard(k, i);
				addCard(k, i);
			}
			// Add one five
			addCard(5, i);
		}
		this.shuffleDeck();
	}

	// Method for adding a card to the deck
	private void addCard(int cardNumber, int cardColour) {
		deckOfCards[initPointer++] = new Card(cardNumber, cardColour);
	}
	
	public Card draw() {																	//Should the test be here??
		if ( cardPointer < deckOfCards.length ) {
			//Updates pointer after return
			return ( deckOfCards[ cardPointer++ ] );
		}
	   	 else
	   	 {
	   	    //No more cards in the deck
	   	    return ( null );  
	   	 }
	}
	
	public int cardsLeftInDeck() {
		return deckOfCards.length - cardPointer;
	}

	// Initializing a hand of four cards
	public Card[] initialseHand(){
		Card[] hand = new Card[4];

		// Drawing four cards
		for (int i = 0; i < hand.length; i++) {
			hand[i] = draw();
		}
		return hand;
	}

	// Method for shuffling the deck
	private void shuffleDeck() {
	    int index;
	    Card temp;
	    Random random = new Random();

	    // Randomize the position of the cards
	    for (int i = deckOfCards.length - 1; i > 0; i--)
	    {
	        index = random.nextInt(i + 1);
	        temp = deckOfCards[index];
	        deckOfCards[index] = deckOfCards[i];
	        deckOfCards[i] = temp;
	    }
	}

	@Override
	public Deck clone() throws CloneNotSupportedException{
		Deck temp = new Deck();
		temp.cardPointer = this.cardPointer;
		System.arraycopy(this.deckOfCards, 0, temp.deckOfCards, 0, deckOfCards.length);
		return temp;
	}
	
}
