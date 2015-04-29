import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;


public class Client extends UnicastRemoteObject implements iClient{


	private static final long serialVersionUID = 1L;
	private static Logger logger= Logger.getLogger("global");
	private static String PROMPT="Comandi>>>";

	protected Client() throws RemoteException {
		super();
	}
	
	@Override
	public void detto(Messaggio m) throws RemoteException {
		System.out.println(m.getMittente()+": "+m.getTesto()+"\n"+PROMPT);		
	}

	public void iscritto(String nickname) throws RemoteException {
		System.out.println("il Client: "+nickname+ " si è connesso alla chat");
		
	}

	@Override
	public void abbandonato(String nickname) throws RemoteException {
		System.out.println("il Client: "+nickname+ " ha abbandonato la chat");
		
	}

	@Override
	public void Kickato() throws RemoteException {
		System.out.println("Se stato kickato dalla chat");
		System.exit(1);
		
	}
	
	
	public static void main(String args[]){
		iChat server=null;
		Client myself=null;
		String cmd,nickname=null;
		if(args.length==0){
			logger.severe("ho bisogno del nick passato in input");
			System.exit(1);
		}else
			nickname=args[0];
		System.setSecurityManager(new RMISecurityManager());
		try{
			myself=new Client();
			server=(iChat)Naming.lookup("Intercettatore");
			server.iscrivi(myself, nickname);
			
		}catch(Exception e){
			logger.severe("problemi con l'istanziasione del server o del client o con la lookup");
		}
		BufferedReader in= new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Bevenuto nella chat");
		for(;;){
			System.out.println(PROMPT);
			try{
				cmd=in.readLine();
				if(cmd.equals("!quit")){
					server.abbandona(myself, nickname);
					System.exit(1);
				}else{
					if(cmd.length()!=0){		//E un messsaggio
						Messaggio m= new Messaggio(nickname,cmd);
						server.dici(myself, m);
					}
				}
			}catch(Exception e ){
				
			}
			
		}
	}

}
