package escada.tpc.tpcc.database.populate.jmx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import escada.tpc.common.clients.jmx.InvalidTransactionException;
import escada.tpc.common.resources.DatabaseResources;
import escada.tpc.common.resources.WorkloadResources;

import escada.tpc.tpcc.database.populate.dbPopulate;
import escada.tpc.tpcc.database.populate.jmx.DatabasePopulateMBean;

public class DatabasePopulate implements DatabasePopulateMBean {
	
	private static final Logger logger = Logger.getLogger(DatabasePopulate.class);
	private DatabaseResources databaseResources = null;
	private WorkloadResources workloadResources = null;

	public DatabasePopulate() {

		if(logger.isInfoEnabled()) {
			logger.info("Trying to load resources for populate!");
		}
		
		databaseResources = new DatabaseResources();
		workloadResources = new WorkloadResources();
	}
	
	public void kill() {
		if(logger.isInfoEnabled()) {
			logger.info("Kill action called. Terminating DatabasePopulate!");
		}
		System.exit(0);
	}

	public void start() throws InvalidTransactionException {

		if(logger.isInfoEnabled()) {
			logger.info("Connecting to database using driver: " + databaseResources.getDriver() + ", username: " + databaseResources.getUserName() + ", connection string: " + databaseResources.getConnectionString());
		}
		
		Connection conn=null;
		try {
			Class.forName(databaseResources.getDriver());
			conn = DriverManager.getConnection(databaseResources.getConnectionString(), databaseResources.getUserName(), databaseResources.getPassword());
			conn.setAutoCommit(false);
		} catch (ClassNotFoundException e) {
			logger.error("Unable to load database driver!", e);
		} catch (SQLException e) {
			logger.error("Exception caught while talking (SQL) to the database!", e);
		}
		
		if(logger.isInfoEnabled()) {
			logger.info("Starting POPULATE process!");
		}

		new dbPopulate(conn, workloadResources.getNumberOfWarehouses());
		
		if(logger.isInfoEnabled()) {
			logger.info("POPULATE process ENDED!");
		}

	}
	
	public void startScenario(String scenario) throws InvalidTransactionException {
		if (configureScenario(scenario)) {
			start();
		}
		else {
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
		
		if (scenario.equalsIgnoreCase("light")) {
			databaseResources.setDriver("org.postgresql.Driver");
			databaseResources.setUserName("tpcc");
			databaseResources.setConnectionString("jdbc:postgresql://192.168.180.32/tpcc");
			databaseResources.setPassword("tpcc");
			workloadResources.setNumberOfWarehouses(4);
			ret = true;
		}
		return (ret);
	}
}
