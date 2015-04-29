import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Logger;


public class Server extends UnicastRemoteObject implements iChat {
	
	private static final long serialVersionUID = 1L;
	static ArrayList<iClientCallback> clientRefs = new ArrayList<iClientCallback>();
	static Logger logger= Logger.getLogger("global");


	protected Server() throws RemoteException {
	}

	public void dici(Messaggio m) throws RemoteException {
		for(int i=0; i< clientRefs.size();i++){		//controlla tutti i client
			iClientCallback c1=clientRefs.get(i);
			c1.detto(m);							//per ogni client che trova invoca il metodo per la stampa del messaggio(inviadogli il mex stesso)
		}

	}

	public void iscrivi(iClientCallback idRef, String nickname) throws RemoteException {
		logger.info("\nEntra "+nickname+".");
		for(int i=0; i< clientRefs.size();i++){		//controlla tutti i client
			iClientCallback c1=clientRefs.get(i);
			c1.iscritto(nickname);				//per ogni client che trova invoca il metodo peravvertire della iscrittura
		}
		clientRefs.add(idRef);				//aggiunge il client alla fine per non spedire anche a lui il messaggio
	}

	
	public void abbandona(iClientCallback idRef, String nickname)throws RemoteException {
		logger.info("\nEsce "+nickname+".");
		clientRefs.remove(idRef);
		for(int i=0; i< clientRefs.size();i++){		//controlla tutti i client
			iClientCallback c1=clientRefs.get(i);
			c1.abbandonato(nickname);				//per ogni client che trova invoca il metodo peravvertire dell'abbandono
		}
	}
	
	public static void main(String args[]){
		System.setSecurityManager(new RMISecurityManager());
		try{
			Server obj = new Server();
			Naming.rebind("ChatServer", obj);
			System.out.println("Pronto per le connessioni alla chat");
		}catch(Exception e){
			logger.severe("Problemi con oggetti remoti "+ e.getMessage());
			e.printStackTrace();
		}
	}

}
