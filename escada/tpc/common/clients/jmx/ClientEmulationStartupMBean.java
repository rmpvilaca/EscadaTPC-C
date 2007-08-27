package escada.tpc.common.clients.jmx;

public interface ClientEmulationStartupMBean {
	public enum Stage {INIT, PAUSED, RUNNING, STOPPED};
	
	public void start(String arg) throws InvalidTransactionException;
	
	public void pause() throws InvalidTransactionException;
	
	public void unpause() throws InvalidTransactionException;
	
	public void stop() throws InvalidTransactionException;
	
	public void kill();
}
