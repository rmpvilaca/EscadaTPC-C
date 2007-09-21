package escada.tpc.common.clients.jmx;

public interface ClientEmulationStartupMBean {
	public enum Stage {
		INIT, PAUSED, RUNNING, STOPPED, FAILOVER
	};

	public void start(String key, String arg)
			throws InvalidTransactionException;
	
	public void startScenario(String key, String scenario)
	throws InvalidTransactionException;

	public void pause(String key) throws InvalidTransactionException;

	public void resume(String key) throws InvalidTransactionException;

	public void stop(String key) throws InvalidTransactionException;
	
	public void kill();
}
