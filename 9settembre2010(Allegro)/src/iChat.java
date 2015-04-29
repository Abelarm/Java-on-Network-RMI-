import java.rmi.*;


public interface iChat extends Remote {
	
	public void dici(Messaggio m)throws RemoteException;
	
	public void iscrivi(iClientCallback c1,String nickname)throws RemoteException;
	
	public void abbandona(iClientCallback c1,String nickname)throws RemoteException;
	
	public void setAllegro(iClientCallback c1,String nickname)throws RemoteException;
	
	public void setTriste(iClientCallback c1,String nickname)throws RemoteException;
}
