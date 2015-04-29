import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;


public class HelloImplFrance extends UnicastRemoteObject implements Hello {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger("global");
	private String myid;

	public HelloImplFrance(String string) throws RemoteException{
		super();
		myid=string;
	}

	@Override
	public String sayHello(String myName) throws RemoteException {
		try{
			logger.info(myid+": Ricevuto"+myName+"@"+getClientHost());
		}catch(ServerNotActiveException e){
			logger.severe("Problemi con la getClientHost: "+e.getMessage());
			logger.info(myid+": Ricevuto"+myName+"@ unknown");
			e.printStackTrace();
		}
		return "Ciao"+myName;
	}

}
