
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				for (int k = 0; k < 15; k++) {
					for (int l = 0; l < 5; l++) {
						for (int m = 0; m < 15; m++) {
							GameFlow.maxPointMulti = i;
							GameFlow.hintMulti = j;
							GameFlow.infoMulti = k;
							GameFlow.lifeMulti = l;
							GameFlow.pointMulti = m;
							int total = 0;
							for (int m2 = 0; m2 < 10; m2++) {
								GameFlow gameFlow = new GameFlow();
								total = total + gameFlow.gameBoard.getPoints();
							}
							System.out.println(i +" " + j + " " + k +" " + l +" " + m + " " + total);
						}	
					}	
				}	
			}
		}

//		GameFlow.maxPointMulti = 0;
//		GameFlow.hintMulti = 0;
//		GameFlow.infoMulti = 0;
//		GameFlow.lifeMulti = 1;
//		GameFlow.pointMulti = 6;
//		
//		for (int i = 0; i < 100; i++) {
//			GameFlow gameflow = new GameFlow();
//			System.out.println(i);
//		}
		
	}

}
