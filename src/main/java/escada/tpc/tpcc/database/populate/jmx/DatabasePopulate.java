package escada.tpc.tpcc.database.populate.jmx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import escada.tpc.common.clients.jmx.InvalidTransactionException;
import escada.tpc.common.resources.DatabaseResources;
import escada.tpc.common.resources.TPCResources;

import escada.tpc.tpcc.database.populate.dbPopulate;

public class DatabasePopulate implements DatabasePopulateMBean {
	
	private static final Logger logger = Logger.getLogger(DatabasePopulate.class);

	public void kill() {
		if(logger.isInfoEnabled()) {
			logger.info("Kill action called. Terminating DatabasePopulate!");
		}
		
		System.exit(0);
	}

	public void start(String key, String arg)
			throws InvalidTransactionException {

		if(logger.isInfoEnabled()) {
			logger.info("Trying to load resources for populate!");
		}
		
		DatabaseResources db = new DatabaseResources();
		TPCResources tpc = new TPCResources();

		if(logger.isInfoEnabled()) {
			logger.info("Connecting to database using driver: " + db.getDriver() + ", username: " + db.getUserName() + ", connection string: " + db.getConnString());
		}
		
		Connection conn=null;
		try {
			Class.forName(db.getDriver());
			conn = DriverManager.getConnection(db.getConnString(), db.getUserName(), db.getPassword());
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

		new dbPopulate(conn, tpc.getNumberOfWarehouses());
	}

}
