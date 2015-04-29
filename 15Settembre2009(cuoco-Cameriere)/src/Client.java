import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class Client extends UnicastRemoteObject implements iClientCallback {

	private static final long serialVersionUID = 1L;
	private static String PROMPT="Comandi >>>";
	
	
	protected Client() throws RemoteException {
		super();
	}
	
	@Override
	public void detto(Messaggio m) throws RemoteException {
		System.out.println(m.getMittente()+": "+ m.getTesto()+"\n"+PROMPT);

	}

	@Override
	public void iscritto(String nickname) throws RemoteException {
		System.out.println("il Client: "+nickname+" si è iscritto alla chat \n"+PROMPT);
	}

	@Override
	public void abbandonato(String nickname) throws RemoteException {
		System.out.println("il Client: "+nickname+"ha abbandonato la chat \n"+PROMPT);
	}

}
