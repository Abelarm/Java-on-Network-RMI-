import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;


public class Server extends UnicastRemoteObject implements iChat{

	private static Logger logger= Logger.getLogger("global");
	private HashMap<String,iIntercettatore> listaIntercettatori= new HashMap<String,iIntercettatore>();
	private HashMap<String,iClient> listaClient= new HashMap<String,iClient>();
	private ArrayList<iIntercettatore> listavotanti= new ArrayList<iIntercettatore>();
	private int numVoti;
	private String sottoVotazione,Starter;
	private boolean statoKick;
	
	private static final long serialVersionUID = 1L;

	protected Server() throws RemoteException {
		super();
		numVoti=0;
		statoKick=false;
	}
	
	@Override
	public void dici(Object c1, Messaggio m) throws RemoteException {
		if(c1 instanceof iClient){
			logger.info("un client sta inviando un messaggio");
			iClient mittente=(iClient) c1;
			Iterator<iClient> iteratore=listaClient.values().iterator();
			Iterator<iIntercettatore> iteratoreInter=listaIntercettatori.values().iterator();
			while(iteratore.hasNext()){
				iClient appoggio=iteratore.next();
				if(!appoggio.equals(mittente))
					appoggio.detto(m);
			}
			while(iteratoreInter.hasNext()){
				iteratoreInter.next().detto(m,"Client");
			}
			
		}else{
			logger.info("un intercettatore sta inviando un messaggio");
			iIntercettatore mittente=(iIntercettatore) c1;
			Iterator<iIntercettatore> iteratoreInter=listaIntercettatori.values().iterator();
			while(iteratoreInter.hasNext()){
				iIntercettatore appoggio=iteratoreInter.next();
				if(!appoggio.equals(mittente))
					appoggio.detto(m,"Intercettatore");
			}
			
		}
		
	}

	@Override
	public void iscrivi(Object c1, String nickname) throws RemoteException {
		if(c1 instanceof iClient){
			logger.info("un client è entrato nella chat");
			iClient client=(iClient) c1;
			Iterator<iClient> iteratore=listaClient.values().iterator();
			while(iteratore.hasNext()){
				iteratore.next().iscritto(nickname);
			}
			listaClient.put(nickname, client);
			Iterator<iIntercettatore> iteratoreInter=listaIntercettatori.values().iterator();
			while(iteratoreInter.hasNext()){
				iteratoreInter.next().iscritto(nickname,"Client");
			}
		}else{
			logger.info("un intercettatore è entrato nella chat");
			iIntercettatore inter=(iIntercettatore) c1;
			Iterator<iIntercettatore> iteratoreInter=listaIntercettatori.values().iterator();
			while(iteratoreInter.hasNext()){
				iteratoreInter.next().iscritto(nickname, "Intercettatore");
			}
			listaIntercettatori.put(nickname, inter);
		}
		
	}

	@Override
	public void abbandona(Object c1, String nickname) throws RemoteException {
		if(c1 instanceof iClient){
			logger.info("un client ha abbandonato la chat");
			listaClient.remove(nickname);
			Iterator<iClient> iteratore=listaClient.values().iterator();
			while(iteratore.hasNext()){
				iteratore.next().abbandonato(nickname);
			}
			Iterator<iIntercettatore> iteratoreInter=listaIntercettatori.values().iterator();
			while(iteratoreInter.hasNext()){
				iteratoreInter.next().abbandonato(nickname,"Client");
			}
		}else{
			logger.info("un intercettatore ha abbandonato la chat");
			Iterator<iIntercettatore> iteratoreInter=listaIntercettatori.values().iterator();
			while(iteratoreInter.hasNext()){
				iteratoreInter.next().abbandonato(nickname, "Intercettatore");
			}
		}
		
	}

	@Override
	public String fakedici(Messaggio m)throws RemoteException {
		logger.info("un intercettatore sta tentando dimandare un messaggio falso");
		iClient appoggio=listaClient.get(m.getMittente());
		if(appoggio==null)
			return "Non puoi fingere il messaggio di un utente che non è connesso alla chat";
		else{
			iClient mittente=listaClient.get(m.getMittente());
			Iterator<iClient> iteratore=listaClient.values().iterator();
			while(iteratore.hasNext()){
				appoggio=iteratore.next();
				if(!appoggio.equals(mittente))
					appoggio.detto(m);
			}
			return "Messaggio finto inviato con successo";
		}
	}

	@Override
	public String startKick(String nickname,String nickInter) throws RemoteException {
		logger.info("un intercettatore ha iniziato uan votazione di kick");
		if(statoKick)
			return "Non puoi inizare una votazione, perché già è in atto un'altra";
		else{
			iClient appoggio=listaClient.get(nickname);
			if(appoggio==null)
				return"Questo nick non esiste";
			else{
				
				listavotanti=new ArrayList<iIntercettatore>();
				
				Iterator<iIntercettatore> iteratoreInter=listaIntercettatori.values().iterator();
				while(iteratoreInter.hasNext()){
					iteratoreInter.next().detto(new Messaggio("Server","E' stata iniziata una votazione contro "+nickname),"Client");
				}
				Starter=nickInter;
				statoKick=true;
				sottoVotazione=nickname;
				return "Votazione iniziata con successo";
			}
		}
	}

	@Override
	public synchronized String vota(iIntercettatore c1) throws RemoteException {
		logger.info("un intercettatore ha votato");
		if(!statoKick)
			return "non c'è nessuna votazione in atto";
		else{
			if(listavotanti.size()==0)
				listavotanti.add(c1);
			else{
				Iterator<iIntercettatore> iteratoreInter=listavotanti.iterator();
				while(iteratoreInter.hasNext()){
					iIntercettatore appoggio=iteratoreInter.next();
					if(appoggio.equals(c1))
						return "non puoi votare hai già votato";
				}

				}
				numVoti++;
				logger.info("i voti sono:"+numVoti);
				if(numVoti==3){
					listaClient.get(sottoVotazione).Kickato();
					listaClient.remove(sottoVotazione);
					statoKick=false;
					sottoVotazione=null;
					numVoti=0;
					Starter=null;
			}
			return "votazione effettuata con successo";
		}
		
	}

	@Override
	public String stopKick(String nickInter) throws RemoteException {
		logger.info("un intercettatore ha stoppato la votazione");
		if(!statoKick)
			return "non c'è nessuna votazione in atto";
		else{
			if(!Starter.equals(nickInter))
				return " non puoi fermare tu la votazione";
			else{
				statoKick=false;
				numVoti=0;
				sottoVotazione=null;
				Starter=null;
			
			return "Votazione stoppata con sucecsso";
			}
		}
		
	}
	
	

	public static void main(String args[]){
		System.setSecurityManager(new RMISecurityManager());
		Server server=null;
		try{
			server=new Server();
			Naming.rebind("rmi://localhost/Intercettatore", server);
		}catch(Exception e){
			logger.severe("problemi con il rebind o con l'istanziazione del server"+e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("il Server è istanziato e pronto per l'uso");
	}
	

}
