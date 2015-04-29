import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;


public class Client extends UnicastRemoteObject implements iClientCallback {
	
	/**
	 * 
	 */
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


	@Override
	public void mexPrivato(String nickname, Messaggio m) throws RemoteException {
		System.out.println("\n"+m.getMittente()+" ti ha inviato un messaggio privato: "+m.getTesto()+"\n"+PROMPT);
	}
	
	public String getNickname()throws RemoteException{
		return nickname;
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
			serverRef = (iChat) Naming.lookup("rmi://"+HOST+"/ChatServer16");
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
					else{
						if(cmd.equals("!credito"))
							try {
								String stringa=serverRef.credito(myself);
								if(stringa!=null)
									System.out.println(stringa);
							} catch (RemoteException e) {
								e.printStackTrace();
							}
						else{
						if(cmd.equals("!regala")){
							System.out.println("Scrivi il nick della persona a cui vuoi regalare crediti");
							try {
								cmd=in.readLine();
								String stringa=serverRef.regala(myself,cmd);
								if(stringa!=null)
									System.out.println(stringa);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						else{
							if(cmd.equals("!dici")){
								try{
								System.out.println("Scrivi il nick della persona a cui vuoi inviare il mex privato");
								String destinatario=in.readLine();
								System.out.println("Scrivi il messaggio");
								cmd=in.readLine();
								Messaggio m= new Messaggio(nickname,cmd);
								String stringa=serverRef.mexPrivato(myself, destinatario, m);
								if(stringa!=null)
									System.out.println(stringa);
								}catch(Exception e){
									e.printStackTrace();
								}
							}
							else{
								if(cmd.equals("!ricarica")){
									try {
										serverRef.ricarica(myself);
									} catch (RemoteException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								else{//è un messaggio
								if(cmd.length()!=0){  //se è un messaggio non vuoto
									Messaggio m= new Messaggio(nickname,cmd);
									try {
										String stringa=serverRef.dici(myself,m);
										if(stringa!=null)
											System.out.println(stringa);
									} catch (RemoteException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							}
						}
							}
					}
						}
				}
			}
		} //fine for
		System.exit(1);
	}	//fine main
}
