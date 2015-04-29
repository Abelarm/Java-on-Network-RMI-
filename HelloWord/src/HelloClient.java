import java.rmi.Naming;
import java.util.logging.Logger;


public class HelloClient {
	
	//Logger per la classe
	static Logger logger= Logger.getLogger("global");
	
	public static void main(String args[]){
		try{
			logger.info("Sto creando l'oggetto remoto");
			Hello obj= (Hello)Naming.lookup("rmi://localhost/HelloServer");
			logger.info("trovato");
			String risultato= obj.dimmiQualcosa("Luigi");
			System.out.println("Ricevuto:"+risultato);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
