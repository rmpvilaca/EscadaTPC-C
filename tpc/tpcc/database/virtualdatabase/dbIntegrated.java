package Escada.tpc.tpcc.database.virtualdatabase;

import Escada.tpc.tpcc.database.*;
import Escada.tpc.common.*;
import Escada.tpc.common.trace.*;

import java.util.*;

/** It is an interface to the a virtual database, which based on the
* the distributions of the TPC-C emulates information been retrieved
* and stored.
**/
public class dbIntegrated
    extends dbTPCCDatabase {

  public dbVirtualDatabase dbVirtual = null;

  /**
  * It instantiates this class, building a virtual database.
  *
  * @param int the total number of clients, which is used in the
  * distributions of the TPC-C.
  **/
  public dbIntegrated(int totcli) {
    dbVirtual = new dbVirtualDatabase(totcli);
  }

  public Object TraceNewOrderDB(OutInfo obj,String hid) throws java.sql.
      SQLException {
    Object TraceNewOrderDB = null;
    String tid = null;
    String wid = (String) obj.getInfo("wid");
    String did = (String) obj.getInfo("did");
    int iwid = Integer.parseInt(wid);
    int idid = Integer.parseInt(did);

    HashSet  dbtrace = new HashSet();
    HashSet tmptrace = new HashSet();

    try {
      tid = dbTrace.initTransactionTrace("tx neworder",
                                         (String) obj.getInfo("thinktime"),hid);
      dbtrace.add(wid + did);
      dbTrace.TransactionTrace(dbtrace, "r", tid, "district", iwid,hid);
      dbTrace.TransactionTrace(dbtrace, "w", tid, "district", iwid,hid);
      dbtrace.clear();

      int i = 0;
      int qtd = Integer.parseInt( (String) obj.getInfo("qtd"));
      while (i < qtd) {
        dbtrace.add(obj.getInfo("iid" + i));
        i++;
      }
      dbTrace.TransactionTrace(dbtrace, "r", tid, "item", iwid,hid);
      dbtrace.clear();

      i = 0;
      qtd = Integer.parseInt( (String) obj.getInfo("qtd"));
      while (i < qtd) {

        if (Integer.parseInt( (String) obj.getInfo("supwid" + i)) == iwid) {
          dbtrace.add( (String) obj.getInfo("supwid" + i) +
                      (String) obj.getInfo("iid" + i));
        }
        else {
          tmptrace.add( (String) obj.getInfo("supwid" + i) +
                       (String) obj.getInfo("iid" + i));
          dbTrace.TransactionTrace(tmptrace, "r", tid, "stock",
                                   Integer.
                                   parseInt( (String) obj.getInfo("supwid" +
              i)),hid);
          dbTrace.TransactionTrace(tmptrace, "w", tid, "stock",
                                   Integer.
                                   parseInt( (String) obj.getInfo("supwid" +
              i)),hid);
          tmptrace.clear();
        }

        i++;
      }
      dbTrace.TransactionTrace(dbtrace, "r", tid, "stock", iwid,hid);
      dbTrace.TransactionTrace(dbtrace, "w", tid, "stock", iwid,hid);
      dbtrace.clear();

      dbtrace.add(wid + did + (String) obj.getInfo("cid"));
      dbTrace.TransactionTrace(dbtrace, "r", tid, "customer", iwid,hid);
      dbtrace.clear();

      dbVirtual.insertOrders(wid, did);

      i = 0;
      qtd = Integer.parseInt( (String) obj.getInfo("qtd"));
      while (i < qtd) {
        dbtrace.add(wid + did + (String) dbVirtual.getCurrentOrders(wid, did) +
                    i);
        i++;
      }
      dbTrace.TransactionTrace(dbtrace, "w", tid, "orderline", iwid,hid);
      dbtrace.clear();

      dbtrace.add(wid + did + (String) dbVirtual.getCurrentOrders(wid, did));
      dbTrace.TransactionTrace(dbtrace, "w", tid, "orders", iwid,hid);
      dbTrace.TransactionTrace(dbtrace, "w", tid, "neworder", iwid,hid);
      dbtrace.clear();

      dbtrace.add(wid);
      dbTrace.TransactionTrace(dbtrace, "r", tid, "warehouse", iwid,hid);
      dbtrace.clear();

      if (Integer.parseInt((String)obj.getInfo("abort")) == 0 )
      {
         TraceNewOrderDB = dbTrace.closeTransactionTrace(tid,hid);
      }
      else
      {
         TraceNewOrderDB = dbTrace.closeErrorTransactionTrace(tid,hid);
      }
    }
    catch (java.lang.Exception ex) {
      ex.printStackTrace(System.err);
    }

    return (TraceNewOrderDB);

  }

  public Object TraceDeliveryDB(OutInfo obj,String hid) throws java.sql.
      SQLException {
    Object TraceDeliveryDB = null;
    String tid = null;
    String wid = (String) obj.getInfo("wid");
    int iwid = Integer.parseInt(wid);
    HashSet dbtrace = null;    
    
    try {
      tid = dbTrace.initTransactionTrace("tx delivery",
                                         (String) obj.getInfo("thinktime"), hid);

      dbtrace = dbVirtual.getNewOrder(wid);
      dbTrace.TransactionTrace(dbtrace, "r", tid, "neworder", iwid,hid);
      dbtrace.clear();

      dbtrace = dbVirtual.getCurrentNewOrder(wid);
      dbTrace.TransactionTrace(dbtrace, "r", tid, "neworder", iwid,hid);
      dbTrace.TransactionTrace(dbtrace, "w", tid, "neworder", iwid,hid);
      dbTrace.TransactionTrace(dbtrace, "r", tid, "orders", iwid,hid);
      dbTrace.TransactionTrace(dbtrace, "w", tid, "orders", iwid,hid);
      dbtrace.clear();

      dbtrace = dbVirtual.getCustomerNewOrder( (String) obj.getInfo("wid"));
      dbTrace.TransactionTrace(dbtrace, "r", tid, "customer", iwid,hid);
      dbTrace.TransactionTrace(dbtrace, "w", tid, "customer", iwid,hid);
      dbtrace.clear();

      dbtrace = dbVirtual.getOrderLineNewOrder( (String) obj.getInfo("wid"));
      dbTrace.TransactionTrace(dbtrace, "r", tid, "orderline", iwid,hid);
      dbTrace.TransactionTrace(dbtrace, "w", tid, "orderline", iwid,hid);
      dbtrace.clear();

      dbVirtual.insertNewOrder( (String) obj.getInfo("wid"));

      if (Integer.parseInt((String)obj.getInfo("abort")) == 0 )
      {
         TraceDeliveryDB = dbTrace.closeTransactionTrace(tid,hid);
      }
      else
      {
         TraceDeliveryDB = dbTrace.closeErrorTransactionTrace(tid,hid);
      }
    }
    catch (java.lang.Exception ex) {
      ex.printStackTrace(System.err);
    }
    return (TraceDeliveryDB);
  }

  public Object TraceOrderStatusDB(OutInfo obj,String hid) throws java.sql.
      SQLException {
    Object TraceOrderStatusDB = null;
    Iterator it = null;
    String tid = null;
    String cid = null;
    String wid = (String) obj.getInfo("wid");
    String did = (String) obj.getInfo("did");
    int iwid = Integer.parseInt(wid);
    int idid = Integer.parseInt(did);

    HashSet dbtrace = new HashSet();
    HashSet tmptrace = new HashSet();
    
    try {
      String str = (String) (obj).getInfo("cid");
      if (str.equals("0")) {
        HashSet customertrace = null;
        tid = dbTrace.initTransactionTrace("tx orderstatus 01",
                                           (String) obj.getInfo("thinktime"),hid);
        
        customertrace = dbVirtual.getCustomerLastName(wid,did,(String)obj.getInfo("lastname"));
        dbTrace.TransactionTrace(customertrace, "r", tid, "customer", iwid,hid);

        if (customertrace.size() == 0) {
          TraceOrderStatusDB = dbTrace.closeErrorTransactionTrace(tid,hid);
          return(TraceOrderStatusDB);
        }
        it = customertrace.iterator();
        
        tmptrace.add(wid + did + (String) it.next());
        dbTrace.TransactionTrace(tmptrace, "r", tid, "customer", iwid,hid);
        tmptrace.clear();

        it = customertrace.iterator();
        tmptrace.add(it.next());

        customertrace.clear();
      }
      else {
        tid = dbTrace.initTransactionTrace("tx orderstatus 02",
                                           (String) obj.getInfo("thinktime"),hid);

        dbtrace.add(wid + did + (String) obj.getInfo("cid"));
        tmptrace.add(obj.getInfo("cid"));
        dbTrace.TransactionTrace(dbtrace, "r", tid, "customer", iwid,hid);
        dbtrace.clear();
      }
      if (tmptrace.size() == 0) {
          TraceOrderStatusDB = dbTrace.closeErrorTransactionTrace(tid,hid);
          return(TraceOrderStatusDB);
      }

      it = tmptrace.iterator();
      cid = (String) it.next();
      tmptrace.clear();

      dbTrace.TransactionTrace(dbVirtual.getCustomerOrders(wid, did, cid), "r",
                               tid, "orders", iwid,hid);
      dbTrace.TransactionTrace(dbtrace, "r", tid, "orders", iwid,hid);
      dbTrace.TransactionTrace(dbVirtual.getCustomerOrders(wid, did, cid), "r",
                               tid, "orderline", iwid,hid);

      if (Integer.parseInt((String)obj.getInfo("abort")) == 0 )
      {
         TraceOrderStatusDB = dbTrace.closeTransactionTrace(tid,hid);
      }
      else
      {
         TraceOrderStatusDB = dbTrace.closeErrorTransactionTrace(tid,hid);
      }
    }
    catch (java.lang.Exception ex) {
      ex.printStackTrace(System.err);
    }

    return (TraceOrderStatusDB);

  }

  public Object TracePaymentDB(OutInfo obj,String hid) throws java.sql.SQLException {
    Object TracePaymentDB = null;
    String tid = null;
    Iterator it = null;
    String wid = (String) obj.getInfo("cwid");
    String did = (String) obj.getInfo("cdid");
    int iwid = Integer.parseInt(wid);

    HashSet dbtrace = new HashSet();
    HashSet tmptrace = new HashSet();
    
    try {
      String str = (String) (obj).getInfo("cid");
      if (str.equals("0")) {
        HashSet customertrace = null;
        tid = dbTrace.initTransactionTrace("tx payment 01",
                                           (String) obj.getInfo("thinktime"),hid);

        customertrace = dbVirtual.getCustomerLastName(wid,did,(String)obj.getInfo("lastname"));
        dbTrace.TransactionTrace(customertrace, "r", tid, "customer", iwid,hid);

        if (customertrace.size() == 0) {
          TracePaymentDB = dbTrace.closeErrorTransactionTrace(tid,hid);
          return(TracePaymentDB);
        }
        it = customertrace.iterator();
        
        tmptrace.add(wid + did + (String) it.next());
        dbTrace.TransactionTrace(tmptrace, "r", tid, "customer", iwid,hid);
        tmptrace.clear();

        it = customertrace.iterator();
        tmptrace.add(it.next());

        customertrace.clear();
      }
      else {
        tid = dbTrace.initTransactionTrace("tx payment 02",
                                           (String) obj.getInfo("thinktime"),hid);

        dbtrace.add(wid + did + (String) obj.getInfo("cid"));
        dbTrace.TransactionTrace(dbtrace, "r", tid, "customer", iwid,hid);
        tmptrace.add(wid + did + (String) obj.getInfo("cid"));
        dbtrace.clear();
      }
      if (tmptrace.size() == 0) {
          TracePaymentDB = dbTrace.closeErrorTransactionTrace(tid,hid);
          return(TracePaymentDB);
      }

      dbTrace.TransactionTrace(tmptrace, "r", tid, "customer", iwid,hid);
      dbTrace.TransactionTrace(tmptrace, "w", tid, "customer", iwid,hid);
      tmptrace.clear();

      dbtrace.add((String) obj.getInfo("wid") + obj.getInfo("did"));
      dbTrace.TransactionTrace(dbtrace, "r", tid, "district",
                               Integer.parseInt( (String) obj.getInfo("wid")),hid);
      dbTrace.TransactionTrace(dbtrace, "w", tid, "district",
                               Integer.parseInt( (String) obj.getInfo("wid")),hid);
      dbtrace.clear();

      dbtrace.add(obj.getInfo("wid"));
      dbTrace.TransactionTrace(dbtrace, "r", tid, "warehouse",
                               Integer.parseInt( (String) obj.getInfo("wid")),hid);
      dbTrace.TransactionTrace(dbtrace, "w", tid, "warehouse",
                               Integer.parseInt( (String) obj.getInfo("wid")),hid);

      dbtrace.clear();

      if (Integer.parseInt((String)obj.getInfo("abort")) == 0 )
      {
         TracePaymentDB = dbTrace.closeTransactionTrace(tid,hid);
      }
      else
      {
         TracePaymentDB = dbTrace.closeErrorTransactionTrace(tid,hid);
      }
    }
    catch (java.lang.Exception ex) {
      ex.printStackTrace(System.err);
    }
      return (TracePaymentDB);
  }

  public Object TraceStockLevelDB(OutInfo obj,String hid) throws java.sql.
      SQLException {
    Object TraceStockLevelDB = null;
    String tid = null;
    String wid = (String) obj.getInfo("wid");
    String did = (String) obj.getInfo("did");
    int iwid = Integer.parseInt(wid);
    int idid = Integer.parseInt(did);

    HashSet dbtrace = new HashSet();
    
    try {
      tid = dbTrace.initTransactionTrace("tx stocklevel",
                                         (String) obj.getInfo("thinktime"),hid);

      dbtrace.add(wid + did);
      dbTrace.TransactionTrace(dbtrace, "r", tid, "district", iwid,hid);
      dbtrace.clear();

      dbTrace.TransactionTrace(dbVirtual.getStockLevel(wid, did), "r", tid,
                               "stock", iwid,hid);

      dbTrace.TransactionTrace(dbVirtual.getOrderLineStockLevel(wid, did), "r",
                               tid, "orderline", iwid,hid);

      if (Integer.parseInt((String)obj.getInfo("abort")) == 0 )
      {
         TraceStockLevelDB = dbTrace.closeTransactionTrace(tid,hid);
      }
      else
      {
         TraceStockLevelDB = dbTrace.closeErrorTransactionTrace(tid,hid);
      }  
    }
    catch (java.lang.Exception ex) {
      ex.printStackTrace(System.err);
    }
    return (TraceStockLevelDB);   
  }
}
// arch-tag: 82714440-b6e5-4733-8843-58203c4778c1
