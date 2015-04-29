import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Logger;


public class CounterServer {
	
	static Logger logger = Logger.getLogger("global");
	private static BufferedReader con = new BufferedReader(new InputStreamReader(System.in));
	
	
	
	public static void main(String args[]){
		String cmd;
		if(System.getSecurityManager()==null)
			System.setSecurityManager(new RMISecurityManager());
		try{
			RemoteCounter cont= new RemoteCounter("Contatore",0);
			System.out.println("Pronto!");
			while(!(cmd=ask()).equals("quit")){
				if(cmd.equals("valore"))
					System.out.println("localGetValue:"+cont.localGetValue()); //invoca il metodo(locale) senza creare log
				else 					
					if (cmd.equals("valore(remoto)"))								//metodo di sopra e di sotto sono uguali
						System.out.println("getValue: "+cont.getValue("Server")); //invoca il metodo(remoto) ovvero quello che crea un log(mettendo come esecutore dell'operazione il server)
					else 
						if(cmd.equals("nome"))
							System.out.println("getName:"+cont.getName());
						else
							if(cmd.equals("movimenti")){
								Vector<String> v= cont.getAccesses();
								synchronized(v){
									for(Enumeration<String> e =v.elements() ;e.hasMoreElements() ;)
										System.out.println(e.nextElement());
									}//fine sinchronized
								} //fine if movimenti
							else
								System.out.println("ERRORE");
							} //end while
	}catch(RemoteException e){
		logger.severe("Problemi con oggetti remoti: "+ e.getMessage());
		e.printStackTrace();
	}
	catch(Exception e){
		logger.severe("C' qualche altro problema: "+ e.getMessage());
		e.printStackTrace();
	}
	System.out.println("Ciao");
	System.exit(0);
}
		
	private static String ask() throws IOException{
		System.out.println(">> ");
		return (con.readLine());
	}
}
