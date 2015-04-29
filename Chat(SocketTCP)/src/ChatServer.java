import java.io.*;
import java.net.*;
import java.util.logging.Logger;


public class ChatServer {

	static Logger logger= Logger.getLogger("global");
	
	public ChatServer() throws IOException{
		ServerSocket server= new ServerSocket(9999);
		while(true){	//while infinito per l'accept del server
			Socket client= server.accept();
			DataInputStream in = new DataInputStream(client.getInputStream());
			String name= in.readUTF(); //prima read che per via del protocollo serve per trovare il nome
			logger.info("New client: "+ name +" from "+client.getInetAddress());
			ChatHandler c = new ChatHandler(name,client);  //class(thread) che server per gestire I/O di ogni socket connesso al server
			c.start();
		}
	}

	
	public static void main(String args[]){
		try{
			new ChatServer();
		}catch(IOException e){
			logger.severe("Problemi con l'uso dei socket:"+e.getMessage());
			e.printStackTrace();
		}
	}
}
