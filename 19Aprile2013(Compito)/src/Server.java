import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;


public class Server extends UnicastRemoteObject implements iChat {

	
	private static final long serialVersionUID = 1L;
	private static int stanza;
	/**
	 * @uml.property  name="numMax"
	 */
	private int NumMax;
	/**
	 * @uml.property  name="lista"
	 * @uml.associationEnd  inverse="server:Wrapper" qualifier="nickname:java.lang.String Wrapper"
	 */
	private HashMap<String,Wrapper> lista=new HashMap<String,Wrapper>();

	public Server() throws RemoteException {
		super();
		NumMax=0;
	}

	@Override
	public void dici(Messaggio m) throws RemoteException {
		/** c'era due volte il controllo se era un server prima per stampare ciò che aveva scritto  
		 * e poi per inviare o no il messaggio...ho accorpato i due controlli in uno solo*/
		
		if(m.getMittente().equals("Server")){    //è stato il server ad inviare il messaggio
			Iterator<Wrapper> iteratore=lista.values().iterator();
			while(iteratore.hasNext())
				iteratore.next().getClient().detto(m);
			}else{         //è stato il client ad inviare il messaggio
				System.out.println(m.getMittente()+": "+m.getTesto()+"\nComandi>>>");
				Wrapper mittente=lista.get(m.getMittente());
				Iterator<Wrapper> iteratore=lista.values().iterator();
				while(iteratore.hasNext()){
					Wrapper appoggio=iteratore.next();
					if(appoggio.getStanza()==mittente.getStanza() && !appoggio.equals(mittente))
						appoggio.getClient().detto(m);     //mi ero scordato di invocare prima getClient per ricevere il rifermito al client 
				}
			}
		}


	@Override
	public void iscrivi(String nickname, iClient c1) throws RemoteException {
		System.out.println("il client: "+nickname+" è entrato nella chat \nComandi>>>");
		Iterator<Wrapper> iteratore= lista.values().iterator();
		while(iteratore.hasNext()){
			Wrapper appoggio=iteratore.next();
			if(appoggio.getStanza()==0)
				appoggio.getClient().iscritto(nickname);
		}
		lista.put(nickname, new Wrapper(nickname,c1,NumMax,this));

	}

	@Override
	public void abbandona(String nickname) throws RemoteException {
		System.out.println("il client: "+nickname+" ha abbandonato la chat \nComandi>>>");
		int numstanza=lista.get(nickname).getStanza();
		lista.remove(nickname);
		Iterator<Wrapper> iteratore= lista.values().iterator();
		while(iteratore.hasNext()){
			Wrapper appoggio=iteratore.next();
			if(appoggio.getStanza()==numstanza)
				appoggio.getClient().abbandonato(nickname);
		}
		

	}

	@Override
	public void sposta(String nickname, int numStanza) throws RemoteException {
		if(lista.get(nickname)==null)
			System.out.println("Client non trovato\nComandi>>>");
		else{
			Wrapper appoggio1=lista.get(nickname);
			appoggio1.getClient().spostato(numStanza);
			
			/**l'iterazione dei client per informarli dello spostamento del client l'ho messa prima
			 * dello spostamento effettivo per far si che non si auto informi
			 */
			Iterator<Wrapper> iteratore =lista.values().iterator();
			while(iteratore.hasNext()){
				Wrapper appoggio=iteratore.next(); 
				if(appoggio.getStanza()==numStanza)
					appoggio.getClient().informato(nickname, false); 
			} 
			appoggio1.spostato(numStanza);
			if(numStanza>NumMax){
				NumMax=numStanza;
				this.aggiornaClient();          
			}
		}

	}
	
	
	// modificato la firma del metodo invece di passare il parametro del nuovo numero max
	// utilizza la variabile locale...
	private void aggiornaClient() {
		Iterator<Wrapper> iteratore = lista.values().iterator();
		while(iteratore.hasNext()){
			iteratore.next().nuovaStanza(NumMax);
		}
		
	}

	public void informa(String nickname,int numstanza)throws RemoteException{
		Iterator<Wrapper> iteratore=lista.values().iterator();
		while(iteratore.hasNext()){
			Wrapper appoggio=iteratore.next();
			if(appoggio.getStanza()==numstanza)
				appoggio.getClient().informato(nickname, true);
		}
	}
	
	
	public void incendio(int numstanza)throws RemoteException{
		Iterator<Wrapper> iteratore=lista.values().iterator();
		while(iteratore.hasNext()){
			Wrapper appoggio=iteratore.next();
			if(appoggio.getStanza()==numstanza){
				appoggio.getClient().incendio();
				appoggio.Scappa();
			}
		}
	}
	
	public static void main(String args[]){
		Server server=null;
		String cmd;
		String PROMPT="Comandi>>>";
		System.setSecurityManager(new RMISecurityManager());
		try{
			server = new Server();
			Naming.rebind("rmi://localhost/Stanza", server);
		}catch(Exception e){
			System.out.println("ERRORE!!");
			e.printStackTrace();
			System.exit(1);
		}
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Chat istanziata e pronta per l'uso");	
		
		for( ; ;){
			try{
				System.out.println(PROMPT);
				cmd=in.readLine();
				if(cmd.equals("!sposta")){
					System.out.println("Inserisci il nickname");
					String nick=in.readLine();
					int stanza;
					System.out.println("Inserisci la stanza in cui lo vuoi spostare");
					stanza= Integer.parseInt(in.readLine());
					server.sposta(nick, stanza);
						
				}else{
					if(cmd.equals("!incendio")){
						stanza=0;
						while(stanza==0){    //il controllo sulla stanza andava sotto il comando !incedio non sotto sposta
							System.out.println("Inserisci la stanza da incendiare, (non la zero)");
							stanza= Integer.parseInt(in.readLine());
						}
						server.incendio(stanza);
					}else{
						if(cmd.length()!=0){
							Messaggio m= new Messaggio("Server",cmd);
							server.dici(m);
						}
					}
				}
			}catch(Exception e){
				System.out.println("ERRORE");
				e.printStackTrace();
				System.exit(1);
			}
		}
	
	
	}

}
