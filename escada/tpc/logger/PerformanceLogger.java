/*
 * Created on 24-May-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package escada.tpc.logger;

import org.apache.log4j.Logger;

/**
 * @author alfranio
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PerformanceLogger {
	private static Logger logger = Logger.getLogger(PerformanceLogger.class);

	public static boolean isPerformanceLoggerEnabled() {
		return (logger.isInfoEnabled());
	}
	
	public static void info(String pStr) {
		logger.info(pStr);
	}
}