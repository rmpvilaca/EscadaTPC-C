package escada.tpc.tpcc;

import java.sql.SQLException;

import escada.tpc.common.Emulation;
import escada.tpc.common.StateObject;
import escada.tpc.common.TPCConst;
import escada.tpc.common.util.RandGen;
import escada.tpc.tpcc.database.transaction.dbTPCCDatabase;

/**
 * It implements the states according to the definition of the TPC-C. Basically,
 * it setups import information used in the execution of the delivery
 * transaction. Additionally, it defines the trace flag, which is a boolean
 * value used to log traces or not and the trace file.
 */
public class DeliveryTrans extends StateObject {
	public void initProcess(Emulation em, String hid) throws SQLException {
		int wid = (em.getEmulationId() / TPCConst.getNumMinClients()) + 1;
		int crid = 0;

		
		System.out.println("Accessing warehouse " + wid);
		
		outInfo
				.put("resubmit", Boolean
						.toString(em.getStatusReSubmit()));
		outInfo.put("trace", em.getTraceInformation());
		outInfo.put("abort", "0");
		outInfo.put("hid", hid);

		outInfo.put("wid", Integer.toString(wid));
		crid = RandGen.nextInt(em.getRandom(), 1, TPCCConst.rngCarrier + 1);
		outInfo.put("crid", Integer.toString(crid));
		outInfo.put("thinktime", Long.toString(em.getThinkTime()));
		outInfo.put("file", em.getEmulationName());
	}

	public void prepareProcess(Emulation em, String hid) throws SQLException {

	}

	public Object requestProcess(Emulation em, String hid) throws SQLException {
		Object requestProcess = null;
		dbTPCCDatabase db = (dbTPCCDatabase) em.getDatabase();
		initProcess(em, hid);
		requestProcess = db.TraceDeliveryDB(outInfo, hid);

		return (requestProcess);
	}

	public void postProcess(Emulation em, String hid) throws SQLException {
		inInfo.clear();
		outInfo.clear();
	}

	public void setProb() {
		prob = 4;
	}

	public void setKeyingTime() {
		keyingtime = 2;
	}

	public void setThinkTime() {
		thinktime = 5;
	}

	public String toString() {
		return ("DeliveryTrans");
	}
}
// arch-tag: 945bcc5f-b3d7-4f5a-8322-dd470a3fe18f
