import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;


public class Client extends UnicastRemoteObject implements iClientCallback{

	public static Logger logger= Logger.getLogger("global");
	private static final long serialVersionUID = 1L;
	private static String PROMPT="Comandi >>";
	
	protected Client() throws RemoteException {
	}


	@Override
	public void detto(Messaggio m) throws RemoteException {
		System.out.println("\n"+m.getMittente()+": "+m.getTesto()+"\n"+PROMPT);
	}

	@Override
	public void iscritto(String nickname)
			throws RemoteException {
		System.out.println("il Client: "+nickname+"è entrato nella chat");
		
	}

	@Override
	public void abbandonato(iClientCallback c1, String nickname)
			throws RemoteException {
		System.out.println("il Client: "+nickname+"ha abbandonato la chat");
		
	}

	@Override
	public void mexPrivato(Messaggio m) throws RemoteException {
		System.out.println("Questo è un messaggio privato");
		System.out.println("\n"+m.getMittente()+": "+m.getTesto()+"\n"+PROMPT);
		
	}
	
	public static void main(String args[]){
		Client myself=null;
		iChat serverRef=null;
		if(args.length==0){
			logger.severe("Mi serve il nickname...ESCO");
			System.exit(0);
		}
		String nickname=args[0];
		BufferedReader in= new BufferedReader(new InputStreamReader(System.in));
		System.setSecurityManager(new RMISecurityManager());
		String cmd="";
		
		try {
			serverRef= (iChat) Naming.lookup("rmi://localhost/ChatSeverComizio");
			
			myself= new Client();
			serverRef.iscrivi(myself, nickname);
		} catch (Exception e) {
			logger.severe("problemi con il lookup o con la creazione del client o con l'iscrizione...ESCO");
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Benvenuto "+nickname);
		for(;;){
			try {
				System.out.println(PROMPT);
				cmd=in.readLine();
				if(cmd.equals("!private")){ //messaggio privato
					System.out.println("Digita il nome del destinatario");
					String destinatario=in.readLine();
					System.out.println("Scrivi il tuo messaggio");
					String messaggio= in.readLine();
					Messaggio m=new Messaggio(nickname,messaggio);
					serverRef.mexPrivato(m, destinatario);
				}
				else{
					if(cmd.length()!=0){ // è un messaggio
						Messaggio m=new Messaggio(nickname,cmd);
						System.out.println(serverRef.dici(m));
					}
					else{
						if(cmd.equals("!quit")){
							serverRef.abbandona(myself, nickname);
						}
					}
				}
			} catch (IOException e) {
				logger.severe("problemi vari ed eventuali");
				e.printStackTrace();
			}
			
		}
		
	}

}
