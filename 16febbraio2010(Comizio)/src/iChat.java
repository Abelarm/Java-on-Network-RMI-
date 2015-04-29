import java.rmi.*;


public interface iChat extends Remote {

	public String dici(Messaggio m)throws RemoteException;
	
	public void iscrivi(iClientCallback c1, String nickname) throws RemoteException;
	
	public void abbandona(iClientCallback c1,String nickname) throws RemoteException;
	
	public void startComizio(String nickname)throws RemoteException;
	
	public String mexPrivato(Messaggio m,String nickname)throws RemoteException;
}
