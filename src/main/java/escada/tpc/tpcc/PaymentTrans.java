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
 * it sets up import information used in the execution of the payment
 * transaction. Additionally, it defines the trace flag, which is a boolean
 * value used to log traces or not and the trace file.
 */
public class PaymentTrans extends StateObject {
	public void initProcess(Emulation em, String hid) throws SQLException {
		int wid = (em.getEmulationId() / TPCConst.getNumMinClients()) + 1;
		int cid = 0;
		int did = 0;
		int cwid = 0;
		int cdid = 0;
		String lastname = null;
		float hamount = 0;

		outInfo.put("trace", em.getTraceInformation());
		outInfo
				.put("resubmit", Boolean
						.toString(em.getStatusReSubmit()));
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
					TPCCConst.numINICustomer, TPCCConst.getNumCustomer());
			outInfo.put("cid", Integer.toString(cid));
			outInfo.put("lastname", "");
		}

		if ((RandGen.nextInt(em.getRandom(),
				TPCCConst.rngPaymentLOCALWarehouse + 1) <= TPCCConst.probPaymentLOCALWarehouse)
				|| (em.getNumberConcurrentEmulators() <= TPCConst.getNumMinClients())) {
			outInfo.put("cwid", Integer.toString(wid));
			outInfo.put("cdid", Integer.toString(did));
		} else {
			cdid = RandGen
					.nextInt(em.getRandom(), 1, TPCCConst.getNumDistrict() + 1);
			outInfo.put("cdid", Integer.toString(cdid));
			cwid = RandGen.nextInt(em.getRandom(), 1, (em
					.getNumberConcurrentEmulators() / TPCConst.getNumMinClients()) + 1);
			outInfo.put("cwid", Integer.toString(cwid));
		}

		hamount = (float) RandGen.nextInt(em.getRandom(),
				TPCCConst.numINIAmount, TPCCConst.numENDAmount + 1)
				/ (float) TPCCConst.numDIVAmount;
		outInfo.put("hamount", Float.toString(hamount));
		outInfo.put("thinktime", Long.toString(em.getThinkTime()));
		outInfo.put("file", em.getEmulationName());
	}

	public void prepareProcess(Emulation em, String hid) throws SQLException {

	}

	public Object requestProcess(Emulation em, String hid) throws SQLException {
		Object requestProcess = null;
		dbTPCCDatabase db = (dbTPCCDatabase) em.getDatabase();
		initProcess(em, hid);
		requestProcess = db.TracePaymentDB(outInfo, hid);

		return (requestProcess);
	}

	public void postProcess(Emulation em, String hid) throws SQLException {
		inInfo.clear();
		outInfo.clear();
	}

	public void setProb() {
		prob = 43;
	}

	public void setKeyingTime() {
		keyingtime = 3;
	}

	public void setThinkTime() {
		thinktime = 12;
	}

	public String toString() {
		return ("PaymentTrans");
	}
}
// arch-tag: 903bfad5-d906-42fd-8d7c-cc9051459c7d

