import java.rmi.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Logger;


public class Server extends UnicastRemoteObject implements iChat {

	private static final long serialVersionUID = 1L;
	private static Logger logger= Logger.getLogger("global");
	private HashMap<String,iClientCallback> listaClient= new HashMap<String,iClientCallback>();
	
	private HashMap<String,iClientCallback> camerieri= new HashMap<String,iClientCallback>();
	private Stack<iClientCallback> cuochi=new Stack<iClientCallback>();
	private Stack<Ordine> ordiniPendenti= new Stack<Ordine>();
	private HashMap<String,iClientCallback> lavoro= new HashMap<String,iClientCallback>();
	
	protected Server() throws RemoteException {
		super();
	}

	

	@Override
	public void dici(Messaggio m) throws RemoteException {
		logger.info("il client: "+m.getMittente()+"sta inviando un messaggio");
		iClientCallback mittente=listaClient.get(m.getMittente());
		Iterator<iClientCallback> iteratore= listaClient.values().iterator();
		while(iteratore.hasNext()){
			iClientCallback appoggio=iteratore.next();
			if(!appoggio.equals(mittente))
				appoggio.detto(m);
		}

	}

	@Override
	public String iscrivi(iClientCallback c1,String nickname, String tipo)throws RemoteException {
		if(tipo.equals("Cuoco")){
			logger.info("si vuole iscrivere un cuoco");
			if(cuochi.size()+lavoro.size()<5){
				logger.info("il numero dei cuochi non è sufficiente");
				Iterator<iClientCallback> iteratore= listaClient.values().iterator();
				while(iteratore.hasNext())
					iteratore.next().iscritto(nickname);
				listaClient.put(nickname, c1);
				cuochi.push(c1);
				accoppia();
				return "Iscrizione effettuata con sucesso";
			}else{
				logger.info("troppi cuochi");
				return "Impossibile già abbiamo raggiunto il numero di cuochi necessari";
			}
		}else{
			logger.info("si vuole iscrivere un cameriere");
			if(camerieri.size()<3){
				logger.info("il numero dei camerieri non è sufficiente");
				Iterator<iClientCallback> iteratore= listaClient.values().iterator();
				while(iteratore.hasNext())
					iteratore.next().iscritto(nickname);
				listaClient.put(nickname, c1);
				camerieri.put(nickname, c1);
				return "Iscrizione effettuata con successo";
			}else{
				logger.info("troppi camerieri");
				return "Impossibile già abbiamo raggiunto il numero di camerieri necessari";
			}
		}
	}

	@Override
	public String abbandona(iClientCallback c1,String nickname ,String tipo)throws RemoteException {
		
		if(tipo.equals("Cuoco")){
			logger.info("vuole abbandonare un cuoco");
			Iterator<iClientCallback> iteratore= lavoro.values().iterator();
			iClientCallback appoggio;
			while(iteratore.hasNext()){
				appoggio=iteratore.next();
				if(appoggio.equals(c1))
					return "Non Puoi abbandonare la chat prima di aver finito il tuo piatto";
			}
			int size=cuochi.size();
			for(int i=0;i<size;i++){
				appoggio=cuochi.pop();
				if(!appoggio.equals(c1))
					cuochi.push(appoggio);
			}
		}else{
			logger.info("vuole abbandonare un cameriere");
			camerieri.remove(nickname);
		}
		
		listaClient.remove(nickname);
		Iterator<iClientCallback> iteratore= listaClient.values().iterator();
		while(iteratore.hasNext())
			iteratore.next().abbandonato(nickname);
		
		return "abbandono effettuato con successo";
	}

	@Override
	public void NuovoOrdine(String tavolo, String ordine)throws RemoteException {
			logger.info("è stato creato un nuovo ordine");
			Ordine nuovo = new Ordine(tavolo,ordine);
			ordiniPendenti.push(nuovo);
			accoppia();
	}

	private synchronized void  accoppia() throws RemoteException {
		if(!cuochi.isEmpty()&& !ordiniPendenti.isEmpty()){
			logger.info("stiamo accoppiando un cuoco con un ordine");
			iClientCallback cuoco=cuochi.pop();
			Ordine ordine=ordiniPendenti.pop();
			cuoco.detto(new Messaggio("Nuovo Ordine","tavolo # "+ordine.getTavolo() +"comanda: "+ordine.getOrdine()));
			
			lavoro.put(ordine.getTavolo(), cuoco);
		}
		
	}



	@Override
	public String specifica(String tavolo, String specifica)throws RemoteException {
		logger.info("si sta inviando una specifica");
		iClientCallback cuoco= lavoro.get(tavolo);
		if(cuoco==null)
			return "l'ordine cercato non esiste";
		cuoco.detto(new Messaggio("Specifica",specifica));
		return	"Specifica ricevuta";
		
	}
	
	@Override
	public synchronized String OrdineTerminato(String tavolo,iClientCallback c1) throws RemoteException{	
		if(lavoro.remove(tavolo)==null)
			return "non stai lavorando";
		cuochi.push(c1);
		logger.info("un cuoco ha terminato il proprio piatto");
		accoppia();
		return "piatto terminato con successo";
		
	}
	
	public static void main(String args[]){
		Server server=null;
		System.setSecurityManager(new RMISecurityManager());
		try{
			server= new Server();
			Naming.rebind("Cucina", server);
			
		}catch(Exception e){
			logger.severe("problemi con l'istanziazione del server o con il rebind"+ e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		System.out.println("La cucina è aperta e pronta per l'uso");
	}

}
