import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Counter extends Remote {
	
	int getValue(String from) throws RemoteException;
	void sum(String from, int valore) throws RemoteException;
}
