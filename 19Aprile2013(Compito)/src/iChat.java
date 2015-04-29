import java.rmi.*;


public interface iChat extends Remote {

	public void dici(Messaggio m)throws RemoteException;
	
	public void iscrivi(String nickname, iClient c1)throws RemoteException;
	
	public void abbandona(String nickname)throws RemoteException;
	
	public void sposta(String nickname,int numStanza)throws RemoteException;
}
