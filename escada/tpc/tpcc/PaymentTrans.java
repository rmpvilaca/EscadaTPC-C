package escada.tpc.tpcc;

import escada.tpc.common.Emulation;
import escada.tpc.common.StateObject;
import escada.tpc.common.util.RandGen;
import escada.tpc.tpcc.database.dbTPCCDatabase;
import escada.tpc.tpcc.util.TPCCRandGen;

/**
* It implements the states according to the definition  of the TPC-C. Basically, it sets up import information used in the execution of the payment transaction. Additionally, it defines the trace flag, which is a boolean value used to log traces or not and the trace file.
**/
public class PaymentTrans
    extends StateObject {
  public void initProcess(Emulation em,String hid) {
    int wid = (em.getEmulationId() / 10) + 1;
    int cid = 0;
    int did = 0;
    int cwid = 0;
    int cdid = 0;
    String lastname = null;
    float hamount = 0;

    outInfo.putInfo("trace", Emulation.getTraceInformation());
    outInfo.putInfo("abort", "0");
    outInfo.putInfo("wid", Integer.toString(wid));
    did = RandGen.nextInt(em.getRandom(), 1, TPCCConst.rngDistrict + 1);
    outInfo.putInfo("did", Integer.toString(did));

    if (RandGen.nextInt(em.getRandom(), TPCCConst.rngLASTNAME + 1) <=
        TPCCConst.probLASTNAME) {
      lastname = TPCCRandGen.digSyl(RandGen.NURand(em.getRandom(),
          TPCCConst.LastNameA,
          TPCCConst.numINILastName,
          TPCCConst.numENDLastName));
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

    if ( (RandGen.nextInt(em.getRandom(),
                          TPCCConst.rngPaymentLOCALWarehouse + 1) <=
          TPCCConst.probPaymentLOCALWarehouse) ||
        (Emulation.getNumberConcurrentEmulators() <= TPCCConst.numMinClients)) {
      outInfo.putInfo("cwid", Integer.toString(wid));
      outInfo.putInfo("cdid", Integer.toString(did));
    }
    else {
      cdid = RandGen.nextInt(em.getRandom(), 1, TPCCConst.rngDistrict + 1);
      outInfo.putInfo("cdid", Integer.toString(cdid));
      cwid = RandGen.nextInt(em.getRandom(), 1,
                             (Emulation.getNumberConcurrentEmulators() / 10) + 1);
      outInfo.putInfo("cwid", Integer.toString(cwid));
    }

    hamount =
        (float) RandGen.nextInt(em.getRandom(), TPCCConst.numINIAmount,
                                TPCCConst.numENDAmount + 1) /
        (float) TPCCConst.numDIVAmount;
    outInfo.putInfo("hamount", Float.toString(hamount));
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
      requestProcess = db.TracePaymentDB(outInfo,hid);
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
}// arch-tag: 903bfad5-d906-42fd-8d7c-cc9051459c7d
// arch-tag: e24ea8d7-b0d2-40df-ab64-c35cc4dc2f22
// arch-tag: ec1d6a90-1685-4abb-b672-7c822244936b
