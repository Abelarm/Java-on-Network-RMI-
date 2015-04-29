import java.net.*;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Logger;
import java.io.*;

public class ChatHandler extends Thread {
	
	static Logger logger= Logger.getLogger("global");
	Socket socket;
	String name;
	DataInputStream in;
	DataOutputStream out;
	protected static Vector<ChatHandler> handlers= new Vector<ChatHandler>(); //vettore statico (affinche tutte le istanze abbiano lo stesso vettore)
	

	public ChatHandler(String name, Socket client) throws IOException {
		this.name=name;
		this.socket=client;
		in= new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		out= new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	}
	
	public void run(){
		try{
			broadcast("INFO","Entra "+name); //metodo che manda in input a tutti i client un messagio
			handlers.addElement(this);
			while(true){		//while infinitp per controllare lo streamInput
				String message = in.readUTF();
				broadcast(name,message);
			}
		}catch(IOException e){
			logger.warning("Connessione persa con: "+name+"."+e.getMessage());
			e.printStackTrace();
		}finally{
			handlers.removeElement(this);
			broadcast("INFO",name+"è andato via :'(");
			try{
				socket.close(); 
			}catch(IOException e){
				logger.warning("Il socket era già chiuso?: "+ e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private void broadcast(String from, String message) {
		synchronized(handlers){
			Enumeration<ChatHandler> e= handlers.elements();
			while(e.hasMoreElements()){						//si scorre tutti i client connessi
				ChatHandler handler = e.nextElement();
				try{
					if(!from.equals(handler.name)){			//se il client preso in considerazione è diverso da quello che ha mandato il mex
						handler.out.writeUTF(from+":"+message);
						handler.out.flush();
					}//fine if
				}catch(IOException ex){
					logger.warning("Connessione persa con: "+name+"."+ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
		
	}

}
