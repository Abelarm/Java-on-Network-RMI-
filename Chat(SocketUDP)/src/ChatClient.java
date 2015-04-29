import java.io.*;
import java.net.*;
import java.util.logging.Logger;


public class ChatClient {
	
	private static BufferedReader con= new BufferedReader(new InputStreamReader(System.in));
	static Logger logger= Logger.getLogger("global");
	
	private static String ask(){
		String s="";
		System.out.println(">> ");
		try{
			s=con.readLine();
		}catch(IOException e){
			logger.severe("Erore nell'input da console: "+ e.getMessage());
			e.printStackTrace();
		}
		return (s);
	}
	
	public static void main(String args[]){
		int port;
		String nome;
		InetAddress gruppo;
		MulticastSocket chatSocket;
		String cmd="";
		if(args.length!=3){
			logger.severe("Sono necessari tre parametri: nome IpAddressGroup port");
			System.exit(1);
		}
		nome=args[0];
		port=Integer.parseInt(args[2]);
		try{
			gruppo=InetAddress.getByName(args[1]);
			chatSocket = new MulticastSocket(port);
			chatSocket.setTimeToLive(1);
			chatSocket.joinGroup(gruppo);
			cmd="(entra "+nome+")";
			DatagramPacket p= new DatagramPacket(cmd.getBytes(),cmd.length(),gruppo,port);
			chatSocket.send(p);
			ChatClientListener lis= new ChatClientListener(chatSocket);	//classe(thread) che si occupa dell'input dal socketUDP
			lis.start();
			while(!(cmd=ask()).equals("!quit")){			//while per la gestione dell' output verso il socketUDP
				cmd= nome +":"+cmd;
				p= new DatagramPacket(cmd.getBytes(),cmd.length(),gruppo,port);
				chatSocket.send(p);
			}
			chatSocket.close();
		}catch(IOException e){
			logger.severe("Problemi sulla creazione/uso/chiusura del socket: "+  e.getMessage());
			e.printStackTrace();
		}
		System.out.println("Ciao");
	}
}
