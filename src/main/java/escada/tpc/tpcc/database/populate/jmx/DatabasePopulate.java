package escada.tpc.tpcc.database.populate.jmx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;

import escada.tpc.common.TPCConst;
import escada.tpc.common.clients.jmx.InvalidTransactionException;
import escada.tpc.common.resources.DatabaseResources;
import escada.tpc.common.resources.WorkloadResources;

import escada.tpc.tpcc.TPCCConst;
import escada.tpc.tpcc.database.populate.dbPopulate;
import escada.tpc.tpcc.database.populate.jmx.DatabasePopulateMBean;

public class DatabasePopulate implements DatabasePopulateMBean {

	private static final Logger logger = Logger
			.getLogger(DatabasePopulate.class);

	private DatabaseResources databaseResources = null;

	private WorkloadResources workloadResources = null;

	private HashSet<String> servers = new HashSet<String>();

	public DatabasePopulate() {

		if (logger.isInfoEnabled()) {
			logger.info("Trying to load resources for populate!");
		}

		databaseResources = new DatabaseResources();
		workloadResources = new WorkloadResources();
	}

	public void kill() {
		if (logger.isInfoEnabled()) {
			logger.info("Kill action called. Terminating DatabasePopulate!");
		}
		System.exit(0);
	}

	public void start() throws InvalidTransactionException {

		if (logger.isInfoEnabled()) {
			logger.info("Connecting to database using driver: "
					+ databaseResources.getDriver() + ", username: "
					+ databaseResources.getUserName() + ", connection string: "
					+ databaseResources.getConnectionString());
		}

		Connection conn = null;
		try {
			Class.forName(databaseResources.getDriver());
			conn = DriverManager.getConnection(databaseResources
					.getConnectionString(), databaseResources.getUserName(),
					databaseResources.getPassword());
			conn.setAutoCommit(false);

			if (logger.isInfoEnabled()) {
				logger.info("Starting POPULATE process!");
			}

			dbPopulate db = new dbPopulate();
			db.populate(conn, workloadResources.getNumberOfWarehouses());
			conn.close();

			if (logger.isInfoEnabled()) {
				logger.info("POPULATE process ENDED!");
			}

		} catch (ClassNotFoundException e) {
			logger.error("Unable to load database driver!", e);
		} catch (SQLException e) {
			logger.error(
					"Exception caught while talking (SQL) to the database!", e);
		}
	}

	public void clean() {
		Iterator<String> it = servers.iterator();
		

		if (logger.isInfoEnabled()) {
			logger.info("Starting CLEANUP process!");
		}

		while (it.hasNext()) {

			Connection conn = null;
			String server = null;
			try {
				Class.forName(databaseResources.getDriver());
				server = it.next(); 
				conn = DriverManager.getConnection(server, databaseResources
						.getUserName(), databaseResources.getPassword());
				conn.setAutoCommit(false);

				dbPopulate db = new dbPopulate();
				db.clean(conn);
				conn.close();

			} catch (ClassNotFoundException e) {
				logger.error("Unable to load database driver!", e);
			} catch (SQLException e) {
				logger.error("Error while connecting to " + server);
				logger.error("Exception caught ", e);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("CLEANUP process ENDED!");
		}		
	}

	public void startScenario(String scenario)
			throws InvalidTransactionException {
		if (configureScenario(scenario)) {
			start();
		} else {
			logger.info("Scenario was not found...");
		}
	}

	public void cleanScenario(String scenario)
			throws InvalidTransactionException {
		if (configureScenario(scenario)) {
			clean();
		} else {
			logger.info("Scenario was not found...");
		}
	}

	public synchronized DatabaseResources getDatabaseResources() {
		return databaseResources;
	}

	public synchronized void setDatabaseResources(
			DatabaseResources databaseResources) {
		this.databaseResources = databaseResources;
	}

	public synchronized WorkloadResources getWorkloadResources() {
		return workloadResources;
	}

	public synchronized void setWorkloadResources(
			WorkloadResources workloadResources) {
		this.workloadResources = workloadResources;
	}

	private boolean configureScenario(String scenario) {
		boolean ret = false;

		if (scenario.equalsIgnoreCase("lightPgsql")) {
			databaseResources.setDriver("org.postgresql.Driver");
			databaseResources.setUserName("tpcc");
			databaseResources
			.setConnectionString("jdbc:postgresql://192.168.190.32:5432/tpcc");
			
			databaseResources.setPassword("tpcc");
			workloadResources.setNumberOfWarehouses(4);

			servers.add("jdbc:postgresql://192.168.190.32:5432/tpcc");
			servers.add("jdbc:postgresql://192.168.190.33:5432/tpcc");
			servers.add("jdbc:postgresql://192.168.190.34:5432/tpcc");
			servers.add("jdbc:postgresql://192.168.190.35:5432/tpcc");
			
			TPCConst.setNumMinClients(5);
			TPCCConst.setNumCustomer(100);
			TPCCConst.setNumDistrict(5);
			TPCCConst.setNumItem(10);
			TPCCConst.setNumLastName(99);

			ret = true;
		}
		else if (scenario.equalsIgnoreCase("lightSequoia")) {
			databaseResources.setDriver("org.continuent.sequoia.driver.Driver");
			databaseResources.setUserName("tpcc");
			databaseResources
			.setConnectionString("jdbc:sequoia://192.168.190.32/tpcc");
			
			databaseResources.setPassword("");
			workloadResources.setNumberOfWarehouses(4);

			servers.add("jdbc:sequoia://192.168.190.32/tpcc");
			servers.add("jdbc:sequoia://192.168.190.33/tpcc");
			servers.add("jdbc:sequoia://192.168.190.34/tpcc");
			servers.add("jdbc:sequoia://192.168.190.35/tpcc");
			
			TPCConst.setNumMinClients(5);
			TPCCConst.setNumCustomer(100);
			TPCCConst.setNumDistrict(5);
			TPCCConst.setNumItem(10);
			TPCCConst.setNumLastName(99);

			ret = true;
		}
		return (ret);
	}
}
