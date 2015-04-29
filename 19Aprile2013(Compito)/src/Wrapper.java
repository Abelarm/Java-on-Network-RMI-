import java.rmi.RemoteException;
import java.util.Random;


public class Wrapper {

		/**
		 * @uml.property  name="nickname"
		 */
		private String nickname;
		/**
		 * @uml.property  name="client"
		 * @uml.associationEnd  multiplicity="(1 1)"
		 */
		private iClient client;
		/**
		 * @uml.property  name="numMax"
		 */
		private int numMax;
		/**
		 * @uml.property  name="numStanza"
		 */
		private int numStanza;
		/**
		 * @uml.property  name="server"
		 * @uml.associationEnd  multiplicity="(1 1)" inverse="lista:Server"
		 */
		private Server server;
		
	public Wrapper(String nick,iClient c1,int nummax,Server server){
		nickname=nick;
		client=c1;
		numMax=nummax;
		this.server=server;
		numStanza=0;
	}
	
	public String getNick(){
		return nickname;
	}
	
	
	/**
	 * @return
	 * @uml.property  name="client"
	 */
	public iClient getClient(){
		return client;
	}
	
	public int getStanza(){
		return numStanza;
	}
	
	public void spostato(int num){
		numStanza=num;
	}
	
	public void Scappa() throws RemoteException{
		long seed=10;
		Random rnd= new Random(seed);
		int numStanzax=rnd.nextInt(numMax);
		while(numStanzax==numStanza)
			numStanzax=rnd.nextInt(numMax+1);
	
		client.spostato(numStanzax);
		server.informa(nickname, numStanzax);
		
		
		
		//ho spostato questa istruzione sotto informa per far si che non si autoinformi
		numStanza=numStanzax;
		
	}
	
	public void nuovaStanza(int num){
		numMax=num;
	}
}
