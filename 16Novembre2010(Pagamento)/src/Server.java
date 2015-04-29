import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Logger;


public class Server extends UnicastRemoteObject implements iChat {
	private static final long serialVersionUID = 1L;
	static ArrayList<WrapperClient> clientRefs = new ArrayList<WrapperClient>();
	static Logger logger= Logger.getLogger("global");

	protected Server() throws RemoteException {
	}

	@Override
	public String dici(iClientCallback idRef,Messaggio m) throws RemoteException {
		for(int i=0; i< clientRefs.size();i++){
			if(clientRefs.get(i).getClient().equals(idRef)){
				WrapperClient cliente=clientRefs.get(i);
				if(cliente.getCredito()==0)
					return "Errore credito insufficiente";
				else{
					clientRefs.get(i).creditoMeno();
					for(i=0; i< clientRefs.size();i++){		//controlla tutti i client
						WrapperClient c1=clientRefs.get(i);
						c1.getClient().detto(m);							//per ogni client che trova invoca il metodo per la stampa del messaggio(inviadogli il mex stesso
				}
			}
		}
	}
		return null;
}


	@Override
	public void iscrivi(iClientCallback idRef, String nickname)throws RemoteException {
		logger.info("\nEntra "+nickname+".");
		for(int i=0; i< clientRefs.size();i++){		//controlla tutti i client
			iClientCallback c1=clientRefs.get(i).getClient();
			c1.iscritto(nickname);				//per ogni client che trova invoca il metodo peravvertire della iscrittura
		}
		clientRefs.add(new WrapperClient(idRef));				//aggiunge il client alla fine per non spedire anche a lui il messaggio

	}

	@Override
	public void abbandona(iClientCallback idRef, String nickname) throws RemoteException {
		logger.info("\nEsce "+nickname+".");
		clientRefs.remove(idRef);
		for(int i=0; i< clientRefs.size();i++){		//controlla tutti i client
			iClientCallback c1=clientRefs.get(i).getClient();
			c1.abbandonato(nickname);				//per ogni client che trova invoca il metodo peravvertire dell'abbandono
		}

	}

	@Override
	public void ricarica(iClientCallback idRef) throws RemoteException {
		logger.info("il client:"+idRef.getNickname() +"ha ricaricato");	
		for(int i=0; i< clientRefs.size();i++){
			if(clientRefs.get(i).getClient().equals(idRef))
				clientRefs.get(i).ricarica();
		}
	}

	@Override
	public String credito(iClientCallback idRef) throws RemoteException {
		WrapperClient cliente=null;
		logger.info("il client"+idRef.getNickname() +"sta chiedendo il proprio credito");
		for(int i=0; i< clientRefs.size();i++){
			if(clientRefs.get(i).getClient().equals(idRef))
				cliente=clientRefs.get(i);
		}
		return "Il suo credito :"+cliente.getCredito();
	}

	@Override
	public String regala(iClientCallback idRef, String nickname)throws RemoteException {
		logger.info("il client"+idRef.getNickname() +"sta regalando del credito a: "+nickname);
		for(int i=0; i< clientRefs.size();i++){		//controlla tutti i client
			WrapperClient c1=clientRefs.get(i);
			if(c1.getClient().getNickname().equals(nickname)){
				c1.regalato(idRef.getNickname());
				c1.getClient().detto(new Messaggio("Server","L'utente "+idRef.getNickname()+" ti ha regalato 3 crediti"));
				return "Regalo effettuato con successo";
			}
		}
		return "Errore cliente non trovato";
	}

	@Override
	public String mexPrivato(iClientCallback idRef, String nickname, Messaggio m)throws RemoteException {
		logger.info("il client"+idRef.getNickname() +"sta mandando un mex privato a: "+nickname);
		for(int i=0; i< clientRefs.size();i++){		//controlla tutti i client
			WrapperClient c1=clientRefs.get(i);
			if(c1.getClient().getNickname().equals(nickname)){
				c1.getClient().detto(m);
				return "";
			}
		}
		
		return "Errore: nickname inesistente";
	}
	
	public static void main(String args[]){
			System.setSecurityManager(new RMISecurityManager());
			try{
				Server obj = new Server();
				Naming.rebind("ChatServer16", obj);
				System.out.println("Pronto per le connessioni alla chat");
			}catch(Exception e){
				logger.severe("Problemi con oggetti remoti "+ e.getMessage());
				e.printStackTrace();
			}
		}
}


