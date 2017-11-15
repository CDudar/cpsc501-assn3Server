import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class mainServer {

	
	public static void main(String[] args){
		
		
		System.out.println("Creating server");
		WebServer webServer = new WebServer(2226);
		webServer.runServer();
	
	
		SAXBuilder builder = new SAXBuilder();
	
		//int numDocsReceived = Server.getNumDocsReceived
		
		int numDocsReceived = Server.getNumDocsReceived();
		System.out.println("Number of documents received " + numDocsReceived);
		
		Vector<Document> documentsReceived = new Vector<Document>();
		
		for(int i = 0 ; i < numDocsReceived; i++){
			try {
				documentsReceived.add(builder.build(new File("receive" + String.valueOf(i) + ".xml")));
			} catch (JDOMException | IOException e) {
				e.printStackTrace();
			}
		}
	
		
		Vector<Object> objectsReceived = new Vector<Object>();
		
		Deserializer deserializer = new Deserializer();
		
		for(int i = 0; i < documentsReceived.size(); i++){
			objectsReceived.add(deserializer.deserialize(documentsReceived.get(i)));
			
		}
		//Choose a document to inspect, show all the objects in that docs corresponding list of objects
	
		Asst2TestDriver driver;
		try {
			driver = new Asst2TestDriver("ObjectInspector", true);
			Visualizer vis = new Visualizer();
			vis.visualizeObjects(objectsReceived, driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		
	}
	
}
