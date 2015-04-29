import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.logging.Logger;


public class CounterClient {
	
	static Logger logger= Logger.getLogger("global");
	
	public static void main(String args[]){
		String host="localhost";
		if(System.getSecurityManager()==null){
			System.setSecurityManager(new RMISecurityManager());
		}
		String nome="luigi";
		try{
			Counter cont= (Counter)Naming.lookup("rmi://"+host+"/Contatore");
			int valore=3;
			cont.sum(nome, valore);
			System.out.println("Totale="+cont.getValue(nome));
		}catch(RemoteException e){
			logger.severe("Problemi con oggetti remoti: "+ e.getMessage());
			e.printStackTrace();
		}
		catch(Exception e){
			logger.severe("C'è qualche altro problema: "+ e.getMessage());
			e.printStackTrace();
		}
	}
	
}
