import java.io.Serializable;


public class Messaggio implements Serializable {
	
	private String testo;
	private String mittente;
	
	public Messaggio(String testo,String mittente){
		this.testo=testo;
		this.mittente=mittente;
	}
	
	public String getTesto(){
		return testo;
	}
	
	public String getMittente(){
		return mittente;
	}

}
