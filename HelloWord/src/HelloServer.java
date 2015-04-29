

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

public class HelloServer extends UnicastRemoteObject implements Hello{
	//Seria UID
	private static final long serialVersionUID = 1L;
	
	//Logger per la classe
	static Logger logger= Logger.getLogger("global");

	
	//Costruttore 
	protected HelloServer() throws RemoteException {
		//vuoto
	}

	public String dimmiQualcosa(String daChi) throws RemoteException {
		
		logger.info("Sto salutando "+ daChi);
		return "Ciao";
	}
	
	public static void main(String args[]){
		System.setSecurityManager(new RMISecurityManager());
		try{
			logger.info("Creo l'oggetto");
			Hello obj= new HelloServer();
			logger.info("ora ne effettuo il rebind");
			Naming.rebind("HelloServer", obj);
			logger.info("PRONTO!!");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
 