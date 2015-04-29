import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import java.util.logging.Logger;


public class HelloImpl extends UnicastRemoteObject implements Hello,ServerCallback {

	private static final long serialVersionUID = 1L;
	static Logger logger= Logger.getLogger("global");
	private Vector<ClientCallback> ClientList;
	
	protected HelloImpl() throws RemoteException {
		super();
		ClientList= new Vector<ClientCallback>();
	}

	public String dimmiQualcosa(String daChi) throws RemoteException {
		logger.info("Saluto "+daChi);
		doCallbacks(daChi);
		return "Ciao";
	}
	
	private synchronized void doCallbacks(String daChi) throws RemoteException {
		for(int i=0;i<ClientList.size();i++){
			logger.info("Effettuo il callback n:"+i);
			ClientCallback c1= (ClientCallback)ClientList.elementAt(i);
			c1.notifyMe("Salutato"+daChi);
		}
		
	}

	@Override
	public void registerCallback(ClientCallback c1) throws RemoteException {
		logger.info("sto aggiungendo un client");
		ClientList.add(c1);

	}

	@Override
	public void unregisterCallback(ClientCallback c1) throws RemoteException {
		logger.info("sto rimuovendo un client");
		ClientList.remove(c1);
	}

	public static void main(String[] args) {
		System.setSecurityManager(new RMISecurityManager());
		try{
			HelloImpl obj= new HelloImpl();
			logger.info("Effetuto il rebind");
			Naming.rebind("HelloServer", obj);
			System.out.println("Pronto!");
		}catch(RemoteException e){
			logger.severe("Problemi con oggetti remoti:"+e.getMessage());
		} catch (Exception e) {
			logger.severe("C' qualche altro problema: "+ e.getMessage());
			e.printStackTrace();
		}

	}


	
}
