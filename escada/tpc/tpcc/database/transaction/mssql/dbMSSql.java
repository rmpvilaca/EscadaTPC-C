/*
 * TPCC Client
 * Copyright (C) 2006 University of Minho
 * See http://gorda.di.uminho.pt/ for more information.
 *
 * Partially funded by the European Union Framework Programme for
 * Research and Technological Development, thematic priority
 * Information Society and Media, project GORDA (004758).
 * 
 * Contributors:
 *  - Rui Oliveira <rco@di.uminho.pt>
 *  - Jose Orlando Pereira <jop@di.uminho.pt>
 *  - Antonio Luis Sousa <als@di.uminho.pt>
 *  - Alfranio Tavares Correia Junior <alfranio@lsd.di.uminho.pt> 
 *  - Luis Soares <los@di.uminho.pt>
 *  - Ricardo Manuel Pereira Vilaca <rmvilaca@di.uminho.pt>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
 * USA.
 */


package escada.tpc.tpcc.database.transaction.mssql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Properties;

import org.apache.log4j.Logger;

import escada.tpc.tpcc.database.transaction.dbTPCCDatabase;

/**
 * It is an interface to a postgreSQL, which based is based on the the
 * distributions of the TPC-C.
 */
public class dbMSSql extends dbTPCCDatabase {

	private Logger logger = Logger.getLogger(dbMSSql.class);

