package escada.tpc.common.database;

import java.sql.*;
import java.util.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;
	

public class ConnectionManager
{
  private static Vector availConn = new Vector(0);
  private static int checkedOut = 0;
  private static int totalConnections = 0;

  private static String user = "tpcc";
  private static String passwd = null;
  
  private static String driverName = "org.postgresql.Driver";
  private static String jdbcPath = "jdbc:postgresql://localhost:5432/tpcc";
                                                                                                                             
  private static int maxConn = 1;


 /*
   * It defines the maximum number of available connections.
   * This information must be configured according to the sets of the database, therefore avoiding 
   * to exceed the maximum allowed number of connections of the database.
   *
   * @param int the maximum number of connections
   **/
  public static void setMaxConnection(int mConn)  {
	maxConn = mConn;
  }

  /** 
   * It sets the driver used to connect to the database.
   * It is important to notice that this information is dependent of the database used.
   * It is also important to remember to set the classpath in order to locate the correct driver.
   *
   * @param String the driver
   **/
  public static void setDriverName(String dName)
  {
      driverName = dName;
  }

  /**
   * It sets the jdbc path used to connect to the database.
   * It is important to notice that this information is dependent of the database used.
   * For that reason, it must be set carefully.
   *
   * @param String the jdbc path
   **/
  public static void setjdbcPath(String jdbc)
  {
      jdbcPath = jdbc;
  }

  /**
   * It sets the information about the user indentification and its password in order to connect to 
   * the database.
   *
   * @param user the user identification
   * @param passwd the password used to connect
   **/
  public static void setUserInfo(String usr,String pass)
  {
      user = usr;
      passwd = pass;
  }
  
  /**
  * It retrieves a new connection in order to access the database. It
  * is important to notice that this new connection is usually obtained
  * from a pool. However, if there is not an idle connection available,
  * it tries to create a new one whenever the number of open connections
  * does not exceed the maximun configured value.
  **/
  public synchronized Connection getConnection() {
    Connection con = null;
    boolean acquiredResource = false;
    while (!acquiredResource)  {
    	if (availConn.size() > 0) {
	    	con = (Connection) availConn.firstElement();
		availConn.removeElementAt(0);
        	try {
			if (con.isClosed()) {
			    totalConnections--;	
        		    continue;
	        	}
	        }
		catch (SQLException e) {
        		e.printStackTrace();
		        continue;
	        }
	        acquiredResource = true;
	} else if ((maxConn == 0) || (totalConnections < maxConn)) {
		con = createConnection();
	        acquiredResource = true;
        }
	else if (!(checkedOut < totalConnections))  {
		try {
			System.out.println("Waiting to acquire connection (thread(" + Thread.currentThread().getName() +")");
	           	wait();
		}
		catch (InterruptedException it) {
	 	        continue;
	   	}
        }
    }
                                                                                                                             
    if (con != null) checkedOut++;

    return con;
  }


  /**
  * It returns the connection to the pool in order to improve performance, instead of closing the connection.
  *
  * @param Connection the connection be released and stored into the pool
  **/
  public synchronized void returnConnection(Connection con) {
     if (con != null) {
     	checkedOut--;
       	availConn.addElement(con);
       	notify();
     }
  }

 /**
  * It returns a new connection to the database, according to the
  * paramters configured at the initialization process.
  *
  * @return the new connection
  **/
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
		}
	     	else con = DriverManager.getConnection(jdbcPath,user,passwd);
          break;
        }
        catch (java.sql.SQLException ex) {
          System.err.println("Error getting connection: " +
                             ex.getMessage() + " : " +
                             ex.getErrorCode() +
                             ": trying to get connection again.");
          ex.printStackTrace();
          java.lang.Thread.sleep(1000);
        }
      }
      con.setAutoCommit(true);

      totalConnections++;

      return con;
    }
    catch (java.lang.Exception ex) {
      ex.printStackTrace();
    }

    return null;
  }

  public synchronized void closeConnection(Connection con) {
	try {
		if (con != null) con.close();
		totalConnections--;
		
	}
	catch(Exception ex) {
		ex.printStackTrace(System.err);
	}
  }
 
}


// arch-tag: e016f45f-3046-4e29-956c-c3267f1d86ba
