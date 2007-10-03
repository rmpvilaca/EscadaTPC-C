package escada.tpc.common.clients;

public interface ClientEmulationMaster {
	public void notifyThreadsCompletion(String key);
	public void notifyThreadsError(String key);
}
