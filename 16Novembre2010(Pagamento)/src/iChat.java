import java.rmi.*;

//interfaccia del server
public interface iChat extends Remote {
	
	public String dici(iClientCallback idRef,Messaggio m) throws RemoteException;			//metodo per spedire messaggi ai client
	public void iscrivi(iClientCallback idRef,String nickname) throws RemoteException;	//metodo per aggiungere un client quando si connette
	public void abbandona(iClientCallback idRef,String nickname) throws RemoteException; //metodo per rimuovere un client quando esce
	public void ricarica(iClientCallback idRef) throws RemoteException;  //metodo per ricaricare il client!
	public String credito(iClientCallback idRef) throws RemoteException; //metodo per sapere il credito del client
	public String regala(iClientCallback idRef,String nickname) throws RemoteException;  //metodo per regalare crediti ad un client
	public String mexPrivato(iClientCallback idRef,String nickname,Messaggio m) throws RemoteException; //metodo per l'invio di mex privato
}
