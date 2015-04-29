
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Logger;


public class RemoteCounter extends LocalCounter implements Counter {
	
	static Logger logger= Logger.getLogger("global");
	String name;
	Vector<String> accesses= new Vector<String>();
	

	public RemoteCounter(String n,int v) throws RemoteException {
		super(v);
		name=n;
		UnicastRemoteObject.exportObject(this);
		try{
			Naming.rebind(name, this);
		}catch(Exception e){
			logger.severe("Problemi con la rebind:" + e.getMessage());
			e.printStackTrace();
		}
		String cr = "Nuovo counter creato il: " + new Date();
		accesses.add(cr);
		logger.info(cr);
	}

	//Metodo che pu˜ essere acceduto sia da locale che da remoto
	public int getValue(String from) throws RemoteException {
		int app= this.localGetValue();
		String cr= "getValue da "+from+" (" + new Date() +"): "+app;
		accesses.add(cr);
		logger.info(cr);
		return app;
	}

	@Override
	public void sum(String from, int volte) throws RemoteException {
		for(int i=1;i<volte;i++)
			this.increment();
		String cr= "sum "+volte+ "da" +from+" (" + new Date() +"): nuovo"+this.localGetValue();
		accesses.add(cr);
		logger.info(cr);

	}
	
	public String getName(){
		return name;
	}

	
	public Vector<String> getAccesses(){
		return accesses;
	}
}
