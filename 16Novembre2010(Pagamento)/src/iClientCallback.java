import java.rmi.*;

//Interfaccia del client 
public interface iClientCallback extends Remote {
	
	public void detto(Messaggio m) throws RemoteException;		//metodo per la stampa di un messaggio inviato dal client
	public void iscritto(String nickname) throws RemoteException;	//metodo per stampare un mex quando il client si iscrive
	public void abbandonato(String nickname) throws RemoteException; //metodo per stampare un mex quando il client esce
	public void mexPrivato(String nickname, Messaggio m)throws RemoteException; //metodo lato client per la ricezione di messaggi
	public String getNickname()throws RemoteException;
}
