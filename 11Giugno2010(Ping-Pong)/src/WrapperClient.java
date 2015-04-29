
public class WrapperClient {
	
	private iClientCallback client;
	private int punteggio;
	
	public WrapperClient(iClientCallback c1){
		client=c1;
		punteggio=0;
	}
	
	public void setPunteggio(int i){
		punteggio+=i;
	}
	
	public int getPunteggio(){
		return punteggio;
	}
	
	public iClientCallback getClient(){
		return client;
	}
}
