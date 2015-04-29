import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;


public class FactoryImpl extends UnicastRemoteObject implements Factory {

	private static final long serialVersionUID = 1L;
	static Logger logger= Logger.getLogger("global");
	private String host;
	private int italian=1;
	private int french=1;
	private int any=1;
	
	protected FactoryImpl(String nome) throws RemoteException {
		super();
		host="localhost";
		try{
			Naming.rebind("rmi://"+host+"/"+nome, this);
		}catch(Exception e){
			logger.severe("Problemi con la rebind");
			e.printStackTrace();
		}
	}

	public Hello creaHello(String from) throws RemoteException {
		if (from.equals("Italia")){
			return new HelloImplItaly("Italy"+italian++);
		}
		else if(from.equals("France")){
			return new HelloImplFrance("France"+french++);
		}
		return new HelloImplAny("Any"+any++);
	}
	
	
	public static void main(String args[]){
		System.setSecurityManager(new RMISecurityManager());
		try{
			new FactoryImpl("FactoryRemoteHello");
		}catch(Exception e){
			logger.severe("Problemi per le creazione della factory:"+ e.getMessage());
			e.printStackTrace();
		}
		System.out.println("Factory pronta!");
	}

}
