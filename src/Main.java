
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				for (int k = 0; k < 15; k++) {
					for (int l = 0; l < 15; l++) {
						for (int m = 0; m < 15; m++) {
							GameFlow.maxPointMulti = i;
							GameFlow.hintMulti = j;
							GameFlow.infoMulti = l;
							GameFlow.lifeMulti = k;
							GameFlow.pointMulti = m;
							int total = 0;
							for (int m2 = 0; m2 < 10; m2++) {
								GameFlow gameFlow = new GameFlow();
								total = total + gameFlow.gameBoard.getPoints();
							}
							System.out.println(i +" " + j + " " + l +" " + k +" " + m + " " + total);
						}	
					}	
				}	
			}
		}

	}

}
