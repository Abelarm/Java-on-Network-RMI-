import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;


public class Intercettatore extends UnicastRemoteObject implements iIntercettatore{

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger("global");
	private static String PROMPT="Comandi >>>";

	protected Intercettatore() throws RemoteException {
		super();
	}

	@Override
	public void detto(Messaggio m,String tipo) throws RemoteException {
		System.out.println("Tipo>>"+tipo+" "+m.getMittente()+": "+m.getTesto()+"\n"+PROMPT);
		
	}

	@Override
	public void iscritto(String nickname, String tipo) throws RemoteException {
		System.out.println("Tipo>>"+tipo+nickname+"si è iscritto alla chat \n"+PROMPT);
		
	}

	@Override
	public void abbandonato(String nickname, String tipo)
			throws RemoteException {
		System.out.println("Tipo>>"+tipo+nickname+"ha abbandonato la chat \n"+PROMPT);
		
	}
	
	public static void main(String args[]){
		String cmd;
		Intercettatore myself=null;
		iChat server=null;
		if(args.length==0){
			logger.severe("ho bisogno di un nickname da linea di comando");
			System.exit(1);
		}
		String nickname=args[0];
		System.setSecurityManager(new RMISecurityManager());
		BufferedReader in= new BufferedReader(new InputStreamReader(System.in));
		try{
			myself=new Intercettatore();
			server=(iChat)Naming.lookup("Intercettatore");
			server.iscrivi(myself, nickname);
		}catch(Exception e){
			logger.severe("problemi con l'istanziazione del client oppure nel lookup o nell'iscrizione"+e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Bevenuto nella chat, intercettatore...");
		for(;;){
			System.out.println(PROMPT);
			try{
				cmd=in.readLine();
				if(cmd.equals("!quit")){
					server.abbandona(myself, nickname);
					System.exit(1);
				}else{
					if(cmd.equals("!fake")){ //messagio finto
						System.out.println("Inescerisci il nickname di un client presente nella chat");
						String fakeNick=in.readLine();
						System.out.println("Inscerisci il messaggio");
						String mex=in.readLine();
						Messaggio m= new Messaggio(fakeNick,mex);
						System.out.println(server.fakedici(m));
					}else{
						if(cmd.equals("!kick")){ //iniziare un kick
							System.out.println("Inescerisci il nickname di un client presente nella chat");
							String KickNick=in.readLine();
							System.out.println(server.startKick(KickNick,nickname));
						}else{
							if(cmd.equals("!yes")){ //votazione
								System.out.println(server.vota(myself));
							}else{
								if(cmd.equals("!unkick")){ //stop kik
									System.out.println(server.stopKick(nickname));
								}else{
									if(cmd.length()!=0){ // un messaggio
										Messaggio m= new Messaggio(nickname,cmd);
										server.dici(myself, m);
									}
								}
							}
						}
					}
				}
			}catch(Exception e){
				logger.severe("c'è un problema da qualche parte"+e.getMessage());
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

}
