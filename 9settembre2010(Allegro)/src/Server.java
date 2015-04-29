import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Logger;


public class Server extends UnicastRemoteObject implements iChat {
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger("global");
	private int numAllegri,numTristi,numClient;
	private boolean felice;
	private HashMap<String,WrapperClient> mappaClient= new HashMap<String,WrapperClient>();

	protected Server() throws RemoteException {
		super();
		numAllegri=numTristi=numClient=0;
		felice=false;
	}
	
	
	public void dici(Messaggio m) throws RemoteException {
		logger.info("il client: "+m.getMittente()+"sta mandando un messaggio");
		iClientCallback mittente= mappaClient.get(m.getMittente()).getClient();
		iClientCallback appoggio=null;
		Iterator<WrapperClient> iteratore= mappaClient.values().iterator();
		while(iteratore.hasNext()){
			appoggio=iteratore.next().getClient();
			if(!appoggio.equals(mittente))
				appoggio.detto(m);
		}

	}

	@Override
	public void iscrivi(iClientCallback c1, String nickname)throws RemoteException {
		numClient++;
		numTristi++;
		Iterator<WrapperClient> iteratore= mappaClient.values().iterator();
		while(iteratore.hasNext()){
			iteratore.next().getClient().iscritto(nickname);
		}
		mappaClient.put(nickname, new WrapperClient(c1));
		
		if(!this.controllaStato() && felice){
			logger.info("la chat è diventata triste");
			felice=false;
			Messaggio m= new Messaggio("Server","BUUUUUU");
			iteratore= mappaClient.values().iterator();
			while(iteratore.hasNext()){
				iteratore.next().getClient().detto(m);
			}
		}
	}

	@Override
	public void abbandona(iClientCallback c1, String nickname)throws RemoteException {
		numClient--;
		if(mappaClient.get(nickname).getStato())
			numAllegri--;
		else
			numTristi--;
		mappaClient.remove(nickname);
		Iterator<WrapperClient> iteratore= mappaClient.values().iterator();
		while(iteratore.hasNext()){
			iteratore.next().getClient().abbandonato(nickname);
		}
		if(!controllaStato() && felice){
			logger.info("la chat è diventata triste");
			felice=false;
			Messaggio m= new Messaggio("BUUUUUU","Server");
			iteratore= mappaClient.values().iterator();
			while(iteratore.hasNext()){
				iteratore.next().getClient().detto(m);
			}
		}else{
			if(controllaStato() && !felice){
				logger.info("la chat è diventata felice");
				felice=true;
				Messaggio m= new Messaggio("YEEEEEE","Server");
				iteratore= mappaClient.values().iterator();
				while(iteratore.hasNext()){
					iteratore.next().getClient().detto(m);
				}
			}
		}

	}

	@Override
	public void setAllegro(iClientCallback c1, String nickname)throws RemoteException {
		mappaClient.get(nickname).setFelice();
		numAllegri++;
		numTristi--;
		Messaggio m= new Messaggio(nickname+": è diventato felice","Server");
		iClientCallback clientfelice=c1;
		iClientCallback appoggio=null;
		Iterator<WrapperClient> iteratore= mappaClient.values().iterator();
		while(iteratore.hasNext()){
			appoggio=iteratore.next().getClient();
			if(!appoggio.equals(clientfelice))
				appoggio.detto(m);
		}
		
		if(controllaStato() && !felice){
			logger.info("la chat è diventata felice");
			felice=true;
			Messaggio m1= new Messaggio("YEEEEE","Server");
			iteratore= mappaClient.values().iterator();
			while(iteratore.hasNext()){
				iteratore.next().getClient().detto(m1);
			}
		}

	}

	@Override
	public void setTriste(iClientCallback c1, String nickname)throws RemoteException {
		mappaClient.get(nickname).setFelice();
		numTristi++;
		numAllegri--;
		Messaggio m= new Messaggio(nickname+": è diventato triste","Server");
		iClientCallback clienttriste=c1;
		iClientCallback appoggio=null;
		Iterator<WrapperClient> iteratore= mappaClient.values().iterator();
		while(iteratore.hasNext()){
			appoggio=iteratore.next().getClient();
			if(!appoggio.equals(clienttriste))
				appoggio.detto(m);
		}
		
		if(!controllaStato() && felice){
			logger.info("la chat è diventata triste");
			felice=false;
			Messaggio m1= new Messaggio("BUUUUU","Server");
			iteratore= mappaClient.values().iterator();
			while(iteratore.hasNext()){
				iteratore.next().getClient().detto(m1);
			}
		}

	}
	
	
	//restituisce true se i tre quarti dei client è maggiore del numero dei felici true=triste false=felice
	public boolean controllaStato(){
		logger.info("numClienti:"+numClient+"numTristi"+numTristi+"numfelici"+numAllegri);
		if((numClient/4)*3<numAllegri){
			logger.info("ha restituito true la chat dovrebbe diventare felice");
			return true;
			}
		else {
			logger.info("ha restituito false la chat dovrebbe diventare triste");
			return false;
		}
	}
	
	public static void main(String args[]){
		try{
			
		Server server= new Server();
		Naming.rebind("ChatAllegro", server);
		}catch(Exception e){
			logger.severe("problemi con la creazione del server o con la rebind"+e.getMessage());
			e.printStackTrace();
		}
		System.out.println("La chat è istanziata e pronta per l'uso");
	}

}
