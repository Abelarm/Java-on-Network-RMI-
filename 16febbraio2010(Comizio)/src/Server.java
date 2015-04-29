import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;


public class Server extends UnicastRemoteObject implements iChat {
	

	private static final long serialVersionUID = 1L;
	public static Logger logger= Logger.getLogger("global");
	private static HashMap<String,WrapperClient> mappaClient = new HashMap<String,WrapperClient>();
	private boolean statoComizio;
	private static String PROMPT="Comandi >>";

	protected Server() throws RemoteException {
		statoComizio=false;
	}

	@Override
	public String dici(Messaggio m) throws RemoteException {
		if(!statoComizio){
			logger.info("messaggio normale");
			WrapperClient mittente=mappaClient.get(m.getMittente());
			Iterator<WrapperClient> lista=(mappaClient.values()).iterator();
			while(lista.hasNext()){
				WrapperClient appoggio=lista.next();
				if(!appoggio.equals(mittente))
					appoggio.getClient().detto(m);
			}
			return "";
		}else{
			if(mappaClient.get(m.getMittente()).getStato()){
				logger.info("l'oratore sta mandando un messaggio");
				WrapperClient mittente=mappaClient.get(m.getMittente());
				Iterator<WrapperClient> lista=(mappaClient.values()).iterator();
				while(lista.hasNext()){
					WrapperClient appoggio=lista.next();
					if(!appoggio.equals(mittente))
						appoggio.getClient().detto(m);
				}
				return "";
			}
			else{
				logger.info("un Client sta cercando di mandare un messaggio quando si è in modalità comizio");
				System.out.println("\n"+m.getMittente()+": "+m.getTesto()+"\n"+PROMPT);
				return "non puoi inviare messaggi a tutti quando si è in modalità comizio";
			}
		}
	}

	@Override
	public void iscrivi(iClientCallback c1, String nickname) throws RemoteException {
		
		Iterator<WrapperClient> lista=(mappaClient.values()).iterator();
		while(lista.hasNext()){
			lista.next().getClient().iscritto(nickname);
		}
		mappaClient.put(nickname, new WrapperClient(c1));
	}

	@Override
	public void abbandona(iClientCallback c1, String nickname)throws RemoteException {
		mappaClient.remove(nickname);
		Iterator<WrapperClient> lista=(mappaClient.values()).iterator();
		while(lista.hasNext()){
			lista.next().getClient().abbandonato(c1, nickname);
		}
	}

	@Override
	public void startComizio(String nickname) throws RemoteException {
		if(mappaClient.get(nickname)!=null){
			statoComizio=true;
			logger.info("Il comizio è iniziato");
			mappaClient.get(nickname).settaOratore();
			
			Messaggio m= new Messaggio("Server","E' iniziata la fase del comizio");
			Iterator<WrapperClient> lista=(mappaClient.values()).iterator();
			while(lista.hasNext()){
				lista.next().getClient().detto(m);
			}
		}
		else
			System.out.println("Errore il nickname non è presente tra i client connessi");
	}
	
	public String mexPrivato(Messaggio m,String nickname) throws RemoteException{
		logger.info("un client sta mandando un messaggio privato");
		if(mappaClient.get(nickname)!=null)
			mappaClient.get(nickname).getClient().mexPrivato(m);
		else
			return "Errore l'utente che stai cercando non esiste";
		
		return "";
	}
	
	public static void main(String args[]){
		Server server=null;
		try{
			server=new Server();
			Naming.rebind("ChatSeverComizio", server);
		}catch(Exception e){
			logger.severe("problemi con la rebind");
			System.exit(1);
		}
		
		BufferedReader in= new BufferedReader(new InputStreamReader(System.in));
		String cmd="";
		System.out.println("Server istanziato, pronto per le connessioni");
		for(;;){
			try{
				System.out.println(PROMPT);
				cmd=in.readLine();
				if(cmd.length()!=0){ //se scrive qualcosa
					if(server.statoComizio)			//se è in fase di comizio
						System.out.println("non puoi invare messaggi è in fase di comizio");
					else{ //se non è in fase di comizio;
						if(cmd.equals("!comizio")){
							System.out.println("Selezione il cliente da far diventare oratore");
							String nickname= in.readLine();
							server.startComizio(nickname);
						}
						else{ // è un messaggio da inviare a tutti i client
							
							Messaggio m= new Messaggio("Server",cmd);
							Iterator<WrapperClient> lista=(mappaClient.values()).iterator();
							while(lista.hasNext()){
								lista.next().getClient().detto(m);
							}
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
