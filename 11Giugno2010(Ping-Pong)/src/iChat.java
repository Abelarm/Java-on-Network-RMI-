import java.rmi.*;


public interface iChat extends Remote {
	
	public String iscrivi(iClientCallback c1,String nickname)throws RemoteException;
	
	public String dici(iClientCallback c1,Messaggio m)throws RemoteException;
	
	public void abbandona(iClientCallback c1,String nickname)throws RemoteException;
}
