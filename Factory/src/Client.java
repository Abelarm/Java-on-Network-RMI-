import java.rmi.Naming;
import java.util.logging.Logger;


public class Client {

	static Logger logger= Logger.getLogger("global");
	
	public static void main(String[] args) {
		try{
			Factory fact =(Factory)Naming.lookup("rmi://localhost/FactoryRemoteHello");
			Hello hello=(Hello)fact.creaHello("Italia");
			System.out.println(hello.sayHello("Luigi"));
		}catch(Exception e){
			logger.severe("Errore con le invocazioni " + e.getMessage());
			e.printStackTrace();
		}
	}

}
