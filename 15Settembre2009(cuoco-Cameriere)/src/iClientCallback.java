import java.rmi.Remote;
import java.rmi.RemoteException;


public interface iClientCallback extends Remote {
	
	public void detto(Messaggio m)throws RemoteException;
	
	public void iscritto(String nickname)throws RemoteException;
	
	public void abbandonato(String nickname)throws RemoteException;
}
