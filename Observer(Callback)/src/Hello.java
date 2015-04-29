
import java.rmi.Remote;


public interface Hello extends Remote {
	String dimmiQualcosa(String daChi) throws java.rmi.RemoteException;
}
