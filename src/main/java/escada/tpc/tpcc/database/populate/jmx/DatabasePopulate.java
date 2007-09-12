package escada.tpc.tpcc.database.populate.jmx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import escada.tpc.common.clients.jmx.InvalidTransactionException;
import escada.tpc.common.resources.DatabaseResources;
import escada.tpc.common.resources.WorkloadResources;

import escada.tpc.tpcc.database.populate.dbPopulate;

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
			System.exit(1);
		} catch (SQLException e) {
			logger.error("Exception caught while talking (SQL) to the database!", e);
			System.exit(1);
		}
		
		if(logger.isInfoEnabled()) {
			logger.info("Starting populating:");
		}

		new dbPopulate(conn, workloadResources.getNumberOfWarehouses());
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

}
