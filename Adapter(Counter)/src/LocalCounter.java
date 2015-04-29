

public class LocalCounter {

	//Costruttore
	public LocalCounter(int v){
		value=v;
	}
	
	//legge il valore del contatore
	public synchronized int localGetValue(){
		return value;
	}
	
	//incremente il valore del contatore
	public synchronized void increment(){
		value++;
	}
	
	//variabile istanza
	private int value;
}
