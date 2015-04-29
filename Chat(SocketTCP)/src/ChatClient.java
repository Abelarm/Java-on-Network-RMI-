import java.io.*;
import java.net.*;
import java.util.logging.Logger;


public class ChatClient {

	static Logger logger= Logger.getLogger("global");
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private static BufferedReader con= new BufferedReader(new InputStreamReader(System.in));
	
	
	public ChatClient(String name,String server,int port){
		String cmd;
		try{
			socket= new Socket(server,port);
			in = new DataInputStream(socket.getInputStream());
			out= new DataOutputStream(socket.getOutputStream());
			sendTextToChat(name);					//per come abbiamo impostato il protocollo la prima stringa che invia al server  il nome
			ChatClientListener lis= new ChatClientListener(in); //classe(thread) che si occupa della gestione dell Input dal server
			lis.start();
			while(!(cmd=ask()).equals("!quit"))
				sendTextToChat(cmd);				//metodo che si occupa dell'input al server
			socket.close();
			System.out.println("Ciao");
		}catch(IOException e){
			logger.severe("Problemi con il socket: "+ e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static String ask() throws IOException {
		System.out.println(">> ");
		return (con.readLine());
	}

	private void sendTextToChat(String str) {
		try{
			out.writeUTF(str);
		}catch(IOException e){
			logger.severe("Problemi con l'invio: "+ e.getMessage());
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ChatClient("Prova","127.0.0.1",9999);
	}

}
