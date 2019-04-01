import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("Do you want to play the game or watch AI play:");
		System.out.println("Play: 1" );
		System.out.println("Watch = 2");
		 Scanner scanner = new Scanner(System.in);
		 GameFlow gameFlow;
		 int choice = scanner.nextInt();
		 if(choice == 1)
			 gameFlow = new GameFlow(true);
		 else
			 gameFlow = new GameFlow(false);
		 
		 scanner.close();
	}

}
