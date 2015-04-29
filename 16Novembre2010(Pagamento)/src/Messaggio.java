import java.io.Serializable;

//classe che definisce il messaggio che viene spedito dai client
public class Messaggio implements Serializable {
	
	private static final long serialVersionUID=1L;
	private String mittente;
	private String testo;
	
	public Messaggio(String mit, String tes){
		mittente=mit;
		testo=tes;
	}
	
	public String getMittente(){
		return mittente;
	}
	
	public String getTesto(){
		return testo;
	}
}
