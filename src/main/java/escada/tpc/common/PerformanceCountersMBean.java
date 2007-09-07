package escada.tpc.common;

public interface PerformanceCountersMBean {
	public float getIncommingRate();
	public float getAbortRate();
	public float getCommitRate();
}
