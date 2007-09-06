package escada.tpc.cluster;

import escada.tpc.cluster.database.transaction.dbCLUSTERDatabase;
import escada.tpc.common.Emulation;
import escada.tpc.common.StateObject;
import escada.tpc.common.util.RandGen;

/**
 * It implements the states according to the definition of the TPC-C. Basically,
 * it setups import information used in the execution of the delivery
 * transaction. Additionally, it defines the trace flag, which is a boolean
 * value used to log traces or not and the trace file.
 */
public class ReadOnlyTrans extends StateObject {

	public void initProcess(Emulation em, String hid) {
		outInfo
				.put("resubmit", Boolean
						.toString(Emulation.getStatusReSubmit()));
		outInfo.put("trace", Emulation.getTraceInformation());
		outInfo.put("abort", "0");
		outInfo.put("hid", hid);

		int crid = RandGen.nextInt(em.getRandom(), 1,
				CLUSTERConst.rngCarrier + 1);
		outInfo.put("crid", Integer.toString(crid));
		outInfo.put("thinktime", Long.toString(em.getThinkTime()));
		outInfo.put("file", em.getEmulationName());
	}

	public void prepareProcess(Emulation em, String hid) {

	}

	public Object requestProcess(Emulation em, String hid) {
		Object requestProcess = null;
		dbCLUSTERDatabase db = (dbCLUSTERDatabase) em.getDatabase();
		try {
			initProcess(em, hid);
			requestProcess = db.TraceReadOnlyTrans(outInfo, hid);
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
		prob = 50;
	}

	public void setKeyingTime() {
		keyingtime = 2;
	}

	public void setThinkTime() {
		thinktime = 5;
	}

	public String toString() {
		return ("UpdateTrans");
	}
}
// arch-tag: eec7ff6d-ef63-42e2-a681-bed438d987d6
