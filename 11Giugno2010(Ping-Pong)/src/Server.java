import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;


public class Server extends UnicastRemoteObject implements iChat {
	
	
	private static final long serialVersionUID = 1L;
	private static Logger logger= Logger.getLogger("global");
	private ArrayList<WrapperClient> listaClient= new ArrayList<WrapperClient>();
	private int statoGioco,statoPing;
	private static final String PROMPT="Comandi >>";
	

	protected Server() throws RemoteException {
		statoGioco=statoPing=0;
	}


	public String iscrivi(iClientCallback c1,String nickname) throws RemoteException {
		if(statoGioco==1)
			return"Mi spiace,troppo tardi";
		else{
			logger.info("il Client:"+ c1.getNickname() +"è entrato nella chat");
			Iterator<WrapperClient> lista= listaClient.iterator();
			while(lista.hasNext()){
				lista.next().getClient().iscritto(nickname);
			}
			listaClient.add(new WrapperClient(c1));
			return Integer.toString(statoGioco);
		}
	}

	@Override
	public String dici(iClientCallback c1, Messaggio m) throws RemoteException {
		WrapperClient client = null;
		if(statoGioco==1 && statoPing==1){
			synchronized(listaClient){
				if(m.getTesto().indexOf("pong")!=-1){
					Iterator<WrapperClient> lista= listaClient.iterator();
					while(lista.hasNext()){
						client=lista.next();
						if(client.getClient().equals(c1)){
							client.setPunteggio(1);
							logger.info("il client sta a: "+client.getPunteggio());
							if(this.controllaPunteggi(client))
								this.chiudiClient();
							statoPing=0;
							logger.info("è finita la pazziariella dei ping pong");
							break;
						}
					}
				}
			}
			return "\n Hai vinto questo manche";
		}
		else{
			if(statoGioco==1 && statoPing==0){
				synchronized(listaClient){
					if(m.getTesto().indexOf("pong")!=-1){
						Iterator<WrapperClient> lista= listaClient.iterator();
							while(lista.hasNext()){
								client=lista.next();
								if(client.getClient().equals(c1)){
									client.setPunteggio(-1);
									statoPing=0;
									return "\n Sei stato penalizato perchè hai detto pong nel momento sbagliato";
								}
						}
					}
						
				}
			}
		}
		logger.info("una persona sta mandando un messaggio");
		Iterator<WrapperClient> lista= listaClient.iterator();
		while(lista.hasNext()){
			lista.next().getClient().detto(m);
		}
		return "";
	}

	
	public void settaPing(){
		statoPing=1;
	}
	
	public void startGioco(){
		statoGioco=1;
	}
	
	
	public boolean controllaPunteggi(WrapperClient c1) throws RemoteException{
		if(c1.getPunteggio()==10){
			c1.getClient().detto(new Messaggio("Server","Hai vinto la partita"));
			Messaggio m=new Messaggio("Server","Il giocatore "+c1.getClient().getNickname()+" ha vinto la partita ora la chat terminerà");
			Iterator<WrapperClient> lista=listaClient.iterator();
			while(lista.hasNext()){
				lista.next().getClient().detto(m);
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		else
			return false;
	}
	
	public void chiudiClient(){
		Iterator<WrapperClient> lista= listaClient.iterator();
		while(lista.hasNext()){
			try{
			lista.next().getClient().chiudi();
			}catch(Exception e){
				
			}
		}
	}
	
	public void abbandona(iClientCallback c1,String nickname) throws RemoteException {
		logger.info("il Client:"+ c1.getNickname() +"sta abbandonando la chat");
		Iterator<WrapperClient> lista= listaClient.iterator();
		while(lista.hasNext()){
			WrapperClient client=lista.next();
			if(client.getClient().equals(c1))
				listaClient.remove(client);
			else
				client.getClient().abbandonato(nickname);
		}
	}
	
	public static void main(String args[]){
		BufferedReader in= new BufferedReader(new InputStreamReader(System.in));
		String cmd="";
		System.setSecurityManager(new RMISecurityManager());
		try{
			Server obj = new Server();
			Naming.rebind("ChatServer11", obj);
			System.out.println("Pronto per le connessioni alla chat");
			for(;;){
				System.out.println(PROMPT);
				try{
					cmd=in.readLine();
				}catch(Exception e){
					e.printStackTrace();
				}
				if(cmd.length()!=0){
					if(cmd.equals("!stop")){
						obj.startGioco();
						logger.info("il gioco è iniziato");
					}
					else{
						if(obj.statoGioco==1 && cmd.indexOf("ping")!=-1){
							obj.settaPing();
							logger.info("è partita la pazziariella dei ping pong");
						}
						else{
							Messaggio m=new Messaggio("Server",cmd);
							Iterator<WrapperClient> lista= obj.listaClient.iterator();
							while(lista.hasNext()){
								lista.next().getClient().detto(m);
							}
						}
					}
			}
		}
		}catch(Exception e){
			logger.severe("Problemi con oggetti remoti "+ e.getMessage());
			e.printStackTrace();
		}
	}

}
