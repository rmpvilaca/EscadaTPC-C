package Escada.tpc.tpcc;


/**
* It defines important constants used in the simulation of the TPC-C.
**/
public class TPCCConst {
  public final static int CustomerA = 1023;
  public final static int numINICustomer = 1;
  public final static int numENDCustomer = 3000;

  public final static int qtdINIItem = 5;
  public final static int qtdENDItem = 15;
  public final static int qtdItem = 10;

  public final static int iidA = 8191;
  public final static int numINIItem = 1;
  public final static int numENDItem = 100000;

  public final static int rngCarrier = 10;

  public final static int rngDistrict = 10;

  public final static int probABORTNewOrder = 1;
  public final static int rngABORTNewOrder = 100;

  public final static int probNewOrderLOCALWarehouse = 99;
  public final static int rngNewOrderLOCALWarehouse = 100;

  public final static int probPaymentLOCALWarehouse = 85;
  public final static int rngPaymentLOCALWarehouse = 100;

  public final static int probLASTNAME = 60;
  public final static int rngLASTNAME = 100;
  public final static int LastNameA = 255;
  public final static int numINILastName = 0;
  public final static int numENDLastName = 999;

  public final static int numINIAmount = 100;
  public final static int numENDAmount = 500000;
  public final static int numDIVAmount = 100;

  public final static int numMinClients = 10;

  public final static int numINIThreshHold = 10;
  public final static int numENDThreshHold = 20;

  public final static int numState = 5;
}
// arch-tag: fe63e7a2-dd6e-4431-ad74-35de8fdb5bf1
// arch-tag: fd309368-dcab-49d3-8807-f64a57ae6db4
// arch-tag: a74dbe12-4f5e-4f6d-83f4-36eee7976f3c
