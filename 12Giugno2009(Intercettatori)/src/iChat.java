import java.rmi.*;


public interface iChat extends Remote {
	
	public void dici(Object c1,Messaggio m)throws RemoteException;
	
	public void iscrivi(Object c1,String nickname)throws RemoteException;
	
	public void abbandona(Object c1,String nickname)throws RemoteException;
	
	public String fakedici(Messaggio m)throws RemoteException;
	
	public String startKick(String nickname,String nickInter)throws RemoteException;
	
	public String vota(iIntercettatore c1)throws RemoteException;
	
	public String stopKick(String nickname)throws RemoteException;
}
