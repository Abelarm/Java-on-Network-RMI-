
public class WrapperClient {
	
	private iClientCallback client;
	private boolean felice;
	
	public WrapperClient(iClientCallback c1){
		client=c1;
		felice=false;
		
	}
	
	public iClientCallback getClient(){
		return client;
	}
	
	public boolean getStato(){
		return felice;
	}

	
	public void setFelice(){
		felice=true;
	}
	
	public void setTriste(){
		felice=false;
	}
}
