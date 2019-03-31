
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int max = 0;
		int i=1;
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
								for (int m2 = 0; m2 < 3; m2++) {
									GameFlow gameFlow = new GameFlow();
									total = total + gameFlow.gameBoard.getPoints();
								}
								if(total > max){
									max = total;
									System.out.println("--------New max found: " + max + "-------------------");
								}
								System.out.println(i +" " + j + " " + k +" " + l +" " + m + " " + total);
							}	
						}	
					}	
				}
		System.out.println("Best max for this run: " + max);


//				GameFlow.maxPointMulti = 0;
//				GameFlow.hintMulti = 2;
//				GameFlow.infoMulti = 1;
//				GameFlow.lifeMulti = 0;
//				GameFlow.pointMulti = 4;
//				
//		int total = 0;
//		for (int i = 0; i < 100; i++) {
//			GameFlow gameflow = new GameFlow();
//			total = total + gameflow.gameBoard.getPoints();
//		}
//		System.out.println(total);
//
	}

}
