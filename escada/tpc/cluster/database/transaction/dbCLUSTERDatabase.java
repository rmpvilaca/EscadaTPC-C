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


package escada.tpc.cluster.database.transaction;

import java.sql.Connection;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;

import org.apache.log4j.Logger;

import escada.tpc.common.database.DatabaseManager;

abstract public class dbCLUSTERDatabase extends DatabaseManager {

	private static Logger logger = Logger.getLogger(dbCLUSTERDatabase.class);

	public Object TraceUpdateTrans(Properties obj, String hid)
			throws java.sql.SQLException {

		Connection con = null;
		HashSet dbtrace = null;

		try {
			logger.info("Beginning transaction update trans.");

			Date NetStartTime = new java.util.Date();

			con = getConnection();

			InitTransaction(con, "tx updatetrans", "w");

			dbtrace = UpdateTransDB(obj, con);

			CommitTransaction(con, "tx updatetrans", "w");

			Date NetFinishTime = new java.util.Date();

			processLog(NetStartTime, NetFinishTime, "commit", "w",
					"tx updatetrans");

			logger.info("Finishing transaction update trans.");
		} catch (java.sql.SQLException sqlex) {
			if ((sqlex.getMessage().indexOf("serialize") == -1)
					&& (sqlex.getMessage().indexOf("deadlock") == -1)
					&& (sqlex.getMessage().indexOf("not found") == -1)
					&& (sqlex.getMessage().indexOf("Generated Abort") == -1)) {
				logger.fatal("Unexpected error. Something bad happend");
				sqlex.printStackTrace(System.err);
				System.exit(-1);
			} else {
				if (sqlex.getMessage().indexOf("certification") != -1)
					logger.warn("UpdateTrans - SQL Exception "
							+ sqlex.getMessage());
			}
		} catch (java.lang.Exception ex) {
			logger.fatal("Unexpected error. Something bad happend");
			ex.printStackTrace(System.err);
			System.exit(-1);
		} finally {
			returnConnection(con);
		}
		return (dbtrace);
	}

	public Object TraceReadOnlyTrans(Properties obj, String hid)
			throws java.sql.SQLException {

		Connection con = null;
		HashSet dbtrace = null;

		try {
			logger.info("Beginning transaction readonly trans.");

			Date NetStartTime = new java.util.Date();

			con = getConnection();

			InitTransaction(con, "tx readonlytrans", "r");

			dbtrace = ReadOnlyTransDB(obj, con);

			CommitTransaction(con, "tx readonlytrans", "r");

			Date NetFinishTime = new java.util.Date();

			processLog(NetStartTime, NetFinishTime, "commit", "w",
					"tx readonlytrans");

			logger.info("Finishing transaction readonly trans.");
		} catch (java.sql.SQLException sqlex) {
			if ((sqlex.getMessage().indexOf("serialize") == -1)
					&& (sqlex.getMessage().indexOf("deadlock") == -1)
					&& (sqlex.getMessage().indexOf("not found") == -1)
					&& (sqlex.getMessage().indexOf("Generated Abort") == -1)) {
				logger.fatal("Unexpected error. Something bad happend");
				sqlex.printStackTrace(System.err);
				System.exit(-1);
			} else {
				if (sqlex.getMessage().indexOf("certification") != -1)
					logger.warn("ReadOnlyTrans - SQL Exception "
							+ sqlex.getMessage());
			}
		} catch (java.lang.Exception ex) {
			logger.fatal("Unexpected error. Something bad happend");
			ex.printStackTrace(System.err);
			System.exit(-1);
		} finally {
			returnConnection(con);
		}
		return (dbtrace);
	}

	protected abstract HashSet UpdateTransDB(Properties obj, Connection con)
			throws java.sql.SQLException;

	protected abstract HashSet ReadOnlyTransDB(Properties obj, Connection con)
			throws java.sql.SQLException;

	protected abstract void InitTransaction(Connection con, String strTrans,
			String strAccess) throws java.sql.SQLException;

	protected abstract void CommitTransaction(Connection con, String strTrans,
			String strAccess) throws java.sql.SQLException;

	protected abstract void RollbackTransaction(Connection con,
			java.lang.Exception dump, String strTrans, String strAccess)
			throws java.sql.SQLException;

}// arch-tag: b07c72e5-4bac-417e-a60b-1e38b6dd78cf

