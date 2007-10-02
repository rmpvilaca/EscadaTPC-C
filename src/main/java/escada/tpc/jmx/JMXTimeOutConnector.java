package escada.tpc.jmx;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.log4j.Logger;

public class JMXTimeOutConnector {

	private static final Logger logger = Logger.getLogger(JMXTimeOutConnector.class);
	
	public static JMXConnector connectWithTimeout(final JMXServiceURL url, long timeout, TimeUnit unit) {
	    final ExecutorService executor = Executors.newSingleThreadExecutor();
	    final Future<JMXConnector> future = executor.submit(new Callable<JMXConnector>() {
		public JMXConnector call() throws ExecutionException {
			JMXConnector res =null;
		    try {
				res = JMXConnectorFactory.connect(url);
			} catch (IOException e) {
				throw new ExecutionException(e);
			}
			return res;
		}
	    });
	    
	    JMXConnector res = null;
		try {
			res = future.get(timeout, unit);
		} catch (InterruptedException e) {
			logger.warn("Unable to perform JMX connection! (Interrupted Exception)", e);
		} catch (ExecutionException e) {
			logger.warn("Unable to perform JMX connection! (Execution Exception)", e);		
		} catch (TimeoutException e) {
			logger.warn("Unable to perform JMX connection! (Timeout Exception)", e);
		}

		// if the timeout happened and no 
		if(future.isDone() == false && future.isCancelled() == false) {
			future.cancel(true);
		}
		
	    return res;
	}
	
}
