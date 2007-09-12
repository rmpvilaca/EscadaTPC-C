package escada.tpc.tpcc.database.populate.jmx;

import escada.tpc.common.clients.jmx.InvalidTransactionException;

public interface DatabasePopulateMBean {

	public void start(String key, String arg)
			throws InvalidTransactionException;

	public void kill();

}
