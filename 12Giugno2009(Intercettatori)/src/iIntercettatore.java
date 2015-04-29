import java.rmi.Remote;
import java.rmi.RemoteException;


public interface iIntercettatore extends Remote {
	
	public void detto(Messaggio m,String tipo)throws RemoteException;
	
	public void iscritto(String nickname,String Tipo)throws RemoteException;
	
	public void abbandonato(String nickname,String Tipo)throws RemoteException;
}
