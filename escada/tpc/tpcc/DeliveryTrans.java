package escada.tpc.tpcc;

import escada.tpc.common.Emulation;
import escada.tpc.common.StateObject;
import escada.tpc.common.util.RandGen;
import escada.tpc.tpcc.database.transaction.dbTPCCDatabase;

/**
 * It implements the states according to the definition of the TPC-C. Basically,
 * it setups import information used in the execution of the delivery
 * transaction. Additionally, it defines the trace flag, which is a boolean
 * value used to log traces or not and the trace file.
 */
public class DeliveryTrans extends StateObject {
	public void initProcess(Emulation em, String hid) {
		int wid = (em.getEmulationId() / 10) + 1; // TODO: CHANGE TO CONSTANT
		int crid = 0;

		outInfo.put("resubmit", Boolean.toString(Emulation
				.getStatusReSubmit()));
		outInfo.put("trace", Emulation.getTraceInformation());
		outInfo.put("abort", "0");
		outInfo.put("hid", hid);

		outInfo.put("wid", Integer.toString(wid));
		crid = RandGen.nextInt(em.getRandom(), 1, TPCCConst.rngCarrier + 1);
		outInfo.put("crid", Integer.toString(crid));
		outInfo.put("thinktime", Long.toString(em.getThinkTime()));
		outInfo.put("file", em.getEmulationName());
	}

	public void prepareProcess(Emulation em, String hid) {

	}

	public Object requestProcess(Emulation em, String hid) {
		Object requestProcess = null;
		dbTPCCDatabase db = (dbTPCCDatabase) em.getDatabase();
		try {
			initProcess(em, hid);
			requestProcess = db.TraceDeliveryDB(outInfo, hid);
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return (requestProcess);
	}

	public void postProcess(Emulation em, String hid) {
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
