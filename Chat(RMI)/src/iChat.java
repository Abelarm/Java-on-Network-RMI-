import java.rmi.*;

//interfaccia del server
public interface iChat extends Remote {
	
	public void dici(Messaggio m) throws RemoteException;			//metodo per spedire messaggi ai client
	public void iscrivi(iClientCallback idRef,String nickname) throws RemoteException;	//metodo per aggiungere un client quando si connette
	public void abbandona(iClientCallback idRef,String nickname) throws RemoteException; //metodo per rimuovere un client quando esce
}
