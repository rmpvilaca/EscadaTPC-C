package escada.tpc.tpcc;

import escada.tpc.common.Emulation;
import escada.tpc.common.StateObject;
import escada.tpc.common.util.RandGen;
import escada.tpc.tpcc.database.dbTPCCDatabase;


/**
* It implements the states according to the definition  of the TPC-C. Basically, it setups import information used in the execution of the delivery transaction.
 Additionally, it defines the trace flag, which is a boolean value used to log traces or not and the trace file.
**/
public class DeliveryTrans
    extends StateObject {
  public void initProcess(Emulation em,String hid) {
    int wid = (em.getEmulationId() / 10) + 1;
    int crid = 0;

    outInfo.putInfo("trace", Emulation.getTraceInformation());
    outInfo.putInfo("abort", "0");
    outInfo.putInfo("wid", Integer.toString(wid));
    crid = RandGen.nextInt(em.getRandom(), 1, TPCCConst.rngCarrier + 1);
    outInfo.putInfo("crid", Integer.toString(crid));
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
      requestProcess =  db.TraceDeliveryDB(outInfo,hid);
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
    thinktime = 5;
  }

  public String toString() {
    return ("DeliveryTrans");
  }
}
// arch-tag: 945bcc5f-b3d7-4f5a-8322-dd470a3fe18f
