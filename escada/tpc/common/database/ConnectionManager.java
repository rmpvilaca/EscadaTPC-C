package escada.tpc.common.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.log4j.Logger;

import oracle.jdbc.pool.OracleDataSource;

public class ConnectionManager {
	private static Logger logger = Logger.getLogger(ConnectionManager.class);
	
	private static Vector availConn = new Vector(0);

	private static int checkedOut = 0;

	private static int totalConnections = 0;

	private static String user = "tpcc";

	private static String passwd = null;

	private static String driverName = "org.postgresql.Driver";

	private static String jdbcPath = "jdbc:postgresql://localhost:5432/tpcc";

	private static int maxConn = 1;

	/*
	 * It defines the maximum number of available connections. This information
	 * must be configured according to the sets of the database, therefore
	 * avoiding to exceed the maximum allowed number of connections of the
	 * database.
	 * 
	 * @param int the maximum number of connections
	 */
	public static void setMaxConnection(int mConn) {
		maxConn = mConn;
	}

	/**
	 * It sets the driver used to connect to the database. It is important to
	 * notice that this information is dependent of the database used. It is
	 * also important to remember to set the classpath in order to locate the
	 * correct driver.
	 * 
	 * @param String
	 *            the driver
	 */
	public static void setDriverName(String dName) {
		driverName = dName;
	}

	/**
	 * It sets the jdbc path used to connect to the database. It is important to
	 * notice that this information is dependent of the database used. For that
	 * reason, it must be set carefully.
	 * 
	 * @param String
	 *            the jdbc path
	 */
	public static void setjdbcPath(String jdbc) {
		jdbcPath = jdbc;
	}

	/**
	 * It sets the information about the user indentification and its password
	 * in order to connect to the database.
	 * 
	 * @param user
	 *            the user identification
	 * @param passwd
	 *            the password used to connect
	 */
	public static void setUserInfo(String usr, String pass) {
		user = usr;
		passwd = pass;
	}

	/**
	 * It retrieves a new connection in order to access the database. It is
	 * important to notice that this new connection is usually obtained from a
	 * pool. However, if there is not an idle connection available, it tries to
	 * create a new one whenever the number of open connections does not exceed
	 * the maximun configured value.
	 */
	public synchronized Connection getConnection() {
		Connection con = null;
		boolean acquiredResource = false;
		while (!acquiredResource) {
			if (availConn.size() > 0) {
				con = (Connection) availConn.firstElement();
				availConn.removeElementAt(0);
				try {
					if (con.isClosed()) {
						totalConnections--;
						continue;
					}
				} catch (SQLException sqlex) {
					logger.fatal("Unexpected error. Something bad happend.");
					sqlex.printStackTrace(System.err);
					System.exit(-1);
				}
				acquiredResource = true;
			} else if ((maxConn == 0) || (totalConnections < maxConn)) {
				con = createConnection();
				acquiredResource = true;
			} else if (!(checkedOut < totalConnections)) {
				try {
					logger.info("Waiting to acquire connection.");
					wait();
				} catch (InterruptedException it) {
					continue;
				}
			}
		}

		if (con != null)
			checkedOut++;

		return con;
	}
	
	public synchronized void releaseConnections() {
			Connection con;
			
			while (availConn.size() > 0) {
				con = (Connection) availConn.firstElement();
				availConn.removeElementAt(0);
				try {
					if (! con.isClosed()) {
						con.close();
					}
				} catch (SQLException sqlex) {
					logger.fatal("Unexpected error. Something bad happend.");
					sqlex.printStackTrace(System.err);
					System.exit(-1);
				}
			}
			checkedOut = 0;
			totalConnections = 0;
	}	

	/**
	 * It returns the connection to the pool in order to improve performance,
	 * instead of closing the connection.
	 * 
	 * @param Connection
	 *            the connection be released and stored into the pool
	 */
	public synchronized void returnConnection(Connection con) {
		if (con != null) {
			checkedOut--;
			availConn.addElement(con);
			notify();
		}
	}

	/**
	 * It returns a new connection to the database, according to the paramters
	 * configured at the initialization process.
	 * 
	 * @return the new connection
	 */
	public synchronized Connection createConnection() {
		try {
			Class.forName(driverName);
			Connection con;
			while (true) {
				try {
					if (jdbcPath.indexOf("oracle") != -1) {
						OracleDataSource ods = new OracleDataSource();
						ods.setURL(jdbcPath);
						ods.setUser(user);
						ods.setPassword(passwd);
						con = ods.getConnection();
					} else
						con = DriverManager.getConnection(jdbcPath, user,
								passwd);
						con.setAutoCommit(false);
					break;
				} catch (java.sql.SQLException sqlex) {
					logger.fatal("Unexpected error. Something bad happend.");
					sqlex.printStackTrace(System.err);
					System.exit(-1);
				}
			}
			totalConnections++;
			return con;
		} catch (java.lang.Exception ex) {
			logger.fatal("Unexpected error. Something bad happend.");
			ex.printStackTrace(System.err);
			System.exit(-1);
		}

		return null;
	}

	public synchronized void closeConnection(Connection con) {
		try {
			if (con != null) con.close();
			totalConnections--;

		} catch (Exception ex) {
			logger.error("Error closing connection.");
			ex.printStackTrace(System.err);
		}
	}
}

// arch-tag: e016f45f-3046-4e29-956c-c3267f1d86ba
