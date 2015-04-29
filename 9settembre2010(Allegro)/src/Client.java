import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;


public class Client extends UnicastRemoteObject implements iClientCallback {

	private static final long serialVersionUID = 1L;
	private static Logger logger= Logger.getLogger("global");
	static String PROMPT="Comandi >>";

	protected Client() throws RemoteException {
		super();
	}


	public void detto(Messaggio m) throws RemoteException {
		System.out.println(m.getMittente()+": "+m.getTesto()+"\n"+PROMPT);

	}

	@Override
	public void abbandonato(String nickname) throws RemoteException {
		System.out.println("il Client: "+nickname+"ha abbandonato la chat"+"\n"+PROMPT);

	}

	@Override
	public void iscritto(String nickname) throws RemoteException {
		System.out.println("il Client: "+nickname+"si è iscritto nella chat"+"\n"+PROMPT);

	}
	
	public static void main(String args[]){
		String nickname=null;
		Client myself=null;
		iChat server=null;
		String cmd=null;
		if(args.length==0){
			logger.severe("necessito del nickname passato in input");
			System.exit(-1);
		}
		else{
			nickname=args[0];
		}
		try{
			myself= new Client();
			server= (iChat) Naming.lookup("rmi://localhost/ChatAllegro");
			server.iscrivi(myself, nickname);
		}catch(Exception e){
			logger.severe("problemi con la creazione del client o con il lookup del server"+e.getMessage());
			e.printStackTrace();
		}
		BufferedReader in= new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Benvenuto "+nickname);
		for(;;){
			try{
				System.out.println(PROMPT);
				cmd=in.readLine();
				if(cmd.startsWith("!")){ //è un comando
					if(cmd.equals("!smile")){
						server.setAllegro(myself, nickname);
					}
					if(cmd.equals("!serio")){
						server.setTriste(myself, nickname);
					}
					if(cmd.equals("!quit")){
						server.abbandona(myself, nickname);
						System.exit(0);
					}
				}
				else{
					if(cmd.length()!=0){           //è un messaggio
						Messaggio m= new Messaggio(cmd,nickname);
						server.dici(m);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		}

}
