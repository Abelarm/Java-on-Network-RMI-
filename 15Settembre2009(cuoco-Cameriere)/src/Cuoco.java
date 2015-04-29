import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.*;
import java.util.logging.Logger;


public class Cuoco {

	private static Logger logger= Logger.getLogger("global");
	private static String PROMPT="Comandi >>>";

	public static void main(String[] args) {
		String cmd;
		Client myself=null;
		iChat server=null;
		String nickname=null;
		if(args.length==0){
			logger.severe("ho bisogno del nome");
			System.exit(1);
		}else{
			nickname=args[0];
		}
		System.setSecurityManager(new RMISecurityManager());
		try{
			myself=new Client();
			server=(iChat)Naming.lookup("rmi://localhost/Cucina");
			String iscritto=server.iscrivi(myself, nickname, "Cuoco");
			if(iscritto.equals("Impossibile già abbiamo raggiunto il numero di cuochi necessari")){
				System.out.println("Impossibile già abbiamo raggiunto il numero di cuochi necessari");
				System.exit(0);
			}
		
		}catch(Exception e){
			logger.severe("problemi con l'istanziazione del client o con la lookup"+e.getMessage());
			e.printStackTrace();
		}
		BufferedReader in= new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Benvenuto nella chat");
		for(;;){
			System.out.println(PROMPT);
			try{
				cmd=in.readLine();
				if(cmd.equals("!terminato")){
					System.out.println("Inserisci il numero del tavolo");
					cmd=in.readLine();
					System.out.println(server.OrdineTerminato(cmd,myself));
				}
				else{
					if(cmd.equals("!quit")){
						String abbandona=server.abbandona(myself, nickname,"Cuoco");
						if(abbandona.equals("abbandono effettuato con successo")){
							System.out.println("abbandono effettuato con successo");
							System.exit(0);
						}
							
						else{
							System.out.println(abbandona+"\n"+PROMPT);
						}
					}
					else{
						if(cmd.length()!=0){ //è un messaggio
							Messaggio m = new Messaggio(nickname,cmd);
							server.dici(m);
						}
					}
				}
			}catch(Exception e){
				logger.severe("Problemi vari"+e.getMessage());
				e.printStackTrace();
				System.exit(-1);
			}
			
		}
	}

}
