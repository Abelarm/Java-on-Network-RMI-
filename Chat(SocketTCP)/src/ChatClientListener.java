import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Logger;


public class ChatClientListener extends Thread {

	static Logger logger= Logger.getLogger("global");
	DataInputStream in;
	
	public ChatClientListener(DataInputStream in) {
		this.in=in;
	}

	public void run(){
		while(true){
			try{
				System.out.println("\n"+in.readUTF()+"\n>> ");
			}catch(IOException e){
				logger.warning("Chiusura del socket sulla lettura: "+e.getMessage()); //� warning perch� viene utilizato come metodo per la chisura dello stream
				break;
			}
		}
	}

}
