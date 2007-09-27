package escada.tpc.common.clients.jmx;

import java.util.HashSet;

public interface ClientEmulationStartupMBean {
	public enum Stage {
		INIT, PAUSED, RUNNING, STOPPED, FAILOVER
	};

	public void start(String key, String arg, String machine)
			throws InvalidTransactionException;
	
	public void startScenario(int clients, String scenario)
	throws InvalidTransactionException;

	public void pause(String key) throws InvalidTransactionException;

	public void resume(String key) throws InvalidTransactionException;

	public void stop(String key) throws InvalidTransactionException;
	
	public void addServer(String key) throws InvalidTransactionException;
	
	public void removeServer(String key) throws InvalidTransactionException;
	
	public HashSet<String> getServers() throws InvalidTransactionException;
	
	public HashSet<String> getClients() throws InvalidTransactionException;
	
	public int getNumberOfClients(String key) throws InvalidTransactionException;
	
	public int getNumberOfClientsOnServer(String key) throws InvalidTransactionException;
	
	public boolean checkConsistency() throws InvalidTransactionException;	
	
	public void kill();
}
