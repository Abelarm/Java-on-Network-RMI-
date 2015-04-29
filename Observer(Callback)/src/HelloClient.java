import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;


public class HelloClient extends UnicastRemoteObject implements ClientCallback {
	
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger("global");
	private static BufferedReader con=new BufferedReader(new InputStreamReader(System.in));

	protected HelloClient() throws RemoteException {
		super();
	}

	//implementazione del metodo della callback
	public void notifyMe(String message) throws RemoteException {
		System.out.println("Notifica dal server"+message);
	}

	
	//metodo di servizio di input
	public static String ask() throws IOException{
		System.out.println(">> ");
		return (con.readLine());
		
	}
	
	public static void main(String[] args) {
		String host="localhost";
		String nome="luigi";
		ServerCallback objCallback=null;
		HelloClient client=null;
		System.setSecurityManager(new RMISecurityManager());
		try{
			client=new HelloClient();
			Object obj= Naming.lookup("rmi://"+host+"/HelloServer"); //oggetto che si riferisce al Server 
			Hello objHello=(Hello)obj;		//copia del server Castata a Hello per invocagli il mettodo dimmiQualcosa	
			objCallback=(ServerCallback)obj;  //copia del server Castata a ServeCallback per invocagli register e unregister
			objCallback.registerCallback(client);
			System.out.println("Ricevuto: "+objHello.dimmiQualcosa(nome));
			System.out.println("Premi invio per terminare...");
			ask();
		}catch(RemoteException e){
			logger.severe("Problemi con oggetti remoti:" +e.getMessage());
			e.printStackTrace();
		}
		catch(Exception e){
			logger.severe("C'è qualche altro problema:" +e.getMessage());
			e.printStackTrace();
		}
		finally{
			System.out.println("Esco...");
			try{
				objCallback.unregisterCallback(client);
			}catch(RemoteException e1){
				logger.severe("Problemi con la unregister; "+e1.getMessage());
				e1.printStackTrace();
			}
		}
	
		System.exit(0);
	}//fine Main

}
