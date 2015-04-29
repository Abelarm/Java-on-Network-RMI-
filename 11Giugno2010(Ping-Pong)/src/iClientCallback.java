import java.rmi.*;


public interface iClientCallback extends Remote {

	public void detto(Messaggio m)throws RemoteException;
	
	public void iscritto(String nickname)throws RemoteException;
	
	public void abbandonato(String nickname)throws RemoteException;

	public void chiudi()throws RemoteException;

	public String getNickname()throws RemoteException;
}
