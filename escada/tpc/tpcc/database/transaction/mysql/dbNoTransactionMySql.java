package escada.tpc.tpcc.database.transaction.mysql;

import java.sql.Connection;

import escada.tpc.common.OutInfo;

/**
 * It is an interface to a postgreSQL, which based is based on the the
 * distributions of the TPC-C.
 */
public class dbNoTransactionMySql extends dbTransactionMySql {

	protected void InitTransaction(OutInfo obj, Connection con,
			String transaction) throws java.sql.SQLException {
	}

	protected void CommitTransaction(Connection con)
			throws java.sql.SQLException {
	}

	protected void RollbackTransaction(Connection con, Exception dump)
			throws java.sql.SQLException {
	}
}
// arch-tag: 4f7d5bff-b83d-42b4-93ce-212da227b9ee
