package escada.tpc.tpcc.database.transaction.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashSet;

import org.apache.log4j.Logger;

import escada.tpc.common.OutInfo;
import escada.tpc.tpcc.database.transaction.*;

/**
 * It is an interface to a postgreSQL, which based is based on the the
 * distributions of the TPC-C.
 */
public class dbPostgresqlConservative extends dbTPCCDatabase {

	private static Logger logger = Logger.getLogger(dbPostgresqlConservative.class);

	protected HashSet NewOrderDB(OutInfo obj, Connection con)
			throws java.sql.SQLException {

		boolean resubmit = Boolean.parseBoolean((String) obj
				.getInfo("resubmit"));
		HashSet dbtrace = new HashSet();

		while (true) {
			PreparedStatement statement = null;
			Statement controlStatement = null;
			ResultSet rs = null;
			String cursor = null;

			try {

				Date NetStartTime = new java.util.Date();
				
				controlStatement = con.createStatement();
				controlStatement.execute("select tg_conservative ('district,stock,orders,new_order,order_line,item,warehouse,customer')");
				System.exit(-1);

				statement = con
						.prepareCall("select tpcc_neworder (?,?,?,?,?,?,?,?)");

				statement.setInt(1, Integer.parseInt((String) obj
						.getInfo("wid")));
				statement.setInt(2, Integer.parseInt((String) obj
						.getInfo("did")));
				statement.setInt(3, Integer.parseInt((String) obj
						.getInfo("cid")));
				statement.setInt(4, Integer.parseInt((String) obj
						.getInfo("qtd")));
				statement.setInt(5, Integer.parseInt((String) obj
						.getInfo("localwid")));

				int icont = 0;
				int qtd = Integer.parseInt((String) obj.getInfo("qtd"));
				StringBuffer iid = new StringBuffer();
				StringBuffer wid = new StringBuffer();
				StringBuffer qtdi = new StringBuffer();
				while (icont < qtd) {
					iid.append((String) obj.getInfo("iid" + icont));
					iid.append(",");
					wid.append((String) obj.getInfo("supwid" + icont));
					wid.append(",");
					qtdi.append((String) obj.getInfo("qtdi" + icont));
					qtdi.append(",");
					icont++;
				}
				statement.setString(6, iid.toString());
				statement.setString(7, wid.toString());
				statement.setString(8, qtdi.toString());

				rs = statement.executeQuery();

				if (rs.next()) {
					cursor = (String) rs.getString(1);
				}
				rs.close();
				rs = null;
				statement.close();
				statement = null;
				statement = con.prepareStatement("fetch all in \"" + cursor
						+ "\"");
				rs = statement.executeQuery();

				while (rs.next()) {
					dbtrace.add(rs.getString(1));
				}
				rs.close();
				rs = null;
				statement.close();
				statement = null;

				Date NetFinishTime = new java.util.Date();

				processLog(NetStartTime, NetFinishTime, "processing", "w",
						"tx neworder");

			} catch (java.sql.SQLException sqlex) {
				logger.warn("NewOrder - SQL Exception " + sqlex.getMessage());
				if ((sqlex.getMessage().indexOf("serialize") != -1)
						|| (sqlex.getMessage().indexOf("deadlock") != -1)) {
					RollbackTransaction(con, sqlex, "tx neworder", "w");
					if (resubmit) {
						InitTransaction(con, "tx neworder", "w");
						continue;
					} else {
						throw sqlex;
					}
				} else {
					RollbackTransaction(con, sqlex, "tx neworder", "w");
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

	protected HashSet DeliveryDB(OutInfo obj, Connection con)
			throws java.sql.SQLException {

		boolean resubmit = Boolean.parseBoolean((String) obj
				.getInfo("resubmit"));
		HashSet dbtrace = new HashSet();

		while (true) {

			PreparedStatement statement = null;
			Statement controlStatement = null;
			ResultSet rs = null;
			String cursor = null;
			
			try {
				Date NetStartTime = new java.util.Date();

				controlStatement = con.createStatement();
				controlStatement.execute("select tg_conservative('orders,new_order,order_line,customer')");

				statement = con.prepareStatement("select tpcc_delivery(?,?)");

				statement.setInt(1, Integer.parseInt((String) obj
						.getInfo("wid")));
				statement.setInt(2, Integer.parseInt((String) obj
						.getInfo("crid")));
				rs = statement.executeQuery();

				if (rs.next()) {
					cursor = (String) rs.getString(1);
				}
				rs.close();
				rs = null;
				statement.close();
				statement = null;
				statement = con.prepareStatement("fetch all in \"" + cursor
						+ "\"");
				rs = statement.executeQuery();

				while (rs.next()) {
					dbtrace.add(rs.getString(1));
				}
				rs.close();
				rs = null;
				statement.close();
				statement = null;

				Date NetFinishTime = new java.util.Date();

				processLog(NetStartTime, NetFinishTime, "processing", "w",
						"tx delivery");

			} catch (java.sql.SQLException sqlex) {
				logger.warn("Delivery - SQL Exception " + sqlex.getMessage());
				if ((sqlex.getMessage().indexOf("serialize") != -1)
						|| (sqlex.getMessage().indexOf("deadlock") != -1)) {
					RollbackTransaction(con, sqlex, "tx delivery", "w");
					if (resubmit) {
						InitTransaction(con, "tx delivery", "w");
						continue;
					} else {
						throw sqlex;
					}
				} else {
					RollbackTransaction(con, sqlex, "tx delivery", "w");
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

	protected HashSet OrderStatusDB(OutInfo obj, Connection con)
			throws java.sql.SQLException {

		boolean resubmit = Boolean.parseBoolean((String) obj
				.getInfo("resubmit"));
		HashSet dbtrace = new HashSet();

		while (true) {
			PreparedStatement statement = null;
			Statement controlStatement = null;
			ResultSet rs = null;
			String cursor = null;

			try {
				Date NetStartTime = new java.util.Date();
				
				controlStatement = con.createStatement();
				controlStatement.execute("select conservative ('orders,order_line,customer')");
				

				statement = con
						.prepareStatement("select tpcc_orderstatus(?,?,?,?)");

				statement.setInt(1, Integer.parseInt((String) obj
						.getInfo("wid")));
				statement.setInt(2, Integer.parseInt((String) obj
						.getInfo("did")));
				statement.setInt(3, Integer.parseInt((String) obj
						.getInfo("cid")));
				statement.setString(4, (String) obj.getInfo("lastname"));
				rs = statement.executeQuery();

				if (rs.next()) {
					cursor = (String) rs.getString(1);
				}
				rs.close();
				rs = null;
				statement.close();
				statement = null;
				statement = con.prepareStatement("fetch all in \"" + cursor
						+ "\"");
				rs = statement.executeQuery();

				while (rs.next()) {
					dbtrace.add(rs.getString(1));
				}
				rs.close();
				rs = null;
				statement.close();
				statement = null;

				Date NetFinishTime = new java.util.Date();

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
						RollbackTransaction(con, sqlex, "tx orderstatus 01",
								"r");
					} else {
						RollbackTransaction(con, sqlex, "tx orderstatus 02",
								"r");
					}

					if (resubmit) {
						if (str.equals("0")) {
							InitTransaction(con, "tx orderstatus 01", "r");
						} else {
							InitTransaction(con, "tx orderstatus 02", "r");
						}
						continue;
					} else {
						throw sqlex;
					}
				} else {
					if (str.equals("0")) {
						RollbackTransaction(con, sqlex, "tx orderstatus 01",
								"r");
					} else {
						RollbackTransaction(con, sqlex, "tx orderstatus 02",
								"r");
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

	protected HashSet PaymentDB(OutInfo obj, Connection con)
			throws java.sql.SQLException {

		boolean resubmit = Boolean.parseBoolean((String) obj
				.getInfo("resubmit"));
		HashSet dbtrace = new HashSet();

		while (true) {
			PreparedStatement statement = null;
			Statement controlStatement = null;
			ResultSet rs = null;
			String cursor = null;

			try {
				Date NetStartTime = new java.util.Date();
				
				controlStatement = con.createStatement();
				controlStatement.execute("select tg_conservative('warehouse,district,customer,history')");

				statement = con
						.prepareStatement("select tpcc_payment(?,?,cast(? as numeric(6,2)),?,?,?,cast(? as char(16)))");

				statement.setInt(1, Integer.parseInt((String) obj
						.getInfo("wid")));
				statement.setInt(2, Integer.parseInt((String) obj
						.getInfo("cwid")));
				statement.setFloat(3, Float.parseFloat((String) obj
						.getInfo("hamount")));
				statement.setInt(4, Integer.parseInt((String) obj
						.getInfo("did")));
				statement.setInt(5, Integer.parseInt((String) obj
						.getInfo("cdid")));
				statement.setInt(6, Integer.parseInt((String) obj
						.getInfo("cid")));
				statement.setString(7, (String) obj.getInfo("lastname"));

				rs = statement.executeQuery();

				if (rs.next()) {
					cursor = (String) rs.getString(1);
				}
				rs.close();
				rs = null;
				statement.close();
				statement = null;
				statement = con.prepareStatement("fetch all in \"" + cursor
						+ "\"");
				rs = statement.executeQuery();

				while (rs.next()) {
					dbtrace.add(rs.getString(1));
				}
				rs.close();
				rs = null;
				statement.close();
				statement = null;

				Date NetFinishTime = new java.util.Date();

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
						RollbackTransaction(con, sqlex, "tx payment 01", "w");
					} else {
						RollbackTransaction(con, sqlex, "tx payment 02", "w");
					}

					if (resubmit) {

						if (str.equals("0")) {
							InitTransaction(con, "tx payment 01", "w");
						} else {
							InitTransaction(con, "tx payment 02", "w");
						}
						continue;
					} else {
						throw sqlex;
					}
				} else {
					if (str.equals("0")) {
						RollbackTransaction(con, sqlex, "tx payment 01", "w");
					} else {
						RollbackTransaction(con, sqlex, "tx payment 02", "w");
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

	protected HashSet StockLevelDB(OutInfo obj, Connection con)
			throws java.sql.SQLException {

		boolean resubmit = Boolean.parseBoolean((String) obj
				.getInfo("resubmit"));
		HashSet dbtrace = new HashSet();

		while (true) {
			PreparedStatement statement = null;
			Statement controlStatement = null;
			ResultSet rs = null;
			String cursor = null;

			try {
				Date NetStartTime = new java.util.Date();
				
				controlStatement = con.createStatement();
				controlStatement.execute("select tg_conservative('district,stock,order_line')");

				statement = con
						.prepareStatement("select tpcc_stocklevel(?,?,?)");

				statement.setInt(1, Integer.parseInt((String) obj
						.getInfo("wid")));
				statement.setInt(2, Integer.parseInt((String) obj
						.getInfo("did")));
				statement.setInt(3, Integer.parseInt((String) obj
						.getInfo("threshhold")));
				rs = statement.executeQuery();

				if (rs.next()) {
					cursor = (String) rs.getString(1);
				}
				rs.close();
				rs = null;
				statement.close();
				statement = null;
				statement = con.prepareStatement("fetch all in \"" + cursor
						+ "\"");
				rs = statement.executeQuery();

				while (rs.next()) {
					dbtrace.add(rs.getString(1));
				}
				rs.close();
				rs = null;
				statement.close();
				statement = null;

				Date NetFinishTime = new java.util.Date();

				processLog(NetStartTime, NetFinishTime, "processing", "r",
						"tx stocklevel");

			} catch (java.sql.SQLException sqlex) {
				logger.warn("StockLevel - SQL Exception " + sqlex.getMessage());
				if ((sqlex.getMessage().indexOf("serialize") != -1)
						|| (sqlex.getMessage().indexOf("deadlock") != -1)) {
					RollbackTransaction(con, sqlex, "tx stocklevel", "r");
					if (resubmit) {
						InitTransaction(con, "tx stocklevel", "r");
						continue;
					} else {
						throw sqlex;
					}
				} else {
					RollbackTransaction(con, sqlex, "tx stocklevel", "r");
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

	protected void RollbackTransaction(Connection con, java.lang.Exception dump,
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
// arch-tag: e3dade63-ee1f-4e59-847c-205abebe3048
