package escada.tpc.tpcc.database.transaction.oracle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashSet;

import org.apache.log4j.Logger;

import escada.tpc.common.OutInfo;
import escada.tpc.tpcc.database.transaction.*;

/** It is an interface to a postgreSQL, which based is based on the
 * the distributions of the TPC-C.
 **/
public class dbOracle
extends dbTPCCDatabase {
	
	private static Logger logger = Logger.getLogger(dbOracle.class); 

    protected HashSet NewOrderDB(OutInfo obj, Connection con) throws java.sql.
    SQLException {

		boolean resubmit = Boolean.parseBoolean((String) obj
				.getInfo("resubmit"));
		HashSet dbtrace = new HashSet();

		while (true) {
			java.util.Date NetStartTime = null;
			java.util.Date NetFinishTime = null;
			NetStartTime = new java.util.Date();
			
            CallableStatement statement = null;

            ResultSet rs = null;
            String cursor = null;
            String query = "begin ? := PKG_TPCC.tpcc_neworder(?,?,?,?,?,?,?,?); end;";

            StringBuffer iid = new StringBuffer();
            StringBuffer wid = new StringBuffer();
            StringBuffer qtdi = new StringBuffer();

            try {

                statement = con.prepareCall(query);

                statement.registerOutParameter(1, oracle.jdbc.driver.OracleTypes.CURSOR );

                statement.setInt(2, Integer.parseInt((String) obj.getInfo("wid")));

                statement.setInt(3, Integer.parseInt( (String) obj.getInfo("did")));
                statement.setInt(4, Integer.parseInt( (String) obj.getInfo("cid")));
                statement.setInt(5, Integer.parseInt( (String) obj.getInfo("qtd")));
                statement.setInt(6, Integer.parseInt( (String) obj.getInfo("localwid")));

                int icont = 0;
                int qtd = Integer.parseInt( (String) obj.getInfo("qtd"));
                while (icont < qtd) {
                    iid.append( (String) obj.getInfo("iid" + icont));
                    iid.append(",");
                    wid.append((String) obj.getInfo("supwid" + icont));
                    wid.append(",");
                    qtdi.append( (String) obj.getInfo("qtdi" + icont));
                    qtdi.append(",");
                    icont++;
                }

                statement.setString(7, iid.toString());
                statement.setString(8, wid.toString());
                statement.setString(9, qtdi.toString());

                statement.execute();

                rs = (ResultSet)statement.getObject(1);
                if (rs.next()) {
                    cursor = (String) rs.getString(1);
                }
                rs.close();
                rs = null;
                statement.close();
                statement = null;
				NetFinishTime = new java.util.Date();

				processLog(NetStartTime, NetFinishTime, "processing", "w",
						"tx neworder");

			} catch (java.sql.SQLException sqlex) {
				logger.warn("NewOrder - SQL Exception " + sqlex.getMessage());
				if ((sqlex.getMessage().indexOf("serialize") != -1)
						|| (sqlex.getMessage().indexOf("deadlock") != -1)) {
					RollbackTransaction(con, sqlex,"tx neworder","w");
					if (resubmit) {
						InitTransaction(con, "tx neworder","w");
						continue;
					} else {
						throw sqlex;
					}
				} else {
					RollbackTransaction(con, sqlex, "tx neworder","w");
					throw sqlex;
				}
			} catch (java.lang.Exception ex) {
				logger.fatal("Unexpected error. Something bad happend");
				ex.printStackTrace(System.err);
				System.exit(-1);
			} finally {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
			}
			break;
		}
		return (dbtrace);
	}

    protected HashSet DeliveryDB(OutInfo obj, Connection con) throws java.sql.
    SQLException {

		boolean resubmit = Boolean.parseBoolean((String) obj
				.getInfo("resubmit"));
		HashSet dbtrace = new HashSet();

		while (true) {
			java.util.Date NetStartTime = null;
			java.util.Date NetFinishTime = null;
			NetStartTime = new java.util.Date();
			
            CallableStatement statement = null;
            ResultSet rs = null;
            String cursor = null;
            String query = "begin ? := PKG_TPCC.tpcc_delivery(?,?); end;";

            try {

                statement = con.prepareCall(query);
                statement.registerOutParameter(1, oracle.jdbc.driver.OracleTypes.CURSOR );
                statement.setInt(2, Integer.parseInt((String) obj.getInfo("wid")));
                statement.setInt(3, Integer.parseInt( (String) obj.getInfo("crid")));

                statement.execute();

                rs = (ResultSet)statement.getObject(1);

                if (rs.next()) {
                    cursor = (String) rs.getString(1);
                }
                rs.close();
                rs = null;
                statement.close();
                statement = null;
                
				NetFinishTime = new java.util.Date();
				processLog(NetStartTime, NetFinishTime, "processing", "w",
						"tx delivery");

			} catch (java.sql.SQLException sqlex) {
				logger.warn("Delivery - SQL Exception " + sqlex.getMessage());
				if ((sqlex.getMessage().indexOf("serialize") != -1)
						|| (sqlex.getMessage().indexOf("deadlock") != -1)) {
					RollbackTransaction(con, sqlex,"tx delivery","w");
					if (resubmit) {
						InitTransaction(con, "tx delivery","w");
						continue;
					} else {
						throw sqlex;
					}
				} else {
					RollbackTransaction(con, sqlex,"tx delivery","w");
					throw sqlex;
				}
			} catch (java.lang.Exception ex) {
				logger.fatal("Unexpected error. Something bad happend");
				ex.printStackTrace(System.err);
				System.exit(-1);
			} finally {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
			}
			break;
		}
		return (dbtrace);
	}

    protected HashSet OrderStatusDB(OutInfo obj, Connection con) throws java.
    sql.SQLException {

		boolean resubmit = Boolean.parseBoolean((String) obj
				.getInfo("resubmit"));
		HashSet dbtrace = new HashSet();

		while (true) {
			java.util.Date NetStartTime = null;
			java.util.Date NetFinishTime = null;
			NetStartTime = new java.util.Date();
			
            CallableStatement statement = null;
            ResultSet rs = null;
            String cursor = null;
            String query = "begin ? := PKG_TPCC.tpcc_orderstatus(?,?,?,?); end;";

            try {
                statement = con.prepareCall(query);
                statement.registerOutParameter(1, oracle.jdbc.driver.OracleTypes.CURSOR );
                statement.setInt(2, Integer.parseInt((String) obj.getInfo("wid")));
                statement.setInt(3, Integer.parseInt( (String) obj.getInfo("did")));
                statement.setInt(4, Integer.parseInt( (String) obj.getInfo("cid")));
                statement.setString(5, (String) obj.getInfo("lastname")+"%");

                statement.execute();

                rs = (ResultSet)statement.getObject(1);
                if (rs.next()) {
                    cursor = (String) rs.getString(1);
                }
                rs.close();
                rs = null;
                statement.close();
                statement = null;

                NetFinishTime = new java.util.Date();

				String str = (String) (obj).getInfo("cid");

				if (str.equals("0")) {
					processLog(NetStartTime, NetFinishTime, "processing", "r",
							"tx orderstatus 01");
				} else {
					processLog(NetStartTime, NetFinishTime, "processing", "r",
							"tx orderstatus 02");
				}

			} catch (java.sql.SQLException sqlex) {
				logger
						.warn("OrderStatus - SQL Exception "
								+ sqlex.getMessage());
				String str = (String) (obj).getInfo("cid");				
				if ((sqlex.getMessage().indexOf("serialize") != -1)
						|| (sqlex.getMessage().indexOf("deadlock") != -1)) {
					
					if (str.equals("0")) {
						RollbackTransaction(con, sqlex, "tx orderstatus 01","r");
					} else {
						RollbackTransaction(con, sqlex, "tx orderstatus 02","r");
					}

					if (resubmit) {
						if (str.equals("0")) {
							InitTransaction(con, "tx orderstatus 01","r");
						} else {
							InitTransaction(con, "tx orderstatus 02","r");
						}
						continue;
					} else {
						throw sqlex;
					}
				} else {
					if (str.equals("0")) {
						RollbackTransaction(con, sqlex, "tx orderstatus 01","r");
					} else {
						RollbackTransaction(con, sqlex, "tx orderstatus 02","r");
					}
					throw sqlex;
				}
			} catch (java.lang.Exception ex) {
				logger.fatal("Unexpected error. Something bad happend");
				ex.printStackTrace(System.err);
				System.exit(-1);
			} finally {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
			}
			break;
		}
		return (dbtrace);
	}

    protected HashSet PaymentDB(OutInfo obj, Connection con) throws java.sql.
    SQLException {

		boolean resubmit = Boolean.parseBoolean((String) obj
				.getInfo("resubmit"));
		HashSet dbtrace = new HashSet();

		while (true) {
			java.util.Date NetStartTime = null;
			java.util.Date NetFinishTime = null;
			NetStartTime = new java.util.Date();
			
            CallableStatement statement = null;
            ResultSet rs = null;
            String cursor = null;
            String query = "begin ? := PKG_TPCC.tpcc_payment(?,?,?,?,?,?,?); end;";

            try {

                statement = con.prepareCall(query);
                statement.registerOutParameter(1, oracle.jdbc.driver.OracleTypes.CURSOR );
                statement.setInt(2, Integer.parseInt( (String) obj.getInfo("wid")));
                statement.setInt(3, Integer.parseInt( (String) obj.getInfo("cwid")));
                statement.setFloat(4, Float.parseFloat( (String) obj.getInfo("hamount")));
                statement.setInt(5, Integer.parseInt( (String) obj.getInfo("did")));
                statement.setInt(6, Integer.parseInt( (String) obj.getInfo("cdid")));
                statement.setInt(7, Integer.parseInt( (String) obj.getInfo("cid")));
                statement.setString(8, ((String) obj.getInfo("lastname"))+"%");

                statement.execute();

                rs = (ResultSet)statement.getObject(1);

                if (rs.next()) {
                    cursor = (String) rs.getString(1);
                }
                rs.close();
                rs = null;
                statement.close();
                statement = null;
				String str = (String) (obj).getInfo("cid");
				if (str.equals("0")) {
					processLog(NetStartTime, NetFinishTime, "processing", "w",
							"tx payment 01");
				} else {
					processLog(NetStartTime, NetFinishTime, "processing", "w",
							"tx payment 02");
				}
			} catch (java.sql.SQLException sqlex) {
				logger.warn("Payment - SQL Exception " + sqlex.getMessage());

				String str = (String) (obj).getInfo("cid");				
				if ((sqlex.getMessage().indexOf("serialize") != -1)
						|| (sqlex.getMessage().indexOf("deadlock") != -1)) {

					if (str.equals("0")) {
						RollbackTransaction(con, sqlex, "tx payment 01","w");
					} else {
						RollbackTransaction(con, sqlex, "tx payment 02","w");
					}
					
					if (resubmit) {
						if (str.equals("0")) {
							InitTransaction(con, "tx payment 01","w");
						} else {
							InitTransaction(con, "tx payment 02","w");
						}
						continue;
					} else {
						throw sqlex;
					}
					
				} else {
					if (str.equals("0")) {
						RollbackTransaction(con, sqlex, "tx payment 01","w");
					} else {
						RollbackTransaction(con, sqlex, "tx payment 02","w");
					}
					throw sqlex;
				}
			} catch (java.lang.Exception ex) {
				logger.fatal("Unexpected error. Something bad happend");
				ex.printStackTrace(System.err);
				System.exit(-1);
			} finally {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
			}
			break;
		}
		return (dbtrace);
	}

    protected HashSet StockLevelDB(OutInfo obj, Connection con) throws java.
    sql.SQLException {

		boolean resubmit = Boolean.parseBoolean((String) obj
				.getInfo("resubmit"));
		HashSet dbtrace = new HashSet();

		while (true) {
			java.util.Date NetStartTime = null;
			java.util.Date NetFinishTime = null;
			NetStartTime = new java.util.Date();
			
            CallableStatement statement = null;
            ResultSet rs = null;
            String cursor = null;
            String query = "begin ? := PKG_TPCC.tpcc_stocklevel(?,?,?); end;";

            try {
                statement = con.prepareCall(query);

                statement.registerOutParameter(1, oracle.jdbc.driver.OracleTypes.CURSOR );

                statement.setInt(2, Integer.parseInt( (String) obj.getInfo("wid")));
                statement.setInt(3, Integer.parseInt( (String) obj.getInfo("did")));
                statement.setInt(4, Integer.parseInt( (String) obj.getInfo("threshhold")));

                rs = statement.executeQuery();

                rs = (ResultSet)statement.getObject(1);
                if (rs.next()) {
                    cursor = (String) rs.getString(1);
                }
                rs.close();
                rs = null;
                statement.close();
                statement = null;
				NetFinishTime = new java.util.Date();
				processLog(NetStartTime, NetFinishTime, "processing", "r",
						"tx stocklevel");

			} catch (java.sql.SQLException sqlex) {
				logger.warn("StockLevel - SQL Exception " + sqlex.getMessage());
				if ((sqlex.getMessage().indexOf("serialize") != -1)
						|| (sqlex.getMessage().indexOf("deadlock") != -1)) {
					RollbackTransaction(con, sqlex,"tx stocklevel","r");
					if (resubmit) {
						InitTransaction(con, "tx stocklevel","r");
						continue;
					} else {
						throw sqlex;
					}
				} else {
					RollbackTransaction(con, sqlex,"tx stocklevel","r");
					throw sqlex;
				}
			} catch (java.lang.Exception ex) {
				logger.fatal("Unexpected error. Something bad happend");
				ex.printStackTrace(System.err);
				System.exit(-1);
			} finally {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
			}
			break;
		}
		return (dbtrace);
	}

    protected void InitTransaction(Connection con,
			String strTrans, String strAccess) throws java.sql.SQLException {
		Statement statement = null;
		try {
			Date NetStartTime = new java.util.Date();

			statement = con.createStatement();
			statement.execute("begin transaction");
			statement.execute("set transaction isolation level serializable");
			statement.execute("select '" + strTrans + "'");

			Date NetFinishTime = new java.util.Date();

			processLog(NetStartTime, NetFinishTime, "beginning", strAccess,
					strTrans);

		} catch (java.lang.Exception ex) {
			logger.fatal("Unexpected error. Something bad happend");
			ex.printStackTrace(System.err);
			System.exit(-1);
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}

	protected void CommitTransaction(Connection con, String strTrans,
			String strAccess) throws java.sql.SQLException {
		{
			Statement statement = null;
			try {

				Date NetStartTime = new java.util.Date();

				statement = con.createStatement();
				statement.execute("commit transaction");

				Date NetFinishTime = new java.util.Date();

				processLog(NetStartTime, NetFinishTime, "committing",
						strAccess, strTrans);

			} catch (java.sql.SQLException sqlex) {
				RollbackTransaction(con, sqlex, strTrans, strAccess);
				throw sqlex;
			} catch (java.lang.Exception ex) {
				logger.fatal("Unexpected error. Something bad happend");
				ex.printStackTrace(System.err);
				System.exit(-1);
			} finally {
				if (statement != null) {
					statement.close();
				}
			}
		}
	}

	protected void RollbackTransaction(Connection con, Exception dump,
			String strTrans, String strAccess) throws java.sql.SQLException {
		Statement statement = null;
		try {
			Date NetStartTime = new java.util.Date();

			statement = con.createStatement();
			statement.execute("rollback transaction");

			Date NetFinishTime = new java.util.Date();

			processLog(NetStartTime, NetFinishTime, "aborting", strAccess,
					strTrans);
		} catch (java.lang.Exception ex) {
			logger.fatal("Unexpected error. Something bad happend");
			ex.printStackTrace(System.err);
			System.exit(-1);
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}
}
// arch-tag: 5fdbe754-a4ea-4fa6-b768-7ce766ea5c82
