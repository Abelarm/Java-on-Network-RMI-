import java.io.Serializable;


public class Messaggio implements Serializable {

	private static final long serialVersionUID = 1L;
	private String testo;
	private String mittente;
	
	public Messaggio(String mittente, String testo){
		this.mittente=mittente;
		this.testo=testo;
	}
	
	public String getTesto(){
		return testo;
	}
	
	public String getMittente(){
		return mittente;
	}
}

