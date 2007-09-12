package escada.tpc.common;

public interface PerformanceSensor {
	
	public static final long DEFAULT_REFRESH_INTERVAL = 10000L;
	
	public static final long MINIMUM_REFRESH_INTERVAL = 1000L;
		
	public static final float MINIMUM_VALUE = 0.05F;

	public long getPerformanceRefreshInterval();

	public void setPerformanceRefreshInterval(long refreshInterval);
	
}
