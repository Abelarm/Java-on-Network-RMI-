import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;


public class Client extends UnicastRemoteObject implements iClientCallback {
	
	private static final long serialVersionUID = 1L;
	private static Logger logger= Logger.getLogger("global");
	private static String nickname;
	private static final String HOST="localhost";
	private static final String PROMPT="Comandi >>";
	private static final String HELP="Qualsiasi stringi digitata verrà inviata al tutti.\n"+
	"I comandi sono:\n"+
			"\t !quit per ucire\n"+
			"\t !help per stampare il messaggio di help";

	protected Client() throws RemoteException {

	}

	@Override
	public void detto(Messaggio m) throws RemoteException {
		if(!m.getMittente().equals(nickname))
			System.out.println("\n"+m.getMittente()+": "+m.getTesto()+"\n"+PROMPT);
	}

	public void iscritto(String nickname) throws RemoteException {
		System.out.println("\nEntra "+nickname+"\n"+PROMPT);

	}

	@Override
	public void abbandonato(String nickname) throws RemoteException {
		System.out.println("\n"+nickname+" ha abbandonato la chat"+"\n"+PROMPT);
	}
	
	public static void main(String args[]){
		Client myself=null;
		iChat serverRef=null;
		if(args.length>0)
			nickname=args[0];
		else{
			logger.severe("E' richiesto il nickname.Esco...");
			System.exit(1);
		}
		System.setSecurityManager(new RMISecurityManager());
		try{
			serverRef = (iChat) Naming.lookup("rmi://"+HOST+"/ChatServer");
			myself= new Client();
			serverRef.iscrivi(myself, nickname);
		}catch(Exception e){
			logger.severe("Non riesco a trovare il server o ad iscrivermi. Esco...");
			e.printStackTrace();
			System.exit(1);
		}
		BufferedReader in= new BufferedReader(new InputStreamReader(System.in));
		String cmd="";
		System.out.println("Benvenuto "+nickname);
		for(;;){			//for infinit per le gestione dell'input
			System.out.println(PROMPT);
			try{
				cmd=in.readLine();
			}catch (Exception e){
				e.printStackTrace();
			}
			if(cmd.equals("!quit")){
				try{
					serverRef.abbandona(myself, nickname);
				}catch(Exception e){
					logger.severe("Non riesco ad invocare abbandona sul server.Esco");
					e.printStackTrace();
					System.exit(1);
				}
				break;
			}
			else{
					if(cmd.equals("!help"))
						System.out.println(HELP);
					else{ //Si tratta di un messaggio
						if(cmd.length()!=0){  //se è un messaggio non vuoto
							Messaggio m= new Messaggio(nickname,cmd);
							try{
								serverRef.dici(m);
							}catch(Exception e){
								logger.severe("Non riesco ad invocare dici sul server.Esco...");
								e.printStackTrace();
								System.exit(1);
							}
						}
				}
			}
		} //fine for
		System.exit(1);
	}	//fine main

}
