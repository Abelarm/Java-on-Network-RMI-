import java.rmi.*;


public interface iClientCallback extends Remote {
	
	public void detto(Messaggio m)throws RemoteException;
	
	public void iscritto(String nickname)throws RemoteException;
	
	public void abbandonato(iClientCallback c1,String nickname)throws RemoteException;

	public void mexPrivato(Messaggio m)throws RemoteException;
}
