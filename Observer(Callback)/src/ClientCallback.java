import java.rmi.*;


public interface ClientCallback extends Remote {
	
	public void notifyMe(String message) throws RemoteException;
}
