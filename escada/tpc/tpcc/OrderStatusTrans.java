package escada.tpc.tpcc;

import escada.tpc.common.Emulation;
import escada.tpc.common.StateObject;
import escada.tpc.common.util.RandGen;
import escada.tpc.tpcc.database.transaction.dbTPCCDatabase;
import escada.tpc.tpcc.util.TPCCRandGen;

/**
* It implements the states according to the definition  of the TPC-C. Basically, it sets up import information used in the execution of the order status transaction. Additionally, it defines the trace flag, which is a boolean value used to log traces or not and the trace file.
**/
public class OrderStatusTrans
    extends StateObject {
  public void initProcess(Emulation em,String hid) {
    int wid = (em.getEmulationId() / 10) + 1; // TODO: Change to constant
    int cid = 0;
    int did = 0;
    String lastname = null;

    outInfo.putInfo("resubmit",Boolean.toString(Emulation.getStatusReSubmit()));
    outInfo.putInfo("trace", Emulation.getTraceInformation());
    outInfo.putInfo("abort", "0");
    outInfo.putInfo("wid", Integer.toString(wid));
    did = RandGen.nextInt(em.getRandom(), 1, TPCCConst.rngDistrict + 1);
    outInfo.putInfo("did", Integer.toString(did));

    if (RandGen.nextInt(em.getRandom(), TPCCConst.rngLASTNAME + 1) <=
        TPCCConst.probLASTNAME) {
      lastname = TPCCRandGen.digSyl(RandGen.NURand(em.getRandom(),
          TPCCConst.LastNameA,
          TPCCConst.numINILastName, TPCCConst.numENDLastName));
      outInfo.putInfo("lastname", lastname);
      outInfo.putInfo("cid", "0");
    }
    else {
      cid = RandGen.NURand(em.getRandom(),
                           TPCCConst.CustomerA, TPCCConst.numINICustomer,
                           TPCCConst.numENDCustomer);
      outInfo.putInfo("cid", Integer.toString(cid));
      outInfo.putInfo("lastname", "");
    }
    outInfo.putInfo("thinktime", Long.toString(em.getThinkTime()));
    outInfo.putInfo("file", em.getEmulationName());
  }

  public void prepareProcess(Emulation em,String hid) {
  }

  public Object requestProcess(Emulation em,String hid) {
    Object requestProcess = null;
    dbTPCCDatabase db = (dbTPCCDatabase) em.getDatabase();
    try {
      initProcess(em,hid);
      requestProcess = db.TraceOrderStatusDB(outInfo,hid);
    }
    catch (Exception ex) {
      ex.printStackTrace(System.err);
    }
    return (requestProcess);
  }

  public void postProcess(Emulation em,String hid) {
    inInfo.resetInfo();
    outInfo.resetInfo();
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
