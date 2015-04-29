
public class WrapperClient {
	
	private iClientCallback client=null;
	private boolean oratore;
	
	public WrapperClient(iClientCallback c1){
		client=c1;
		oratore=false;
	}
	
	public void settaOratore(){
		oratore=true;
	}
	
	public iClientCallback getClient(){
		return client;
	}
	
	public boolean getStato(){
		return oratore;
	}
}
