package escada.tpc.tpcc;

import java.sql.SQLException;

import escada.tpc.common.Emulation;
import escada.tpc.common.StateObject;
import escada.tpc.common.TPCConst;
import escada.tpc.common.util.RandGen;
import escada.tpc.tpcc.database.transaction.dbTPCCDatabase;
import escada.tpc.tpcc.util.TPCCRandGen;

/**
 * It implements the states according to the definition of the TPC-C. Basically,
 * it sets up import information used in the execution of the order status
 * transaction. Additionally, it defines the trace flag, which is a boolean
 * value used to log traces or not and the trace file.
 */
public class OrderStatusTrans extends StateObject {
	public void initProcess(Emulation em, String hid) throws SQLException {
		int wid = (em.getEmulationId() / TPCConst.getNumMinClients()) + 1; 
		int cid = 0;
		int did = 0;
		String lastname = null;
		
		System.out.println("Accessing warehouse " + wid);

		outInfo
				.put("resubmit", Boolean
						.toString(em.getStatusReSubmit()));
		outInfo.put("trace", em.getTraceInformation());
		outInfo.put("abort", "0");
		outInfo.put("hid", hid);

		outInfo.put("wid", Integer.toString(wid));
		did = RandGen.nextInt(em.getRandom(), 1, TPCCConst.getNumDistrict() + 1);
		outInfo.put("did", Integer.toString(did));

		if (RandGen.nextInt(em.getRandom(), TPCCConst.rngLASTNAME + 1) <= TPCCConst.probLASTNAME) {
			lastname = TPCCRandGen.digSyl(RandGen.NURand(em.getRandom(),
					TPCCConst.LastNameA, TPCCConst.numINILastName,
					TPCCConst.getNumLastName()));
			outInfo.put("lastname", lastname);
			outInfo.put("cid", "1");
		} else {
			cid = RandGen.NURand(em.getRandom(), TPCCConst.CustomerA,
					TPCCConst.numINICustomer, TPCCConst.getNumCustomer())+1;
			outInfo.put("cid", Integer.toString(cid));
			outInfo.put("lastname", "");
		}
		outInfo.put("thinktime", Long.toString(em.getThinkTime()));
		outInfo.put("file", em.getEmulationName());
	}

	public void prepareProcess(Emulation em, String hid) throws SQLException {
	}

	public Object requestProcess(Emulation em, String hid) throws SQLException {
		Object requestProcess = null;
		dbTPCCDatabase db = (dbTPCCDatabase) em.getDatabase();
		initProcess(em, hid);
		requestProcess = db.TraceOrderStatusDB(outInfo, hid);

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
		thinktime = 10;
	}

	public String toString() {
		return ("OrderStatusTrans");
	}
}
// arch-tag: 9851d047-714e-4a3f-a1db-a0913b0c0d60
