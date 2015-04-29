import java.rmi.*;


public interface iClient extends Remote {

		public void detto(Messaggio m)throws RemoteException;
		
		public void iscritto(String nickname)throws RemoteException;
		
		public void abbandonato(String nickname)throws RemoteException;
		
		public void Kickato()throws RemoteException;
}
