package escada.tpc.common.clients.jmx;

import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class Test {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		String arg = "-EBclass escada.tpc.tpcc.TPCCEmulation "
				+ "-LOGconfig configuration.files/logger.xml -KEY false -CLI 1 "
				+ "-STclass escada.tpc.tpcc.TPCCStateTransition "
				+ "-DBclass escada.tpc.tpcc.database.transaction.postgresql.dbPostgresql "
				+ "-TRACEFLAG TRACE -PREFIX TPC-C "
				+ "-DBpath jdbc:postgresql://192.168.180.101:5432/tpcc "
				+ "-DBdriver org.postgresql.Driver "
				+ "-DBusr tpcc -DBpasswd tpcc -POOL 20 -MI 5 -FRAG 1 "
				+ "-RESUBMIT false";

		try {
			JMXServiceURL url = new JMXServiceURL(
					"service:jmx:rmi:///jndi/rmi://localhost:5000/jmxrmi");
			JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
			MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
			Set<ObjectName> beans = mbsc
					.queryNames(
							new ObjectName(
									"escada.tpc.common.clients.jmx:type=ClientEmulationStartup"),
							null);

			System.out.println("beans: " + beans.toString() + "\n");

			for (ObjectName b : beans) {
				ClientEmulationStartupMBean ces = (ClientEmulationStartupMBean) MBeanServerInvocationHandler
						.newProxyInstance(mbsc, b,
								ClientEmulationStartupMBean.class, false);
				if (1 == args.length) {
					/*
					 * if ( args[0].equals("pause") ) ces.pause(); else if (
					 * args[0].equals("unpause") ) ces.unpause(); else if (
					 * args[0].equals("stop") ) ces.stop(); else if (
					 * args[0].equals("kill") ) ces.kill(); else
					 * System.err.println("Invalid command");
					 */
				}
				/*
				 * else ces.start(arg);
				 */
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
