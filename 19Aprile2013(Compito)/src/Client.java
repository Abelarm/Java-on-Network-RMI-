import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class Client extends UnicastRemoteObject implements iClient {


	private static final long serialVersionUID = 1L;
	private static String PROMPT="Comandi>>";

	protected Client() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	
	//in ogni metodo ho messo il prompt (ovvero i comandi)
	
	public void detto(Messaggio m) throws RemoteException {
		System.out.println(m.getMittente()+": "+m.getTesto()+"\n"+PROMPT);

	}

	@Override
	public void iscritto(String nickname) throws RemoteException {
		System.out.println("il client: "+nickname+"si è iscritto alla chat"+"\n"+PROMPT);

	}

	@Override
	public void abbandonato(String nickname) throws RemoteException {
		System.out.println("il client: "+nickname+"ha abbandonato la chat"+"\n"+PROMPT);

	}

	@Override
	public void spostato(int numStanza) throws RemoteException {
		System.out.println("Sei stato spostato nella stanza: "+numStanza+"\n"+PROMPT);

	}

	@Override
	public void incendio() throws RemoteException {
		System.out.println("C'è un incedio nella tua stanza scappa"+"\n"+PROMPT);

	}

	@Override
	public void informato(String nickname, boolean incendio) throws RemoteException {
		if(incendio)
			System.out.println("L'utente: "+nickname+" è stato spostato per via di un incedio");
		else
			System.out.println("L'utente: "+nickname+" è stato spostato nella tua stanza");

	}
	
	public static void main(String args[]){
		String nickname = null;
		iChat server=null;
		Client myself;
		String cmd=null;
		if(args.length!=0)
			nickname=args[0];
		else{
			System.out.println("Server un nick per partecipare alla chat");
			System.exit(1);
		}
		System.setSecurityManager(new RMISecurityManager());
		try{
			myself=new Client();
			server=(iChat)Naming.lookup("rmi://localhost/Stanza");
			server.iscrivi(nickname, myself);
		}catch(Exception e){
			System.out.println("Errore");
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("Benvenuto nella chat");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		for(;;){
			try{
				System.out.println(PROMPT);
				cmd=in.readLine();
				if(cmd.length()!=0){
					if(cmd.equals("!quit")){
						server.abbandona(nickname);
						System.exit(0);
					}else{
						Messaggio m= new Messaggio(nickname,cmd);
						server.dici(m);
					}
				}
			}catch(Exception e){
				System.out.println("Errore");
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

}
