import java.io.Serializable;


public class Messaggio implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * @uml.property  name="testo"
	 */
	private String testo;
	/**
	 * @uml.property  name="mittente"
	 */
	private String mittente;
	
	public Messaggio(String mittente, String testo){
		this.mittente=mittente;
		this.testo=testo;
	}
	
	/**
	 * @return
	 * @uml.property  name="testo"
	 */
	public String getTesto(){
		return testo;
	}
	
	/**
	 * @return
	 * @uml.property  name="mittente"
	 */
	public String getMittente(){
		return mittente;
	}
}

