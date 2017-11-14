import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
	
	
	static int numDocsReceived = 0;
	
	boolean clientStillSending = true;
	
	Socket socket;
	
	Server(Socket socket){
		this.socket = socket;
		
	}
	
	
	public void run(){
		
		try{
			
			
			String contentLength_string = receiveInputStream();
			
			int contentLength = Integer.valueOf((contentLength_string.substring(contentLength_string.indexOf(":") + 1)).replace("\r\n", ""));
			
			System.out.println(contentLength);
			
			//Recreating directory structure locally
			
			System.out.println("Saving to receive" + String.valueOf(numDocsReceived));
			File f = new File("receive" + String.valueOf(numDocsReceived++) + ".xml");
			FileOutputStream fos = new FileOutputStream(f);

			int counter = 0; //keeps track of amount of bytes read
			int num_byte_read = 0;
			
			byte[] http_object_bytes = new byte[contentLength];
			
			while(true) {
				

				//read some amount of bytes and write them to file output stream
				try {

				if(num_byte_read == -1 || counter == contentLength){
						break;
					}
					
					
				System.out.println("Performing read");
				num_byte_read = socket.getInputStream().read(http_object_bytes);
				
				fos.write(http_object_bytes);
				fos.flush();
				fos.getFD().sync();
				}
				catch(IOException e) {
					System.out.println("Error downloading document, IOEXCEPTION");
				}

				
				//increment counter by how many bytes were read for this iteration
				counter+= num_byte_read;
				
			}
			
			fos.close();
			socket.close();
			
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		
	}
	
	
	public String receiveInputStream(){
		
		//integers to represent offset while reading and the number of bytes read
		int off = 0;
		int num_byte_read = 0;
		
		//initialize bytelist to hold data as it is read in
		byte[] get_request_bytes = new byte[2048];
		//String to hold request
		String headerString = "";

		/*read get request*/
		try {
		while(num_byte_read != -1) {
			
			//Read in one byte at a time until the end of get request is reached
			socket.getInputStream().read(get_request_bytes, off, 1);				
			off++;
			headerString = new String(get_request_bytes, 0, off, "US-ASCII");
			if(headerString.contains("\r\n\r\n"))
					break;
			}
		}
		catch(IOException e) {
			System.out.println("Error " + e.getMessage());
		}
		
		return headerString;
		
	}
	




	public static int getNumDocsReceived() {
		return numDocsReceived;
	}

}
