package escada.tpc.common;

public interface PerformanceCountersMBean extends PerformanceSensor {
	public float getIncommingRate();
	public float getAbortRate();
	public float getCommitRate();
	public double getAverageLatency();
}
