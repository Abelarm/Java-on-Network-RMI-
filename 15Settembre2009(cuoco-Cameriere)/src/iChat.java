import java.rmi.*;


public interface iChat extends Remote {
	
	public void dici(Messaggio m)throws RemoteException;

	public String iscrivi (iClientCallback c1,String nickname ,String tipo)throws RemoteException;
	
	public String abbandona(iClientCallback c1,String nickname,String tipo)throws RemoteException;
	
	public void NuovoOrdine(String tavolo,String ordine)throws RemoteException;
	
	public String specifica(String tavolo,String specifica)throws RemoteException;
	
	public String OrdineTerminato(String tavolo,iClientCallback c1)throws RemoteException;
}
