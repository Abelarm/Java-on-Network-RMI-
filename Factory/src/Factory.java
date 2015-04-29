import java.rmi.*;


public interface Factory extends Remote {
	
	Hello creaHello(String from) throws RemoteException;
}
