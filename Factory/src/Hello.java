import java.rmi.*;


public interface Hello extends Remote {
	
	String sayHello(String myName) throws RemoteException;
}
