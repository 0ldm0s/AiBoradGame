
public class Player {
	
	//private Card[] Hand; Mangler Card klasse
	//private Information;
	
	public Player(){
		//Assign hand
	}
	
	public void takeAction(GameBoard gb, int action){
		switch (action) {
		case 1:
			gb.discardCard(null);
			break;
		case 2:
			gb.putCardOnTable(null);
			break;
		case 3:
			if(gb.useHintIfPossible()){
				//gb.giveHint();
			}
			break;
		default:
			break;
		}
		
	}

}
