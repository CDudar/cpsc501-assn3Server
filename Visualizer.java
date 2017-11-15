import java.util.Scanner;
import java.util.Vector;

public class Visualizer {

	public String line;
	
	public void visualizeObjects(Vector<Object> objReceived, Asst2TestDriver driver){
		
		Scanner userInputScan = new Scanner(System.in);
		
		while(true){
		
			System.out.println("Choose a document to inspect");
			System.out.println("Enter the number of the document desired");
			System.out.println("Enter -1 to stop visualizing objects");
			
			for(int i = 0; i < objReceived.size(); i++ ){
				System.out.println("Document " + i);
			}
			
			line = userInputScan.next();
			
			int userInput = 0;
			try{
			userInput = Integer.valueOf(line);
			}
			catch(NumberFormatException e){
				System.out.println("Invalid number");
				continue;
			}
			if(userInput >= 0 && userInput < objReceived.size()){
				System.out.println("Document " + userInput + " chosen");	
				
				//visualize object
				try {
					driver.runTest(objReceived.get(userInput));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			else if(userInput == -1){
				System.out.println("Exiting...");
				break;
			}
			else{
				System.out.println("Invalid range entered");
				System.out.println("");
			}
			
		}
		
		userInputScan.close();
		
	}
	
	
	
}
