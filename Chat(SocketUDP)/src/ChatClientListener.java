import java.io.IOException;
import java.net.*;


public class ChatClientListener extends Thread{
	private MulticastSocket multisocket;
	private byte[] buffer = new byte[100];
	private DatagramPacket packet= new DatagramPacket(buffer,buffer.length);
	

	public ChatClientListener(MulticastSocket chatSocket) {
		multisocket=chatSocket;
	}
	
	public void run(){
		while(true){
			try{
				java.util.Arrays.fill(buffer,new Integer(0).byteValue());
				multisocket.receive(packet);
				System.out.print("\n"+(new String(buffer).trim())+"\n>> ");
			}catch(IOException e){
				System.out.println("Connessione terminata");
				break;
			}
		}
	}

}
