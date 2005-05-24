package escada.tpc.tpcc;

import escada.tpc.common.Emulation;
import escada.tpc.common.StateObject;
import escada.tpc.common.util.RandGen;
import escada.tpc.tpcc.database.transaction.dbTPCCDatabase;

/**
* It implements the states according to the definition  of the TPC-C. Basically, it sets up import information used in the execution of the new order transaction. Additionally, it defines the trace flag, which is a boolean value used to log traces or not and the trace file.
**/
public class NewOrderTrans
    extends StateObject {
  public void initProcess(Emulation em,String hid) {
    int wid = (em.getEmulationId() / 10) + 1;
    int did = 0;
    int cid = 0;
    int qtd = 0;
    boolean error = false;
    boolean localWarehouse = false;

    outInfo.putInfo("resubmit",Boolean.toString(Emulation.getStatusReSubmit()));
    outInfo.putInfo("trace", Emulation.getTraceInformation());
    outInfo.putInfo("abort", Integer.toString(0));
    outInfo.putInfo("hid", hid);
    
    outInfo.putInfo("wid", Integer.toString(wid));

    did = RandGen.nextInt(em.getRandom(), 1, TPCCConst.rngDistrict + 1);
    outInfo.putInfo("did", Integer.toString(did));

    cid = RandGen.NURand(em.getRandom(),
                         TPCCConst.CustomerA, TPCCConst.numINICustomer,
                         TPCCConst.numENDCustomer);
    outInfo.putInfo("cid", Integer.toString(cid));

    qtd = RandGen.nextInt(em.getRandom(), TPCCConst.qtdINIItem,
                          TPCCConst.qtdENDItem + 1);

    outInfo.putInfo("qtd", Integer.toString(qtd));

    if (RandGen.nextInt(em.getRandom(), TPCCConst.rngABORTNewOrder + 1) ==
        TPCCConst.probABORTNewOrder) {
      error = true;
    }

    int i = 0;
    int iid = 0;
    int qtdi = 0;
    int supwid = 0;
    while (i < qtd) {
      iid = RandGen.NURand(em.getRandom(), TPCCConst.iidA, TPCCConst.numINIItem,
                           TPCCConst.numENDItem);
      qtdi = RandGen.nextInt(em.getRandom(), 1, TPCCConst.qtdItem + 1);
      if ( (error) && ( (i + 1) >= qtd)) {
        iid = 0;
        outInfo.putInfo("abort", Integer.toString(1));
      }
      outInfo.putInfo("iid" + i, Integer.toString(iid));
      outInfo.putInfo("qtdi" + i, Integer.toString(qtdi));
      if ( (RandGen.nextInt(em.getRandom(),
                            TPCCConst.rngNewOrderLOCALWarehouse + 1) <=
            TPCCConst.probNewOrderLOCALWarehouse) ||
          (Emulation.getNumberConcurrentEmulators() <= TPCCConst.numMinClients)) {
        outInfo.putInfo("supwid" + i, Integer.toString(wid));
      }
      else {
        supwid = RandGen.nextInt(em.getRandom(), 1,
                                 (Emulation.getNumberConcurrentEmulators() / 10) + 1);
        outInfo.putInfo("supwid" + i, Integer.toString(supwid));
        localWarehouse = false;
      }
      i++;
    }

    if (localWarehouse) {
      outInfo.putInfo("localwid", Integer.toString(1));
    }
    else {
      outInfo.putInfo("localwid", Integer.toString(0));
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
      requestProcess = db.TraceNewOrderDB(outInfo,hid);
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
    prob = 45;
  }

  public void setKeyingTime() {
    keyingtime = 18;
  }

  public void setThinkTime() {
    thinktime = 12;
  }

  public String toString() {
    return ("NewOrderTrans");
  }
}// arch-tag: 74f56eb7-28a1-454b-8d3e-cb80a85557e3