	protected HashSet NewOrderDB(Properties obj, Connection con)
			throws java.sql.SQLException {

		boolean resubmit = Boolean.parseBoolean((String) obj
				.get("resubmit"));
		HashSet dbtrace = new HashSet();

		while (true) {

			CallableStatement statement = null;
			ResultSet rs = null;
			String cursor = null;

			java.util.Date NetStartTime = null;
			java.util.Date NetFinishTime = null;
			NetStartTime = new java.util.Date();

			try {
				statement = con
						.prepareCall("{call tpcc_neworder(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

				statement.setInt(1, Integer.parseInt((String) obj
						.get("wid")));
				statement.setInt(2, Integer.parseInt((String) obj
						.get("did")));
				statement.setInt(3, Integer.parseInt((String) obj
						.get("cid")));
				statement.setInt(4, Integer.parseInt((String) obj
						.get("qtd")));
				statement.setInt(5, Integer.parseInt((String) obj
						.get("localwid")));

				int icont = 0;
				int desParam = 6;
				int qtdTotal = 15;
				int qtd = Integer.parseInt((String) obj.get("qtd"));

				while (icont < qtd) {
					statement.setInt(desParam, Integer.parseInt((String) obj
							.get("iid" + icont)));
					statement.setInt(desParam + 1, Integer
							.parseInt((String) obj.get("supwid" + icont)));
					statement.setInt(desParam + 2, Integer
							.parseInt((String) obj.get("qtdi" + icont)));
					icont++;
					desParam = desParam + 3;
				}
				while (icont < qtdTotal) {
					statement.setInt(desParam, 0);
					statement.setInt(desParam + 1, 0);
					statement.setInt(desParam + 2, 0);
					icont++;
					desParam = desParam + 3;
				}
				rs = statement.executeQuery();

				while (rs.next()) {
					dbtrace.add(rs.getString(1));
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

	protected HashSet DeliveryDB(Properties obj, Connection con)
			throws java.sql.SQLException {
		boolean resubmit = Boolean.parseBoolean((String) obj
				.get("resubmit"));
		HashSet dbtrace = new HashSet();

		while (true) {
			CallableStatement statement = null;
			ResultSet rs = null;
			String cursor = null;

			java.util.Date NetStartTime = null;
			java.util.Date NetFinishTime = null;
			NetStartTime = new java.util.Date();

			try {
				statement = con.prepareCall("{call tpcc_delivery(?,?)}");

				statement.setInt(1, Integer.parseInt((String) obj
						.get("wid")));
				statement.setInt(2, Integer.parseInt((String) obj
						.get("crid")));
				rs = statement.executeQuery();

				while (rs.next()) {
					dbtrace.add(rs.getString(1));
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

	protected HashSet OrderStatusDB(Properties obj, Connection con)
			throws java.sql.SQLException {
		boolean resubmit = Boolean.parseBoolean((String) obj
				.get("resubmit"));
		HashSet dbtrace = new HashSet();

		while (true) {
			CallableStatement statement = null;
			ResultSet rs = null;
			String cursor = null;

			java.util.Date NetStartTime = null;
			java.util.Date NetFinishTime = null;
			NetStartTime = new java.util.Date();

			try {
				statement = con.prepareCall("{call tpcc_orderstatus(?,?,?,?)}");

				statement.setInt(1, Integer.parseInt((String) obj
						.get("wid")));
				statement.setInt(2, Integer.parseInt((String) obj
						.get("did")));
				statement.setInt(3, Integer.parseInt((String) obj
						.get("cid")));
				statement.setString(4, (String) obj.get("lastname"));
				rs = statement.executeQuery();

				while (rs.next()) {
					dbtrace.add(rs.getString(1));
				}
				rs.close();
				rs = null;
				statement.close();
				statement = null;
				NetFinishTime = new java.util.Date();

				String str = (String) (obj).get("cid");

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
				String str = (String) (obj).get("cid");
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

	protected HashSet PaymentDB(Properties obj, Connection con)
			throws java.sql.SQLException {
		boolean resubmit = Boolean.parseBoolean((String) obj
				.get("resubmit"));
		HashSet dbtrace = new HashSet();

		while (true) {
			CallableStatement statement = null;
			ResultSet rs = null;
			String cursor = null;

			java.util.Date NetStartTime = null;
			java.util.Date NetFinishTime = null;
			NetStartTime = new java.util.Date();

			try {
				statement = con
						.prepareCall("{call tpcc_payment(?,?,?,?,?,?,?)}");

				statement.setInt(1, Integer.parseInt((String) obj
						.get("wid")));
				statement.setInt(2, Integer.parseInt((String) obj
						.get("cwid")));
				statement.setFloat(3, Float.parseFloat((String) obj
						.get("hamount")));
				statement.setInt(4, Integer.parseInt((String) obj
						.get("did")));
				statement.setInt(5, Integer.parseInt((String) obj
						.get("cdid")));
				statement.setInt(6, Integer.parseInt((String) obj
						.get("cid")));
				statement.setString(7, (String) obj.get("lastname"));

				rs = statement.executeQuery();

				while (rs.next()) {
					dbtrace.add(rs.getString(1));
				}
				rs.close();
				rs = null;
				statement.close();
				statement = null;

				String str = (String) (obj).get("cid");
				if (str.equals("0")) {
					processLog(NetStartTime, NetFinishTime, "processing", "w",
							"tx payment 01");
				} else {
					processLog(NetStartTime, NetFinishTime, "processing", "w",
							"tx payment 02");
				}

			} catch (java.sql.SQLException sqlex) {
				logger.warn("Payment - SQL Exception " + sqlex.getMessage());

				String str = (String) (obj).get("cid");
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

	protected HashSet StockLevelDB(Properties obj, Connection con)
			throws java.sql.SQLException {
		boolean resubmit = Boolean.parseBoolean((String) obj
				.get("resubmit"));
		HashSet dbtrace = new HashSet();

		while (true) {
			CallableStatement statement = null;
			ResultSet rs = null;
			String cursor = null;

			java.util.Date NetStartTime = null;
			java.util.Date NetFinishTime = null;
			NetStartTime = new java.util.Date();

			try {
				statement = con.prepareCall("{call tpcc_stocklevel(?,?,?)}");

				statement.setInt(1, Integer.parseInt((String) obj
						.get("wid")));
				statement.setInt(2, Integer.parseInt((String) obj
						.get("did")));
				statement.setInt(3, Integer.parseInt((String) obj
						.get("threshhold")));
				rs = statement.executeQuery();

				while (rs.next()) {
					dbtrace.add(rs.getString(1));
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

	protected void InitTransaction(Connection con, String strTrans,
			String strAccess) throws java.sql.SQLException {

	}

	protected void CommitTransaction(Connection con, String strTrans,
			String strAccess) throws java.sql.SQLException {

	}

	protected void RollbackTransaction(Connection con, Exception dump,
			String strTrans, String strAccess) throws java.sql.SQLException {
	}
}
// arch-tag: 96ed9699-68a0-4515-bd5f-be5a51f4c369
