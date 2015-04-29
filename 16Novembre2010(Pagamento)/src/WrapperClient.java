import java.rmi.RemoteException;


public class WrapperClient {
	private iClientCallback client;
	private int nmex;

	
	public WrapperClient(iClientCallback cl){
		client=cl;
		nmex=0;
	}
	
	public void ricarica() {
		nmex=3;
	}
	
	public int getCredito() {
		return nmex;
	}
	public void regalato(String nickname)throws RemoteException{
		nmex+=3;
	}
	
	public void creditoMeno(){
		nmex--;
	}
	
	public iClientCallback getClient(){
		return client;
	}
}
