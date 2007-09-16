package escada.tpc.common.clients.jmx;

public interface ClientEmulationStartupMBean {
	public enum Stage {
		INIT, PAUSED, RUNNING, STOPPED, FAILOVER
	};

	public void start(String key, String arg)
			throws InvalidTransactionException;

	public void pause(String key) throws InvalidTransactionException;

	public void unpause(String key) throws InvalidTransactionException;

	public void stop(String key) throws InvalidTransactionException;
	
	public void configureCluster(String replicas) throws InvalidTransactionException;

	public void kill();
}
