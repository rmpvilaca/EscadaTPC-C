package escada.tpc.tpcc.database.populate.jmx;

import escada.tpc.common.clients.jmx.InvalidTransactionException;

public interface DatabasePopulateMBean {

	public void start() throws InvalidTransactionException;
	
	public void clean() throws InvalidTransactionException;

	public void startScenario(String scenario) throws InvalidTransactionException;
	
	public void cleanScenario(String scenario) throws InvalidTransactionException;

	public void kill();

}
