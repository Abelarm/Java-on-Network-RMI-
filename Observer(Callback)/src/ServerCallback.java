import java.rmi.*;


public interface ServerCallback extends Remote {
	
	public void registerCallback(ClientCallback c1) throws RemoteException;
	
	public void unregisterCallback(ClientCallback c1) throws RemoteException;
}
