
public class Ordine {
	
	private String tavolo;
	private String ordine;
	
	public Ordine(String tavolo, String ordine){
		this.tavolo=tavolo;
		this.ordine=ordine;
	}
	
	public String getTavolo(){
		return tavolo;
	}
	
	public String getOrdine(){
		return ordine;
	}
}
